package com.study.studydict.service;

import com.study.studydict.dto.BaseReturnDTO;
import com.study.studydict.dto.DocDTO;
import com.study.studydict.dto.DocListDTO;
import com.study.studydict.model.Doc;
import com.study.studydict.model.Info;
import com.study.studydict.repository.DocRepository;
import com.study.studydict.repository.InfoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class DocService {

    private final DocRepository docRepository;
    private final InfoRepository infoRepository;

    public DocService(DocRepository docRepository,InfoRepository infoRepository){
        this.docRepository=docRepository;
        this.infoRepository=infoRepository;
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
                doc.setContent(makeMarkDown(doc.getContent()));
            }
            ret_data.put("doc",new DocDTO(doc));
        }else {
            ret_data.put("doc", new DocDTO(new Doc()));
        }
        return new BaseReturnDTO("Success","",ret_data);
    }
    public BaseReturnDTO saveDoc(DocDTO docDTO){
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
        docRepository.save(doc);
        return new BaseReturnDTO("Success",message);
    }
    public BaseReturnDTO deleteDoc(DocDTO docDTO){
        Doc doc=new Doc();
        doc.setId(docDTO.id());
        docRepository.delete(doc);
        return new BaseReturnDTO("Success","");
    }

    private String makeMarkDown(String contents){

        Matcher matcher= Pattern.compile("[\\n]?\\[\\[([\\w가-힣]+[\\w가-힣 ]*)\\]\\]\\n").matcher(contents);
        while (matcher.find()){
            String tgt=matcher.group(1);
            if (contents.contains("\n[[" + tgt + "]]\n")){
                contents=contents.replace("\n[["+tgt+"]]\n",makeMDofInfo(tgt));
            }else{
                contents=contents.replace("[["+tgt+"]]\n",makeMDofInfo(tgt));
            }

        }
        return contents;
    }
    private String makeMDofInfo(String name){
        Info info= infoRepository.findByNameIgnoreCase(name);
        if(info==null){
            info=new Info();
            info.setId((long)-1);
            info.setName(name);
            info.setSimpleInfo("-");
        }
        return "<div className=\"m-2 rounded-0 border\" key="+info.getId()+"><div className=\"fs-4 fw-bold mb-1\">"+info.getName()+"</div><div className=\"text-pre\">"+info.getSimpleInfo()+"</div></div>";
    }
}
