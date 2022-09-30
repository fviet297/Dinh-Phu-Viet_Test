package com.vietdp.vietdp.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest extends AbstractDTO{
	
    private String firstName;
    
    private String lastName;
    
    
    @NotNull
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")
    private String email;

    @NotBlank(message = "Mật khẩu bắt buộc nhập")    
    private String password;

}

