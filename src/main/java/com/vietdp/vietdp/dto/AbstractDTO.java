package com.vietdp.vietdp.dto;

import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import org.springframework.data.annotation.CreatedDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AbstractDTO {
	@GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
	
    @CreatedDate
    private Date createdAt;
    
    @CreatedDate
    private Date updatedAt;
}
