package com.vietdp.vietdp.service;


import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.vietdp.vietdp.entity.Users;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthUserDetails implements UserDetails {
    /**
     *
     */
    private static final long serialVersionUID = -6194375138955855309L;
    Users user;

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
	}

    @Override
    public String getPassword() {
        return user.getPassword();
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}


