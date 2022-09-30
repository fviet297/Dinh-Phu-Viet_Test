package com.vietdp.vietdp.dao;

import com.vietdp.vietdp.entity.Tokens;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<Tokens,Long> {
}
