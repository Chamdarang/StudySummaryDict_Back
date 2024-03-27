package com.study.studydict.repository;

import com.study.studydict.model.Doc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocRepository extends JpaRepository<Doc,Long> {
    Doc findByTitleIgnoreCase(String title);
    Page<Doc> findAllByOrderByRecentUpdateDesc(Pageable pageable);
    Page<Doc> findAllByTitleContainingIgnoreCaseOrderByRecentUpdateDesc(String title,Pageable pageable);
}
