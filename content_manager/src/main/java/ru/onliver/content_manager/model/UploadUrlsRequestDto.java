package ru.onliver.content_manager.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UploadUrlsRequestDto {
    private String avatarName;
    private String contentName;
} 