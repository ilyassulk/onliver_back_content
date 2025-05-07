package ru.onliver.content_manager.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "content")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Content {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "text")
    private String description;

    @Column(name = "avatar_object_name", nullable = false)
    private String avatarObjectName;

    @Column(name = "content_object_name", nullable = false)
    private String contentObjectName;
} 