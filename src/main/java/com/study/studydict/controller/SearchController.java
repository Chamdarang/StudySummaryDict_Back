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
    private final SearchService searchService;
    @Autowired
    public SearchController(SearchService searchService){
        this.searchService=searchService;
    }
    @GetMapping(path = "/i/s")
    public BaseReturnDTO searchInfo(@RequestParam String query,@RequestParam int page,@RequestParam int size) {
        Pageable pageable= PageRequest.of(page,size);
        BaseReturnDTO infoList;
        if(query==null || query.isBlank()) {
            infoList = searchService.getAllInfo(pageable);
        }else{
            if(query.charAt(0)=='#') {
                infoList = searchService.getByTag(query.substring(1),pageable);
            }else{
                infoList = searchService.getByName(query,pageable);
            }
        }
        return infoList;
    }
    @PostMapping(path = "/i/a")
    public BaseReturnDTO addInfo(@RequestBody InfoDTO infoDTO) {
        return searchService.saveInfo(infoDTO);
    }
    @PostMapping(path = "/i/d")
    public BaseReturnDTO delInfo(@RequestBody InfoDTO infoDTO) {
        return searchService.deleteInfo(infoDTO);
    }
    @GetMapping(path = "/i/r")
    public BaseReturnDTO randQuiz() {
        return searchService.randQuiz();
    }

    @GetMapping(path = "/i/test")
    public String itest() {
        return "test";
    }

}
