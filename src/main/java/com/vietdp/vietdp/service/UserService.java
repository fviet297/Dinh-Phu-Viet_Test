package com.vietdp.vietdp.service;

import com.vietdp.vietdp.dto.LoginRequest;
import com.vietdp.vietdp.dto.UsersDTO;
import com.vietdp.vietdp.entity.Users;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javassist.NotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface UserService {
    UsersDTO signUp(LoginRequest loginRequest) throws MessagingException;
    Map<String,Object> login(LoginRequest loginRequest) throws NotFoundException;

    void logout(HttpServletRequest request, HttpServletResponse response);

    UserDetails loadUserByEmail(String email) throws NotFoundException;

    List<Users> list();
	Map<String, String> refreshToken(HttpServletRequest request, HttpServletResponse response) throws Throwable;

}
