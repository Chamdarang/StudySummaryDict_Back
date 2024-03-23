package com.study.studydict.repository;

import com.study.studydict.model.Info;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface InfoRepository extends JpaRepository<Info,Long> {
    Info findByNameIgnoreCase(String name);
    Page<Info> findByNameContainingIgnoreCase(String name, Pageable pageable);
    @Query("SELECT info FROM InfoTagMap WHERE tag.tag =:tag")
    Page<Info> findByTag(String tag,Pageable pageable);
}
