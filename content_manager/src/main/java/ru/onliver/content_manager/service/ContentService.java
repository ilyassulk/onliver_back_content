package ru.onliver.content_manager.service;

import ru.onliver.content_manager.model.ContentDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.onliver.content_manager.model.UploadUrlsRequestDto;
import ru.onliver.content_manager.model.UploadUrlsResponseDto;
import ru.onliver.content_manager.model.SaveContentRequestDto;
import ru.onliver.content_manager.model.SaveContentResponseDto;

public interface ContentService {
    Page<ContentDto> getAll(Pageable pageable);
    ContentDto getById(Long id);
    void deleteById(Long id);
    UploadUrlsResponseDto generateUploadUrls(UploadUrlsRequestDto request);
    SaveContentResponseDto saveContent(SaveContentRequestDto request);
    String getContentUrl(Long id);
} 