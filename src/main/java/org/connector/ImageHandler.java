package org.connector;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.adapter.OptionalTypeAdapterFactory;
import org.model.ImageSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.utils.Resize;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.Optional;

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
        logger.info("Input JSON: " + input);
        Type type = new TypeToken<ImageSource>(){}.getType();
        ImageSource image = gson.fromJson(input, type);

        logger.info("Processed Json");
        logger.info("Printing image: " + image);

        Optional<String> srcBucket = image.getSourceBucket();
        Optional<String> fileName = image.getFileName();
        Optional<Integer> newLength = image.getNewHeight();
        Optional<Integer> newWidth = image.getNewWidth();
        Optional<Integer> percentage = image.getPercentage();
        Optional<Integer> rotation = image.getRotation();

        logger.info("Inside ImageHandler");
        if(srcBucket.isEmpty() || fileName.isEmpty()) throw new RuntimeException("No S3 image found");
        logger.info("Length and Width are: " + newLength.isPresent() + " and " + newWidth.isPresent());

        BufferedImage srcImage = downloadImageFromS3(srcBucket.get(), fileName.get());

        String resizeStatus = "Could not resize";
        if(newLength.isPresent() || newWidth.isPresent()){
            resizeStatus = resize.handleResize(srcImage, newLength, newWidth);
        }
        return resizeStatus;
    }

    @SneakyThrows
    private BufferedImage downloadImageFromS3(String bucketName, String key) {
        GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, key);
        InputStream inputStream = s3Client.getObject(getObjectRequest).getObjectContent();
        return ImageIO.read(inputStream);
    }
}
