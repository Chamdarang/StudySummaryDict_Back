package com.study.studydict.dto;

import java.util.HashMap;
public record BaseReturnDTO(
        String status,
        String message,
        HashMap<String,Object> data
) {

    public BaseReturnDTO(String status,String message){
        this(status,message,null);
    }
}
