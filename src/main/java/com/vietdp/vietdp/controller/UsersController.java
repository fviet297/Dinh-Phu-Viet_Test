package com.vietdp.vietdp.controller;

import java.util.Map;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.vietdp.vietdp.dto.LoginRequest;
import com.vietdp.vietdp.dto.UsersDTO;
import com.vietdp.vietdp.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(UsersController.USER)
public class UsersController {
	static final String USER = "/";
	@Autowired
	UserService userService;

	@PostMapping("/sign-up")
	public ResponseEntity<UsersDTO> signUp(@RequestBody @Valid LoginRequest loginRequest) throws MessagingException {

		UsersDTO userDTO = userService.signUp(loginRequest);
		return new ResponseEntity(userDTO, null, HttpStatus.CREATED);
	}

	@PostMapping("/sign-in")
	public Map<String,Object> login(@RequestBody @Valid LoginRequest loginRequest) throws Exception {

		return userService.login(loginRequest);
	}

	@PostMapping("/sign-out")
	public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response){
		userService.logout(request,response);
		return new ResponseEntity<>("Logout Success !",null,HttpStatus.ACCEPTED);
	}
	@PostMapping("/refresh-token")
	public Map<String,String> refreshToken(HttpServletRequest request, HttpServletResponse response) throws Throwable {
		return userService.refreshToken(request,response);
	}
	
	@ExceptionHandler(BindException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)  
	public String handleBindException(BindException e) {
	    String errorMessage = "Request không hợp lệ";
	    if (e.getBindingResult().hasErrors())
	        e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
	    return errorMessage;
	}
}
