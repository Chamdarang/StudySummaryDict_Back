package com.study.studydict.repository;

import com.study.studydict.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag,Long> {
    Tag findByTag(String tag);

    @Query("SELECT tag FROM InfoTagMap WHERE info.id =:infoId")
    List<Tag> findByInfoId(Long infoId);
}
