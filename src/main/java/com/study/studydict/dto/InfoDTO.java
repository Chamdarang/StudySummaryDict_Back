package com.study.studydict.dto;

import java.time.LocalDateTime;
import java.util.List;

public record InfoDTO(
        Long id,
        String name,
        String simpleInfo,
        String detailInfo,
        List<String> tag,
        LocalDateTime recentUpdate,
        LocalDateTime createdDate


) {
}
