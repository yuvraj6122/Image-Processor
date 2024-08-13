package org.utils;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Optional;

public class Resize{
    private static final String DESTINATION_BUCKET_NAME = "image-bucket-origin-destination";
    private static final String RESIZED_IMAGE_PREFIX = "resized/";
    private static final Logger logger = LoggerFactory.getLogger(Resize.class);

    private AmazonS3 s3Client = AmazonS3ClientBuilder.standard().build();

    public String handleResize(Optional<String> srcBucket, Optional<String> fileName, Optional<Integer> height, Optional<Integer> width) {
        logger.info("Inside Resize method for: \n" + srcBucket + " from " + fileName);
        try {
            BufferedImage srcImage = downloadImageFromS3(srcBucket.get(), fileName.get());
            logger.info("Downloaded image from S3");

            Integer newHeight = height.orElseGet(srcImage::getHeight);
            Integer newWidth = width.orElseGet(srcImage::getWidth);
            logger.info("Resizing to new dimensions" + newHeight + " x " + newWidth);

            BufferedImage resizedImage = resizeImage(srcImage, newHeight, newWidth);
            logger.info("Image resized! Uploading to S3 Bucket");

            uploadImageToS3(resizedImage, DESTINATION_BUCKET_NAME, RESIZED_IMAGE_PREFIX + fileName.orElse("empty"));

            return "Image resized successfully!";
        } catch (IOException e) {
            throw new RuntimeException("Error resizing image: " + e.getMessage());
        }
    }

    private BufferedImage downloadImageFromS3(String bucketName, String key) throws IOException {
        GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, key);
        InputStream inputStream = s3Client.getObject(getObjectRequest).getObjectContent();
        return ImageIO.read(inputStream);
    }

    private void uploadImageToS3(BufferedImage image, String bucketName, String key) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", os);
        InputStream is = new ByteArrayInputStream(os.toByteArray());
        ObjectMetadata meta = new ObjectMetadata();
        meta.setContentLength(os.size());
        s3Client.putObject(bucketName, key, is, meta);
    }

    private BufferedImage resizeImage(BufferedImage originalImage, Integer targetHeight, Integer targetWidth) {
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = resizedImage.createGraphics();
        graphics2D.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        graphics2D.dispose();
        return resizedImage;
    }
}