package com.study.studydict.service;

import com.study.studydict.dto.BaseReturnDTO;
import com.study.studydict.dto.InfoDTO;
import com.study.studydict.dto.QuizDTO;
import com.study.studydict.model.Info;
import com.study.studydict.model.InfoTagMap;
import com.study.studydict.model.Tag;
import com.study.studydict.repository.InfoRepository;
import com.study.studydict.repository.InfoTagMapRepository;
import com.study.studydict.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;



@Service
public class SearchService {
    private final InfoRepository infoRepository;
    private final InfoTagMapRepository infoTagMapRepository;
    private final TagRepository tagRepository;
    @Autowired
    public SearchService(InfoRepository infoRepository, InfoTagMapRepository infoTagMapRepository,TagRepository tagRepository){
        this.infoRepository=infoRepository;
        this.infoTagMapRepository=infoTagMapRepository;
        this.tagRepository=tagRepository;
    }
    @Transactional(readOnly = true)
    public BaseReturnDTO getAllInfo(Pageable pageable){ //그냥 전부 검색
        HashMap<String,Object> ret_data=new HashMap<>();
        Page<Info> infoList= infoRepository.findAllByOrderByRecentUpdateDesc(pageable);
        List<InfoDTO> infoListDTO= infoList.stream()
                .map(info->{
                    List<Tag> tagList= tagRepository.findByInfoId(info.getId());
                    return new InfoDTO(info,tagList.stream().map(Tag::getTag).collect(Collectors.toList()));
                })
                .collect(Collectors.toList());
        ret_data.put("infoList",infoListDTO);
        ret_data.put("totalPage",infoList.getTotalPages());
        return new BaseReturnDTO("Success","",ret_data);
    }
    @Transactional(readOnly = true)
    public BaseReturnDTO getByName(String name,Pageable pageable){ //이름 검색
        HashMap<String,Object> ret_data=new HashMap<>();
        Page<Info> infoList= infoRepository.findByNameContainingIgnoreCaseOrderByRecentUpdateDesc(name,pageable);
        List<InfoDTO> infoListDTO= infoList.stream()
                .map(info->{
                    List<Tag> tagList= tagRepository.findByInfoId(info.getId());
                    return new InfoDTO(info,tagList.stream().map(Tag::getTag).collect(Collectors.toList()));
                })
                .collect(Collectors.toList());
        ret_data.put("infoList",infoListDTO);
        ret_data.put("totalPage",infoList.getTotalPages());
        return new BaseReturnDTO("Success","",ret_data);
    }
    @Transactional(readOnly = true)
    public BaseReturnDTO getByTag(String tag,Pageable pageable){ //태그 검색
        HashMap<String,Object> ret_data=new HashMap<>();
        Page<Info> infoList= infoRepository.findByTagOrderByRecentUpdateDesc(tag, pageable);
        List<InfoDTO> infoListDTO= infoList.stream()
                .map(info->{
                    List<Tag> tagList= tagRepository.findByInfoId(info.getId());
                    return new InfoDTO(info,tagList.stream().map(Tag::getTag).collect(Collectors.toList()));
                })
                .collect(Collectors.toList());
        ret_data.put("infoList",infoListDTO);
        ret_data.put("totalPage",infoList.getTotalPages());
        return new BaseReturnDTO("Success","",ret_data);
    }

    public BaseReturnDTO deleteInfo(InfoDTO infoDTO){ //글 삭제
        Info info=new Info();
        info.setId(infoDTO.id());
        List<InfoTagMap> infoTagMaps=infoTagMapRepository.findByInfo(info);
        infoTagMapRepository.deleteAll(infoTagMaps);
        infoRepository.deleteById(infoDTO.id());
        return new BaseReturnDTO("Success","");
    }
    public BaseReturnDTO saveInfo(InfoDTO infoDTO){ //글 추가수정
        HashMap<String, Object> ret_data=new HashMap<>();
        String message="New";
        Info info=new Info();
        info.setId(infoDTO.id());
        info.setName(infoDTO.name());
        info.setSimpleInfo(infoDTO.simpleInfo());
        info.setDetailInfo(infoDTO.detailInfo());

        if(infoRepository.findByNameIgnoreCase(info.getName())!=null){ //중복체크
            if(info.getId().equals((long) -1)) {
                return new BaseReturnDTO("Fail", "Data Already Exist");
            }else{
                message="Update";
            }
        }

        info=infoRepository.save(info);
        List<InfoTagMap> infoTagMaps=infoTagMapRepository.findByInfo(info);
        infoTagMapRepository.deleteAll(infoTagMaps);
        for(String tag: infoDTO.tag()){
            Tag existTag=tagRepository.findByTag(tag);
            if(existTag==null){
                Tag newTag= new Tag();
                newTag.setTag(tag);
                existTag=tagRepository.save(newTag);
            }
            InfoTagMap infoTagMap=new InfoTagMap();
            infoTagMap.setInfo(info);
            infoTagMap.setTag(existTag);
            infoTagMapRepository.save(infoTagMap);
        }
        ret_data.put("info",new InfoDTO(info, infoDTO.tag()));
        return new BaseReturnDTO("Success",message,ret_data);
    }
    public BaseReturnDTO randQuiz(){
        HashMap<String, Object> ret_data=new HashMap<>();
        ret_data.put("QuizList",
                infoRepository.findRandomRecords(3).stream()
                        .map(QuizDTO::new).collect(Collectors.toList()));
        return new BaseReturnDTO("Success","",ret_data);
    }


}
