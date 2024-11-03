package org.example.dms.rest.mapper;

import org.example.dms.rest.dto.DocumentDTO;
import org.example.dms.rest.model.Document;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DocumentMapper {
    DocumentMapper INSTANCE = Mappers.getMapper(DocumentMapper.class);

    @Mapping(source = "created_at", target = "createdAt")
    @Mapping(source = "updated_at", target = "updatedAt")
    DocumentDTO toDocumentDTO(Document document);

    @Mapping(source = "createdAt", target = "created_at")
    @Mapping(source = "updatedAt", target = "updated_at")
    Document toDocument(DocumentDTO documentDTO);
}