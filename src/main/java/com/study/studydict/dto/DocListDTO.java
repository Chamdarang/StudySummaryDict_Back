package com.study.studydict.dto;

import com.study.studydict.model.Doc;

import java.time.LocalDateTime;

public record DocListDTO(
        Long id,
        String title,
        LocalDateTime recentUpdate,
        LocalDateTime createdDate


) {
    public DocListDTO(Doc doc){
            this(doc.getId(), doc.getTitle(), doc.getRecentUpdate(), doc.getCreatedDate());
    }
}
