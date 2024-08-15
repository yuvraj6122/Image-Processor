package org.utils;

import java.awt.Graphics2D;
import java.awt.Transparency;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.Optional;

public class Rotate {
    public BufferedImage rotate(BufferedImage srcImage, Optional<Integer> rotationAngle){
        double angle = Math.toRadians(rotationAngle.get());
        double sin = Math.abs(Math.sin(angle));
        double cos = Math.abs(Math.cos(angle));
        int w = srcImage.getWidth();
        int h = srcImage.getHeight();
        int newWidth = (int) Math.floor(w * cos + h * sin);
        int newHeight = (int) Math.floor(h * cos + w * sin);

        BufferedImage result = new BufferedImage(newWidth, newHeight, Transparency.TRANSLUCENT);
        Graphics2D g = result.createGraphics();
        g.translate((newWidth - w) / 2, (newHeight - h) / 2);
        g.rotate(angle, w / 2, h / 2);
        g.drawRenderedImage(srcImage, null);
        g.dispose();
        return result;
    }
}
