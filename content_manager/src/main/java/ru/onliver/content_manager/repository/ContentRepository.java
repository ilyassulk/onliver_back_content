package ru.onliver.content_manager.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.onliver.content_manager.model.Content;

@Repository
public interface ContentRepository extends JpaRepository<Content, Long> {
    Page<Content> findAll(Pageable pageable);
} 