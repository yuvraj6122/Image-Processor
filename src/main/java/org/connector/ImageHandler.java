package org.connector;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.adapter.OptionalTypeAdapterFactory;
import org.model.ImageSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.utils.Resize;

import java.lang.reflect.Type;
import java.util.Optional;

public class ImageHandler implements RequestHandler<String, String>{

    private final Gson gson = new GsonBuilder()
            .registerTypeAdapterFactory(OptionalTypeAdapterFactory.getInstance())
            .create();
    private final Resize resize = new Resize();
    private final Logger logger = LoggerFactory.getLogger(ImageSource.class);

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

        String resizeStatus = "Could not resize";
        if(newLength.isPresent() || newWidth.isPresent()){
            resizeStatus = resize.handleResize(srcBucket, fileName, newLength, newWidth);
        }
        return resizeStatus;
    }
}
