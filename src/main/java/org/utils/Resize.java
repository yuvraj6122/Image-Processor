package org.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Optional;

public class Resize {
    private static final Logger logger = LoggerFactory.getLogger(Resize.class);

    public BufferedImage resizeByDimensions(BufferedImage srcImage, Optional<Integer> height, Optional<Integer> width) {
        int newHeight = height.orElseGet(srcImage::getHeight);
        int newWidth = width.orElseGet(srcImage::getWidth);
        logger.info("Resizing to new dimensions " + newHeight + " x " + newWidth);
        return resizeImage(srcImage, newWidth, newHeight);
    }

    public BufferedImage resizeByPercentage(BufferedImage srcImage, Optional<Integer> percentage) {
        int originalWidth = srcImage.getWidth();
        int originalHeight = srcImage.getHeight();

        double scaleFactor = percentage.orElse(100) / 100.0;
        int newWidth = (int) (originalWidth * scaleFactor);
        int newHeight = (int) (originalHeight * scaleFactor);

        logger.info("Resizing by percentage: " + percentage.orElse(100) + "%");
        return resizeImage(srcImage, newWidth, newHeight);
    }

    private BufferedImage resizeImage(BufferedImage srcImage, int newWidth, int newHeight) {
        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = resizedImage.createGraphics();
        graphics2D.drawImage(srcImage, 0, 0, newWidth, newHeight, null);
        graphics2D.dispose();
        return resizedImage;
    }
}