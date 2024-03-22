package com.study.studydict.model;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value={"updatedDate"}, allowGetters=true)
public class Info {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String name;
    @Column(name = "simple_info", columnDefinition = "TEXT")
    private String simpleInfo;
    @Column(name = "detail_info", columnDefinition = "TEXT")
    private String detailInfo;
    @Column(name = "recent_update")
    @LastModifiedDate
    private LocalDateTime recentUpdate;
    @Column(name = "created_date",updatable = false)
    @CreatedDate
    private LocalDateTime createdDate;

}