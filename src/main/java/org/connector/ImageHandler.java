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
import org.model.ImageSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.utils.Resize;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.Optional;

import static org.constants.ImageConstants.DESTINATION_BUCKET_NAME;
import static org.constants.ImageConstants.DOWNLOADED_IMAGE;
import static org.constants.ImageConstants.FILENAME;
import static org.constants.ImageConstants.RESIZED_IMAGE_PREFIX;
import static org.constants.ImageConstants.UPLOADED_IMAGE;

@AllArgsConstructor
@NoArgsConstructor
public class ImageHandler implements RequestHandler<String, String>{

    private Gson gson = new GsonBuilder()
            .registerTypeAdapterFactory(OptionalTypeAdapterFactory.getInstance())
            .create();
    private Resize resize = new Resize();
    private Logger logger = LoggerFactory.getLogger(ImageSource.class);
    private AmazonS3 s3Client = AmazonS3ClientBuilder.standard().build();

    @Override
    public String handleRequest(String input, Context context) {
        Type type = new TypeToken<ImageSource>(){}.getType();
        ImageSource image = gson.fromJson(input, type);
        BufferedImage handledImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);;

        Optional<String> srcBucket = image.getSourceBucket();
        Optional<String> fileName = image.getFileName();
        Optional<Integer> newLength = image.getNewHeight();
        Optional<Integer> newWidth = image.getNewWidth();
        Optional<Integer> percentage = image.getPercentage();
        Optional<Integer> rotation = image.getRotation();

        boolean shouldResizeByDimensions = newLength.isPresent() || newWidth.isPresent();
        boolean shouldResizeByPercentage = percentage.isPresent();
        boolean shouldRotate = rotation.isPresent();

        if(srcBucket.isEmpty() || fileName.isEmpty()) {
            throw new RuntimeException(ImageConstants.NO_IMAGE_FOUND);
        } else if((shouldResizeByDimensions ? 1 : 0) +
                (shouldResizeByPercentage ? 1 : 0) +
                (shouldRotate ? 1 : 0) != 1){
            throw new RuntimeException(ImageConstants.INVALID_OPERATION);
        }


        BufferedImage srcImage = downloadImageFromS3(srcBucket.get(), fileName.get());

        String status = ImageConstants.OPERATION_FAILED;
        if(shouldResizeByDimensions){
            logger.info("Image will be resized to new dimensions");
            handledImage = resize.resizeByDimensions(srcImage, newLength, newWidth);
            status = ImageConstants.IMAGE_RESIZED;
        } else if(shouldResizeByPercentage){
            logger.info("Image will be resized to new dimensions");
            handledImage = resize.resizeByPercentage(srcImage, percentage);
            status = ImageConstants.IMAGE_RESIZED;
        }

        uploadImageToS3(handledImage, DESTINATION_BUCKET_NAME, RESIZED_IMAGE_PREFIX + FILENAME);
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
}
