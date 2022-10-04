package com.vietdp.vietdp.dao;

import com.vietdp.vietdp.entity.Tokens;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface TokenRepository extends JpaRepository<Tokens,Long> {
    @Transactional
    @Modifying
    public void deleteByUserId(Long userId);
}
