package com.study.studydict.service;

import com.study.studydict.dto.BaseReturnDTO;
import com.study.studydict.dto.InfoDTO;
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

import java.util.HashMap;
import java.util.List;
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
        Page<Info> infoList= infoRepository.findAll(pageable);
        List<InfoDTO> infoListDTO= infoList.stream()
                .map(info->{
                    List<Tag> tagList= tagRepository.findByInfoId(info.getId());
                    return buildSearchdtoWithTagObj(info,tagList);
                })
                .collect(Collectors.toList());
        ret_data.put("infoList",infoListDTO);
        ret_data.put("totalPage",infoList.getTotalPages());
        return new BaseReturnDTO("Success","",ret_data);
    }
    @Transactional(readOnly = true)
    public BaseReturnDTO getByName(String name,Pageable pageable){ //이름 검색
        HashMap<String,Object> ret_data=new HashMap<>();
        Page<Info> infoList= infoRepository.findByNameContainingIgnoreCase(name,pageable);
        List<InfoDTO> infoListDTO= infoRepository.findByNameContainingIgnoreCase(name,pageable).stream()
                .map(info->{
                    List<Tag> tagList= tagRepository.findByInfoId(info.getId());
                    return buildSearchdtoWithTagObj(info,tagList);
                })
                .collect(Collectors.toList());
        ret_data.put("infoList",infoListDTO);
        ret_data.put("totalPage",infoList.getTotalPages());
        return new BaseReturnDTO("Success","",ret_data);
    }
    @Transactional(readOnly = true)
    public BaseReturnDTO getByTag(String tag,Pageable pageable){ //태그 검색
        HashMap<String,Object> ret_data=new HashMap<>();
        Page<Info> infoList= infoRepository.findByTag(tag, pageable);
        List<InfoDTO> infoListDTO= infoList.stream()
                .map(info->{
                    List<Tag> tagList= tagRepository.findByInfoId(info.getId());
                    return buildSearchdtoWithTagObj(info,tagList);
                })
                .collect(Collectors.toList());
        ret_data.put("infoList",infoList);
        ret_data.put("totalPage",infoList.getTotalPages());
        return new BaseReturnDTO("Success","",ret_data);
    }
    public Info getById(long id){
        return infoRepository.findById(id).orElse(null);
    }//상세페이지 만들때까지 안씀

    public BaseReturnDTO deleteInfo(InfoDTO infoDTO){ //글 삭제
        Info info=new Info();
        info.setId(infoDTO.id());
        List<InfoTagMap> infoTagMaps=infoTagMapRepository.findByInfo(info);
        infoTagMapRepository.deleteAll(infoTagMaps);
        infoRepository.deleteById(infoDTO.id());
        return new BaseReturnDTO("Success","");
    }
    public BaseReturnDTO saveInfo(InfoDTO infoDTO){ //글 추가수정
        HashMap<String, InfoDTO> ret_data=new HashMap<String, InfoDTO>();
        String message="New";
        Info info=new Info();
        info.setId(infoDTO.id());
        info.setName(infoDTO.name());
        info.setSimpleInfo(infoDTO.simpleInfo());
        info.setDetailInfo(infoDTO.detailInfo());

        if(infoRepository.findByNameIgnoreCase(info.getName())!=null){ //중복체크
            if(info.getId().equals(Long.valueOf(-1))) {
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
        ret_data.put("info",buildSearchdtoWithTagString(info, infoDTO.tag()));
        return new BaseReturnDTO("Success",message,ret_data);
    }

    private InfoDTO buildSearchdtoWithTagObj(Info info, List<Tag> tagList){
        return new InfoDTO(
                info.getId(),
                info.getName(),
                info.getSimpleInfo(),
                info.getDetailInfo(),
                tagList.stream().map(Tag::getTag).collect(Collectors.toList()),
                info.getRecentUpdate(),
                info.getCreatedDate()
        );
    }
    private InfoDTO buildSearchdtoWithTagString(Info info, List<String> tagList){
        return new InfoDTO(
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
