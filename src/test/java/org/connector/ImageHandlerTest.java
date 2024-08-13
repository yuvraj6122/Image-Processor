//package org.connector;
//
//import com.amazonaws.services.s3.AmazonS3;
//import com.google.gson.reflect.TypeToken;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.model.ImageSource;
//import org.utils.Resize;
//
//import java.lang.reflect.Type;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//
//public class ImageHandlerTest {
//
//    private ImageHandler imageHandler;
//
//    @Mock
//    private Resize resize;
//
//    @Mock
//    private AmazonS3 s3Client;
//
//    @BeforeEach
//    public void setup() {
//        MockitoAnnotations.openMocks(this);
//        imageHandler = new ImageHandler();
//    }
//
//    @Test
//    public void test_handleRequest_Success(){
//        // Mocked input JSON and expected result
//        String input = "{\"sourceBucket\":\"image-bucket-origin\",\"fileName\":\"testing.png\",\"newHeight\":100,\"newWidth\":100}";
//        String expectedResizeStatus = "Resized";
//
//        // Mock behavior of Gson to return a mocked ImageSource object
//        Type type = new TypeToken<ImageSource>(){}.getType();
//
//        // Mock behavior of Resize dependency
//        when(resize.handleResize(any(), any(), any(), any())).thenReturn(expectedResizeStatus);
//        when(s3Client.getObject(any())).thenReturn(null);
//
//        // Execute the method under test
//        String result = imageHandler.handleRequest(input, null);
//
//        // Verify expected behavior
//        Assertions.assertEquals(expectedResizeStatus, result);
//    }
//}
