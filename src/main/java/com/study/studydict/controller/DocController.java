package com.study.studydict.controller;

import com.study.studydict.dto.BaseReturnDTO;
import com.study.studydict.dto.DocDTO;
import com.study.studydict.service.DocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
public class DocController {
    private final DocService docService;
    @Autowired
    public DocController(DocService docService){
        this.docService=docService;
    }
    @GetMapping(path = "/d/s")
    public BaseReturnDTO getDocList(@RequestParam String query, @RequestParam int page, @RequestParam int size){
        Pageable pageable= PageRequest.of(page,size);
        BaseReturnDTO docList;
        if (query==null || query==""){
            docList=docService.getAllDocList(pageable);
        }else{
            docList=docService.getDocListByTitle(query,pageable);
        }
        return docList;
    }
    @GetMapping(path = "/d/v")
    public BaseReturnDTO getDocDetail(@RequestParam Long id){
        return docService.getDocDetail(id,true);
    }
    @GetMapping(path = "/d/e")
    public BaseReturnDTO getDocDetailForEdit(@RequestParam Long id){
        return docService.getDocDetail(id,false);
    }
    @PostMapping(path = "/d/a")
    public BaseReturnDTO saveDoc(@RequestBody DocDTO docDTO){
        return docService.saveDoc(docDTO);
    }
    @PostMapping(path = "/d/d")
    public BaseReturnDTO deleteDoc(@RequestBody DocDTO docDTO){
        return docService.deleteDoc(docDTO);
    }
    @GetMapping(path = "/d/test")
    public String dtest() {
        return "test";
    }
}
