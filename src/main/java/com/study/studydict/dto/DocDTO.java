package com.study.studydict.dto;

import com.study.studydict.model.Doc;

import java.time.LocalDateTime;

public record DocDTO(
        Long id,
        String title,
        String content,
        LocalDateTime recentUpdate,
        LocalDateTime createdDate


) {
    public DocDTO(Doc doc){
        this(doc.getId(), doc.getTitle(), doc.getContent(), doc.getRecentUpdate(), doc.getCreatedDate());
    }
}
