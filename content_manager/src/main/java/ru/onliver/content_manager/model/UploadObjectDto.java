package ru.onliver.content_manager.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UploadObjectDto {
    private String originalName;
    private String objectKey;
    private String uploadUrl;
} 