package com.study.studydict.dto;

import com.study.studydict.model.Info;

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

    public InfoDTO(Info info, List<String> tagList){
        this(
                info.getId(),
                info.getName(),
                info.getSimpleInfo(),
                info.getDetailInfo(),
                tagList,
                info.getRecentUpdate(),
                info.getCreatedDate()
        );
    }
}
