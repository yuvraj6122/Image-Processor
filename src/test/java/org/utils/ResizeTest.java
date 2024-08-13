//package org.utils;
//
//import com.amazonaws.services.s3.AmazonS3;
//import com.amazonaws.services.s3.model.GetObjectRequest;
//import com.amazonaws.services.s3.model.S3Object;
//import com.amazonaws.services.s3.model.S3ObjectInputStream;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.ArgumentMatchers;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import javax.imageio.ImageIO;
//import java.awt.image.BufferedImage;
//import java.io.ByteArrayInputStream;
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//public class ResizeTest {
//
//    private AmazonS3 amazonS3Client;
//
//    private Resize resize;
//
//    @BeforeEach
//    public void setup() {
//    }
//
//    @Test
//    public void testHandleResize() throws IOException {
//        // Mocking input parameters
//        Optional<String> srcBucket = Optional.of("image-bucket-origin");
//        Optional<String> fileName = Optional.of("testing.png");
//        Optional<Integer> height = Optional.empty();
//        Optional<Integer> width = Optional.empty();
//
//        // Mocking S3 client behavior
//        BufferedImage mockImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
//        S3Object mockS3Object = new S3Object();
//        S3ObjectInputStream mockInputStream = new S3ObjectInputStream(new ByteArrayInputStream(new byte[0]), null);
//        mockS3Object.setObjectContent(mockInputStream);
//        when(amazonS3Client.getObject(any(GetObjectRequest.class))).thenReturn(mockS3Object);
//        when(ImageIO.read(any(InputStream.class))).thenReturn(mockImage);
//
//        // Invoke method
//        String result = resize.handleResize(srcBucket, fileName, height, width);
//
//        // Assertions
//        assertEquals("Image resized successfully!", result);
//    }
//
//    @Test
//    public void testHandleResizeWithCustomDimensions() throws IOException {
//        // Mocking input parameters
//        Optional<String> srcBucket = Optional.of("image-bucket-origin");
//        Optional<String> fileName = Optional.of("testing.png");
//        Optional<Integer> height = Optional.of(50);
//        Optional<Integer> width = Optional.of(50);
//
//        // Mocking S3 client behavior
//        BufferedImage mockImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
//        S3Object mockS3Object = new S3Object();
//        S3ObjectInputStream mockInputStream = new S3ObjectInputStream(new ByteArrayInputStream(new byte[0]), null);
//        mockS3Object.setObjectContent(mockInputStream);
//        when(amazonS3Client.getObject(any(GetObjectRequest.class))).thenReturn(mockS3Object);
//        when(ImageIO.read(any(InputStream.class))).thenReturn(mockImage);
//
//        // Invoke method
//        String result = resize.handleResize(srcBucket, fileName, height, width);
//
//        // Assertions
//        assertEquals("Image resized successfully!", result);
//    }
//}
