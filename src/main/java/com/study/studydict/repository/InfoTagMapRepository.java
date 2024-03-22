package com.study.studydict.repository;

import com.study.studydict.model.Info;
import com.study.studydict.model.InfoTagMap;
import com.study.studydict.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InfoTagMapRepository extends JpaRepository<InfoTagMap,Long> {
    List<InfoTagMap> findByInfo(Info info);
//    List<InfoTagMap> findByTag(Long id);
}

