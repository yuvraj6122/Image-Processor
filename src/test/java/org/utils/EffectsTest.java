package org.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.Color;
import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class EffectsTest{
    private Effects effects;
    private BufferedImage testImage;

    @BeforeEach
    public void init() {
        effects = new Effects();
        testImage = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 10; x++) {
                testImage.setRGB(x, y, new Color(x * 25, y * 25, (x + y) * 10).getRGB());
            }
        }
    }

    @Test
    public void testInvertColors() {
        BufferedImage invertedImage = effects.invertColors(testImage);
        assertNotNull(invertedImage);
        assertEquals(~testImage.getRGB(0, 0) & 0x00FFFFFF, invertedImage.getRGB(0, 0) & 0x00FFFFFF);
    }

    @Test
    public void testBlackAndWhite() {
        BufferedImage bwImage = effects.blackAndWhite(testImage);
        assertNotNull(bwImage);
        assertEquals(BufferedImage.TYPE_BYTE_GRAY, bwImage.getType());
    }

    @Test
    public void testApplySepiaTone() {
        BufferedImage sepiaImage = effects.applySepiaTone(testImage);
        assertNotNull(sepiaImage);
    }

    @Test
    public void testApplyEmbossEffect() {
        BufferedImage embossImage = effects.applyEmbossEffect(testImage);
        assertNotNull(embossImage);
    }
}
