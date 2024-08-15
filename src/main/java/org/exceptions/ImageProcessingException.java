package org.exceptions;

import org.constants.ImageConstants;

public class ImageProcessingException extends RuntimeException{
    public ImageProcessingException(String message) {
        super(message);
    }

    public ImageProcessingException(String message, Throwable cause) {
        super(message, cause);
    }

    public ImageProcessingException(){
        super(ImageConstants.IMAGE_PROCESSING_EXCEPTION);
    }
}
