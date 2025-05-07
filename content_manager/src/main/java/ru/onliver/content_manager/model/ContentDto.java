package ru.onliver.content_manager.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContentDto {
    private Long id;
    private String name;
    private String description;
    private String avatarUrl;
} 