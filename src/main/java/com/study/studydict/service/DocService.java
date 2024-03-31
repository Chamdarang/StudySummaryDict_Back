package com.study.studydict.service;

import com.study.studydict.dto.BaseReturnDTO;
import com.study.studydict.dto.DocDTO;
import com.study.studydict.dto.DocListDTO;
import com.study.studydict.model.Doc;
import com.study.studydict.repository.DocRepository;
import com.study.studydict.util.MarkdownUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DocService {

    private final DocRepository docRepository;
    private final MarkdownUtil markdownUtil;

    public DocService(DocRepository docRepository, MarkdownUtil markdownUtil){
        this.docRepository=docRepository;
        this.markdownUtil = markdownUtil;
    }
    @Transactional(readOnly = true)
    public BaseReturnDTO getAllDocList(Pageable pageable){
        HashMap<String,Object> ret_data=new HashMap<>();
        Page<Doc> DocList=docRepository.findAllByOrderByRecentUpdateDesc(pageable);
        List<DocListDTO> DocListDTO=DocList.stream()
                .map(doc -> new DocListDTO(doc))
                .collect(Collectors.toList());
        ret_data.put("docList",DocListDTO);
        ret_data.put("totalPage",DocList.getTotalPages());
        return new BaseReturnDTO("Success","",ret_data);
    }
    public BaseReturnDTO getDocListByTitle(String title,Pageable pageable){
        HashMap<String,Object> ret_data=new HashMap<>();
        Page<Doc> DocList=docRepository.findAllByTitleContainingIgnoreCaseOrderByRecentUpdateDesc(title,pageable);
        List<DocListDTO> DocListDTO=DocList.stream()
                .map(doc -> new DocListDTO(doc))
                .collect(Collectors.toList());
        ret_data.put("docList",DocListDTO);
        ret_data.put("totalPage",DocList.getTotalPages());
        return new BaseReturnDTO("Success","",ret_data);
    }
    public BaseReturnDTO getDocDetail(Long id,boolean isView){
        HashMap<String,Object> ret_data=new HashMap<>();
        Optional<Doc> docOptional=docRepository.findById(id);
        if (docOptional.isPresent()){
            Doc doc=docOptional.get();
            if (isView){
                doc.setContent(markdownUtil.makeMarkDown(doc.getContent()));
            }
            ret_data.put("doc",new DocDTO(doc));
        }else {
            ret_data.put("doc", new DocDTO(new Doc()));
        }
        return new BaseReturnDTO("Success","",ret_data);
    }
    public BaseReturnDTO saveDoc(DocDTO docDTO){
        HashMap<String,Object> ret_data=new HashMap<>();
        String message="New";
        Doc doc=new Doc();
        doc.setId(docDTO.id());
        doc.setTitle(docDTO.title());
        doc.setContent(docDTO.content());
        if(docRepository.findByTitleIgnoreCase(docDTO.title())!=null){ //중복체크
            if(docDTO.id().equals((long) -1)) {
                return new BaseReturnDTO("Fail", "Data Already Exist");
            }else{
                message="Update";
            }
        }

        Long id= docRepository.save(doc).getId();
        ret_data.put("id",id);
        return new BaseReturnDTO("Success",message,ret_data);
    }
    public BaseReturnDTO deleteDoc(DocDTO docDTO){
        Doc doc=new Doc();
        doc.setId(docDTO.id());
        docRepository.delete(doc);
        return new BaseReturnDTO("Success","");
    }


}
