package org.model;

import java.util.Optional;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ImageSource {
    private Optional<String> sourceBucket;
    private Optional<String> fileName;
    private Optional<Integer> newHeight;
    private Optional<Integer> newWidth;
    private Optional<Integer> percentage;
    private Optional<Integer> rotation;
}