package com.vietdp.vietdp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vietdp.vietdp.dao.UserRepository;
import com.vietdp.vietdp.entity.Users;



@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Users user = repository.findByEmail(email).get();
        if (user == null) {
            throw new UsernameNotFoundException(user.getEmail());
        }
        UserDetails userDetails = new AuthUserDetails(user);
        new AccountStatusUserDetailsChecker().check(userDetails);
        return userDetails;
    }
}


