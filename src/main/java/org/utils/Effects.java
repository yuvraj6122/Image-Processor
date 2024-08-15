package org.utils;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Effects {
    public BufferedImage invertColors(BufferedImage srcImage){
        int width = srcImage.getWidth();
        int height = srcImage.getHeight();
        BufferedImage invertedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = srcImage.getRGB(x, y);
                int invertedRGB = (~rgb) & 0x00FFFFFF;
                invertedImage.setRGB(x, y, invertedRGB);
            }
        }
        return invertedImage;
    }

    public BufferedImage blackAndWhite(BufferedImage srcImage){
        BufferedImage bwImage = new BufferedImage(srcImage.getWidth(), srcImage.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D graphics = bwImage.createGraphics();
        graphics.drawImage(srcImage, 0, 0, null);
        graphics.dispose();
        return bwImage;
    }

    public BufferedImage applySepiaTone(BufferedImage srcImage) {
        BufferedImage sepiaImage = new BufferedImage(srcImage.getWidth(), srcImage.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = sepiaImage.createGraphics();
        graphics.drawImage(srcImage, 0, 0, null);
        graphics.dispose();

        for (int y = 0; y < sepiaImage.getHeight(); y++) {
            for (int x = 0; x < sepiaImage.getWidth(); x++) {
                Color color = new Color(sepiaImage.getRGB(x, y));
                int r = color.getRed();
                int g = color.getGreen();
                int b = color.getBlue();

                int tr = (int) (0.393 * r + 0.769 * g + 0.189 * b);
                int tg = (int) (0.349 * r + 0.686 * g + 0.168 * b);
                int tb = (int) (0.272 * r + 0.534 * g + 0.131 * b);

                if (tr > 255) tr = 255;
                if (tg > 255) tg = 255;
                if (tb > 255) tb = 255;

                Color newColor = new Color(tr, tg, tb);
                sepiaImage.setRGB(x, y, newColor.getRGB());
            }
        }
        return sepiaImage;
    }

    public BufferedImage applyEmbossEffect(BufferedImage srcImage) {
        int width = srcImage.getWidth();
        int height = srcImage.getHeight();

        BufferedImage embossImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                Color color1 = new Color(srcImage.getRGB(x - 1, y - 1));
                Color color2 = new Color(srcImage.getRGB(x, y));
                int r = color2.getRed() - color1.getRed() + 128;
                int g = color2.getGreen() - color1.getGreen() + 128;
                int b = color2.getBlue() - color1.getBlue() + 128;

                r = Math.min(Math.max(r, 0), 255);
                g = Math.min(Math.max(g, 0), 255);
                b = Math.min(Math.max(b, 0), 255);

                Color newColor = new Color(r, g, b);
                embossImage.setRGB(x, y, newColor.getRGB());
            }
        }
        return embossImage;
    }
}
