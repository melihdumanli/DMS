package com.melihdumanli.dms.repository;

import com.melihdumanli.dms.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Document,Long> {
    Boolean existsByFileName(String fileName);
    List<Document> getDocumentsByDeleteFlagFalse();
}
