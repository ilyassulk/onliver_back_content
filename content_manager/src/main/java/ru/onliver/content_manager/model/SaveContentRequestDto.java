package ru.onliver.content_manager.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SaveContentRequestDto {
    private String title;
    private String description;
    private String avatarKey;
    private String contentKey;
} 