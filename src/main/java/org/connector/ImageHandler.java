package org.connector;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.adapter.OptionalTypeAdapterFactory;
import org.constants.ImageConstants;
import org.exceptions.ImageProcessingException;
import org.model.ImageSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.utils.Effects;
import org.utils.OCR;
import org.utils.Resize;
import org.utils.Rotate;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.Optional;

import static org.constants.ImageConstants.DOWNLOADED_IMAGE;
import static org.constants.ImageConstants.MODIFIED_IMAGE_PREFIX;
import static org.constants.ImageConstants.UPLOADED_IMAGE;


/**
 * This class receives the message on AWS Lambda and calls methods to perform the following operations
 * 1. Resize (by percentage or by dimensions)
 * 2. Rotate (by angle)
 * 3. Effects
 *      - Invert colours
 *      - Greyscale
 *      - Sepia Tone
 *      - Emboss
 * 4. Optical Character Recognition (OCR)
 * */
@AllArgsConstructor
@NoArgsConstructor
public class ImageHandler implements RequestHandler<String, String>{
    private Resize resize = new Resize();
    private Rotate rotate = new Rotate();
    private Effects effects = new Effects();
    private OCR ocr = new OCR();
    private Logger logger = LoggerFactory.getLogger(ImageSource.class);
    private AmazonS3 s3Client = AmazonS3ClientBuilder.standard().build();
    private Gson gson = new GsonBuilder()
            .registerTypeAdapterFactory(OptionalTypeAdapterFactory.getInstance())
            .create();

    @Override
    public String handleRequest(String input, Context context) {
        Type type = new TypeToken<ImageSource>(){}.getType();
        ImageSource image = gson.fromJson(input, type);
        BufferedImage handledImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);;

        Optional<String> srcBucket = image.getSourceBucket();
        Optional<String> destBucket = image.getDestBucket();
        Optional<String> fileName = image.getFileName();
        Optional<Integer> newLength = image.getNewHeight();
        Optional<Integer> newWidth = image.getNewWidth();
        Optional<Integer> percentage = image.getPercentage();
        Optional<Integer> rotation = image.getRotation();
        boolean blackAndWhite = image.isBlackAndWhite();
        boolean invertColour = image.isInvertColour();
        boolean sepiaTone = image.isSepiaTone();
        boolean emboss = image.isEmboss();
        boolean doOCR = image.isOcr();

        boolean shouldResizeByDimensions = newLength.isPresent() || newWidth.isPresent();
        boolean shouldResizeByPercentage = percentage.isPresent();
        boolean shouldRotate = rotation.isPresent();

        if(srcBucket.isEmpty() || fileName.isEmpty() || destBucket.isEmpty()) {
            throw new RuntimeException(ImageConstants.NO_IMAGE_FOUND);
        } else if((shouldResizeByDimensions ? 1 : 0) + (shouldResizeByPercentage ? 1 : 0) + (shouldRotate ? 1 : 0) +
                (invertColour ? 1 : 0) + (blackAndWhite ? 1 : 0) + (sepiaTone ? 1 : 0) + (emboss ? 1 : 0) + (doOCR ? 1 : 0)
                != 1){
            throw new RuntimeException(ImageConstants.INVALID_OPERATION);
        }

        BufferedImage srcImage = downloadImageFromS3(srcBucket.get(), fileName.get());
        String status = ImageConstants.OPERATION_FAILED;

        if(shouldResizeByDimensions){
            logger.info("Image will now be resized to new dimensions");
            handledImage = resize.resizeByDimensions(srcImage, newLength, newWidth);
            status = ImageConstants.IMAGE_RESIZED;
        } else if(shouldResizeByPercentage){
            logger.info("Image will now be resized to new dimensions");
            handledImage = resize.resizeByPercentage(srcImage, percentage);
            status = ImageConstants.IMAGE_RESIZED;
        } else if(shouldRotate){
            logger.info("Trying to rotate image");
            handledImage = rotate.rotate(srcImage, rotation);
            status = ImageConstants.IMAGE_ROTATED;
        } else if(invertColour){
            logger.info("Inverting colours");
            handledImage = effects.invertColors(srcImage);
            status = ImageConstants.IMAGE_INVERTED;
        } else if(blackAndWhite){
            logger.info("Image will now be greyscaled");
            handledImage = effects.blackAndWhite(srcImage);
            status = ImageConstants.IMAGE_GREYSCALED;
        } else if(sepiaTone){
            logger.info("Sepia Tone effect will be applied to image");
            handledImage = effects.applySepiaTone(srcImage);
            status = ImageConstants.IMAGE_TONED;
        } else if(emboss){
            logger.info("Emboss effect will be applied to image");
            handledImage = effects.applyEmbossEffect(srcImage);
            status = ImageConstants.IMAGE_EMBOSS;
        } else if(doOCR){
            logger.info("Implementing OCR on the image");
            status = ocr.performOCR(srcImage);
            return status;
        }
        uploadImageToS3(handledImage, destBucket.get(), MODIFIED_IMAGE_PREFIX + fileName.get());
        return status;
    }

    @SneakyThrows
    private BufferedImage downloadImageFromS3(String bucketName, String key) {
        GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, key);
        InputStream inputStream = s3Client.getObject(getObjectRequest).getObjectContent();
        logger.info(DOWNLOADED_IMAGE);
        return ImageIO.read(inputStream);
    }

    @SneakyThrows
    private void uploadImageToS3(BufferedImage image, String bucketName, String key){
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", os);
        InputStream is = new ByteArrayInputStream(os.toByteArray());
        ObjectMetadata meta = new ObjectMetadata();
        meta.setContentLength(os.size());
        s3Client.putObject(bucketName, key, is, meta);
        logger.info(UPLOADED_IMAGE);
    }

    private String getImageFormat(String fileName){
        int extensionIndex = fileName.lastIndexOf('.');
        if(extensionIndex < 1 || extensionIndex == fileName.length() - 1){
            throw new ImageProcessingException("Invalid name or format for source image");
        }
        return fileName.substring(extensionIndex);
    }
}
