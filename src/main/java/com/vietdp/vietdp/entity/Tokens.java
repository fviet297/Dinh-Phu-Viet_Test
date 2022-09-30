package com.vietdp.vietdp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "tokens")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Tokens extends AbstractMetaDataEntity{

    @Column
    private Long userId;

    @Column
    private String refreshToken;

    @Column
    private String expiresIn;


}
