package ru.onliver.content_manager.service.impl;

import ru.onliver.content_manager.service.ContentService;
import ru.onliver.content_manager.repository.ContentRepository;
import ru.onliver.content_manager.model.ContentDto;
import ru.onliver.content_manager.model.Content;
import ru.onliver.content_manager.util.MinioUrlUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import ru.onliver.content_manager.model.UploadUrlsRequestDto;
import ru.onliver.content_manager.model.UploadUrlsResponseDto;
import ru.onliver.content_manager.model.UploadObjectDto;
import ru.onliver.content_manager.model.SaveContentRequestDto;
import ru.onliver.content_manager.model.SaveContentResponseDto;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class ContentServiceImpl implements ContentService {

    private final ContentRepository contentRepository;
    private final MinioUrlUtil minioUrlUtil;

    public ContentServiceImpl(ContentRepository contentRepository, MinioUrlUtil minioUrlUtil) {
        this.contentRepository = contentRepository;
        this.minioUrlUtil = minioUrlUtil;
    }

    @Override
    public Page<ContentDto> getAll(Pageable pageable) {
        return contentRepository.findAll(pageable)
                .map(this::toDto);
    }

    @Override
    public ContentDto getById(Long id) {
        Content content = contentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Content not found with id " + id));
        return toDto(content);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        if (!contentRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Content not found with id " + id);
        }
        contentRepository.deleteById(id);
    }

    @Override
    public UploadUrlsResponseDto generateUploadUrls(UploadUrlsRequestDto request) {
        String avatarExt = request.getAvatarName().contains(".") ? request.getAvatarName().substring(request.getAvatarName().lastIndexOf(".")) : "";
        String avatarKey = UUID.randomUUID().toString() + avatarExt;
        String avatarUrl = minioUrlUtil.getAvatarUploadUrl(avatarKey);

        String contentExt = request.getContentName().contains(".") ? request.getContentName().substring(request.getContentName().lastIndexOf(".")) : "";
        String contentKey = UUID.randomUUID().toString() + contentExt;
        String contentUrl = minioUrlUtil.getContentUploadUrl(contentKey);

        UploadObjectDto avatarObj = UploadObjectDto.builder()
                .originalName(request.getAvatarName())
                .objectKey(avatarKey)
                .uploadUrl(avatarUrl)
                .build();
        UploadObjectDto contentObj = UploadObjectDto.builder()
                .originalName(request.getContentName())
                .objectKey(contentKey)
                .uploadUrl(contentUrl)
                .build();

        return UploadUrlsResponseDto.builder()
                .avatar(avatarObj)
                .content(contentObj)
                .build();
    }

    @Override
    @Transactional
    public SaveContentResponseDto saveContent(SaveContentRequestDto request) {
        Content content = Content.builder()
                .name(request.getTitle())
                .description(request.getDescription())
                .avatarObjectName(request.getAvatarKey())
                .contentObjectName(request.getContentKey())
                .build();
        Content saved = contentRepository.save(content);
        return SaveContentResponseDto.builder()
                .id(saved.getId())
                .title(saved.getName())
                .description(saved.getDescription())
                .avatarKey(saved.getAvatarObjectName())
                .contentKey(saved.getContentObjectName())
                .build();
    }

    @Override
    public String getContentUrl(Long id) {
        Content content = contentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Content not found with id " + id));

        return minioUrlUtil.getContentUrl(content.getContentObjectName());
    }

    private ContentDto toDto(Content content) {
        return ContentDto.builder()
                .id(content.getId())
                .name(content.getName())
                .description(content.getDescription())
                .avatarUrl(minioUrlUtil.getAvatarUrl(content.getAvatarObjectName()))
                .build();
    }
} 