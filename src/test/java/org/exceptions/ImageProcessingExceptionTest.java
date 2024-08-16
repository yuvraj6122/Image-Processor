package org.exceptions;

import org.constants.ImageConstants;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ImageProcessingExceptionTest {

    @Test
    public void testMessageConstructor() {
        String errorMessage = "An error occurred during image processing.";
        ImageProcessingException exception = new ImageProcessingException(errorMessage);
        assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    public void testMessageAndCauseConstructor() {
        String errorMessage = "An error occurred during image processing.";
        Throwable cause = new IllegalArgumentException("Invalid argument passed.");
        ImageProcessingException exception = new ImageProcessingException(errorMessage, cause);
        assertEquals(errorMessage, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    public void testDefaultConstructor() {
        ImageProcessingException exception = new ImageProcessingException();
        assertEquals(ImageConstants.IMAGE_PROCESSING_EXCEPTION, exception.getMessage());
    }

    @Test
    public void testThrowException() {
        assertThrows(ImageProcessingException.class, () -> {
            throw new ImageProcessingException("Testing exception throwing.");
        });
    }

}
