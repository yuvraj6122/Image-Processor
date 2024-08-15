package org.utils;

import com.amazonaws.services.textract.AmazonTextract;
import com.amazonaws.services.textract.AmazonTextractClientBuilder;
import com.amazonaws.services.textract.model.*;
import lombok.SneakyThrows;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.List;

public class OCR {
    private final AmazonTextract client = AmazonTextractClientBuilder.defaultClient();

    @SneakyThrows
    public String performOCR(BufferedImage bufferedImage){
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", os);
        ByteBuffer imageBytes = ByteBuffer.wrap(os.toByteArray());
        os.close();

        DetectDocumentTextRequest request = new DetectDocumentTextRequest()
                .withDocument(new Document().withBytes(imageBytes));

        DetectDocumentTextResult result = client.detectDocumentText(request);
        List<Block> blocks = result.getBlocks();

        StringBuilder stringBuilder = new StringBuilder();
        for (Block block : blocks) {
            if (block.getBlockType().equals("LINE")) {
                stringBuilder.append(block.getText()).append("\n");
            }
        }
        return stringBuilder.toString();
    }
}
