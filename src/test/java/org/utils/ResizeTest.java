package org.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.awt.image.BufferedImage;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ResizeTest {
    @InjectMocks
    Resize resize;

    BufferedImage srcImage;
    @BeforeEach
    public void init(){
        MockitoAnnotations.initMocks(this);
        srcImage = new BufferedImage(10, 10, 1);
    }

    @Test
    public void test_resizeByDimensions_Success(){
        assertNotNull(resize.resizeByDimensions(srcImage, Optional.of(1), Optional.of(1)));
    }

    @Test
    public void test_resizeByPercentage_Success(){
        assertNotNull(resize.resizeByPercentage(srcImage, Optional.of(50)));
    }
}