package com.study.studydict.controller;

import com.study.studydict.dto.BaseReturnDTO;
import com.study.studydict.dto.InfoDTO;
import com.study.studydict.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
@RestController
public class SearchController {
    private final SearchService infoService;
    @Autowired
    public SearchController(SearchService infoService){
        this.infoService=infoService;
    }
    @GetMapping(path = "/s")
    public BaseReturnDTO searchInfo(@RequestParam String query,@RequestParam int page,@RequestParam int size) {
        Pageable pageable= PageRequest.of(page,size);
        BaseReturnDTO infoList;
        if(query==null || query.isBlank()) {
            infoList = infoService.getAllInfo(pageable);
        }else{
            if(query.charAt(0)=='#') {
                infoList = infoService.getByTag(query.substring(1),pageable);
            }else{
                infoList = infoService.getByName(query,pageable);
            }
        }
        return infoList;
    }
    @PostMapping(path = "/a")
    public BaseReturnDTO addInfo(@RequestBody InfoDTO infoDTO) {
        return infoService.saveInfo(infoDTO);
    }
    @PostMapping(path = "/d")
    public BaseReturnDTO delInfo(@RequestBody InfoDTO infoDTO) {
        return infoService.deleteInfo(infoDTO);
    }

    @PostMapping(path = "/test")
    public String posttest() {
        return "test";
    }
    @GetMapping(path = "/test")
    public String gettest() {
        return "test";
    }
}
