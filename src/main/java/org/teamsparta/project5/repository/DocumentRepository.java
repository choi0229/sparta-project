package org.teamsparta.project5.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.teamsparta.project5.entity.DocumentEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface DocumentRepository extends JpaRepository<DocumentEntity, UUID> {
    List<DocumentEntity> findByFilenameContaining(String filename);
    List<DocumentEntity> findByContentType(String contentType);
}
