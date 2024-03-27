package com.study.studydict.dto;

import com.study.studydict.model.Info;

public record QuizDTO(
        String name,
        String simpleInfo,
        String userAnswer,
        int correct


) {

    public QuizDTO(Info info){
        this(
                info.getName(),

                info.getSimpleInfo(),
                "",
                0
        );
    }
}
