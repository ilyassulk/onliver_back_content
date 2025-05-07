package ru.onliver.content_manager.controller;

import org.springframework.http.HttpMethod;
import ru.onliver.content_manager.service.ContentService;
import ru.onliver.content_manager.model.ContentDto;
import ru.onliver.content_manager.model.UploadUrlsRequestDto;
import ru.onliver.content_manager.model.UploadUrlsResponseDto;
import ru.onliver.content_manager.model.SaveContentRequestDto;
import ru.onliver.content_manager.model.SaveContentResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.onliver.content_manager.util.MinioUrlUtil;

@RestController
@RequestMapping("/content")
public class ContentController {

    private final ContentService contentService;

    public ContentController(ContentService contentService) {
        this.contentService = contentService;
    }


    @RequestMapping(method = RequestMethod.OPTIONS)
    public ResponseEntity<Void> optionsContent() {
        return ResponseEntity
                .ok()
                .allow(HttpMethod.GET, HttpMethod.POST, HttpMethod.OPTIONS)
                .build();
    }

    @RequestMapping(method = RequestMethod.OPTIONS, path = "/{id}")
    public ResponseEntity<HttpStatus> optionsContentById(@PathVariable Long id) {
        return ResponseEntity.ok().allow(HttpMethod.DELETE, HttpMethod.GET, HttpMethod.OPTIONS).build();
    }

    @RequestMapping(method = RequestMethod.OPTIONS, path = "/{id}/url")
    public ResponseEntity<HttpStatus> optionsContentURLById(@PathVariable Long id) {
        return ResponseEntity.ok().allow(HttpMethod.GET, HttpMethod.OPTIONS).build();
    }

    @RequestMapping(method = RequestMethod.OPTIONS, path = "/uploadurls")
    public ResponseEntity<HttpStatus> optionsUploadUrls() {
        return ResponseEntity.ok().allow(HttpMethod.POST, HttpMethod.OPTIONS).build();
    }



    @GetMapping
    public ResponseEntity<Page<ContentDto>> getAll(Pageable pageable) {
        Page<ContentDto> page = contentService.getAll(pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContentDto> getById(@PathVariable Long id) {
        ContentDto dto = contentService.getById(id);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        contentService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/url")
    public ResponseEntity<String> getURLById(@PathVariable Long id) {
        return ResponseEntity.ok(contentService.getContentUrl(id));
    }

    @PostMapping("/uploadurls")
    public ResponseEntity<UploadUrlsResponseDto> generateUploadUrls(@RequestBody UploadUrlsRequestDto request) {
        UploadUrlsResponseDto response = contentService.generateUploadUrls(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<SaveContentResponseDto> saveContent(@RequestBody SaveContentRequestDto request) {
        SaveContentResponseDto response = contentService.saveContent(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
} 