package ru.onliver.content_manager.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UploadUrlsResponseDto {
    private UploadObjectDto avatar;
    private UploadObjectDto content;
} 