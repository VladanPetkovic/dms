package org.example.paperless_rest.repository;

import org.example.paperless_rest.model.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    Page<Document> findByNameContainingIgnoreCase(String name, Pageable pageable);

    @Query("SELECT d.path FROM Document d WHERE d.id = :id")
    Optional<String> findByIdGetPath(Long id);

    @Query("SELECT d FROM Document d WHERE d.path IN :fileNames")
    Page<Document> findByFileNames(@Param("fileNames") List<String> fileNames, Pageable pageable);

    Document findDocumentByPath(String fileName);
}
