package org.example.dms.rest.repository;

import org.example.dms.rest.model.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    Page<Document> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
