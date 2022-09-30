package com.vietdp.vietdp.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

	protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();

	public CustomAuthenticationProvider() {
		super();
	}

	// API

	@Override
	public Authentication authenticate(final Authentication authentication) throws AuthenticationException {
		final String name = authentication.getName();
		final String password = authentication.getCredentials().toString();
		final List<GrantedAuthority> grantedAuths = new ArrayList<>();
		grantedAuths.add(new SimpleGrantedAuthority("ROLE_USER"));
		final UserDetails principal = new User(name, password, grantedAuths);
		if (!principal.isEnabled()) {
			throw new DisabledException(messages.getMessage("AbstractUserDetailsAuthenticationProvider.disabled", "User is disabled"));
		}
		return new UsernamePasswordAuthenticationToken(principal, password, grantedAuths);
	}

	@Override
	public boolean supports(final Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

}
