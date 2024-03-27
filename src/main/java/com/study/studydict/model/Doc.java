package com.study.studydict.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class  Doc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String title;
    @Column
    private String content;
    @Column
    private Boolean isWrite;
    @Column(name = "recent_update")
    @LastModifiedDate
    private LocalDateTime recentUpdate;
    @Column(name = "created_date",updatable = false)
    @CreatedDate
    private LocalDateTime createdDate;
}
