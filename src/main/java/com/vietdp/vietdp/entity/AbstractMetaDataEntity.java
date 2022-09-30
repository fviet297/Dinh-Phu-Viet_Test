package com.vietdp.vietdp.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Data;

@MappedSuperclass
@Data
@AllArgsConstructor
public abstract class AbstractMetaDataEntity implements Serializable {
    private static final long serialVersionUID = 6317994726473574608L;
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy=GenerationType.IDENTITY)

    private Long id;

    @Column(name = "createdAt", columnDefinition="DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    @ColumnDefault("CURRENT_TIMESTAMP")
    private Date createdAt;

    @Column(name = "updatedAt", columnDefinition="DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    @ColumnDefault("CURRENT_TIMESTAMP")
    private Date updatedAt;

    protected AbstractMetaDataEntity() {
    }
}

