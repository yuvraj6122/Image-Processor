package org.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.awt.image.BufferedImage;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class RotateTest{

    private Rotate rotate;
    private BufferedImage testImage;

    @BeforeEach
    public void setUp() {
        rotate = new Rotate();
        testImage = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 10; x++) {
                testImage.setRGB(x, y, (x * 25) << 16 | (y * 25) << 8 | ((x + y) * 10));
            }
        }
    }

    @Test
    public void testRotate() {
        Optional<Integer> rotationAngle = Optional.of(90);
        BufferedImage rotatedImage = rotate.rotate(testImage, rotationAngle);
        assertNotNull(rotatedImage);
        assertEquals(testImage.getHeight(), rotatedImage.getWidth());
        assertEquals(testImage.getWidth(), rotatedImage.getHeight());
    }
}
