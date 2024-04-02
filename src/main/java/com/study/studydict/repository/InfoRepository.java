package com.study.studydict.repository;

import com.study.studydict.model.Info;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface InfoRepository extends JpaRepository<Info,Long> {
    Info findByNameIgnoreCase(String name);
    Page<Info> findAllByOrderByRecentUpdateDesc(Pageable pageable);
    Page<Info> findByNameContainingIgnoreCaseOrSimpleInfoContainingIgnoreCaseOrderByRecentUpdateDesc(String name,String simpleInfo, Pageable pageable);
    @Query("SELECT info FROM InfoTagMap WHERE tag.tag =:tag")
    Page<Info> findByTagOrderByRecentUpdateDesc(String tag,Pageable pageable);
    @Query(value = "SELECT id,name,simple_info,detail_info,created_date,recent_update FROM Info ORDER BY RANDOM() LIMIT :limit", nativeQuery = true)
    List<Info> findRandomRecords(@Param("limit") int limit);
}
