package org.model;

import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ImageSource {
    private Optional<String> sourceBucket;
    private Optional<String> destBucket;
    private Optional<String> fileName;
    private Optional<Integer> newHeight;
    private Optional<Integer> newWidth;
    private Optional<Integer> percentage;
    private Optional<Integer> rotation;
    private boolean blackAndWhite;
    private boolean invertColour;
    private boolean sepiaTone;
    private boolean emboss;
    private boolean ocr;
}