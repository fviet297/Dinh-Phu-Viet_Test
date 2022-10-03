package com.vietdp.vietdp.service;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javassist.NotFoundException;

import javax.security.sasl.AuthenticationException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private UserService userService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            // Lấy jwt từ request
            log.info("url {} {}", request.getRequestURL().toString(), request.getHeader("Authorization"));
            String jwt = getJwtFromRequest(request);
            log.info("Token: {}",jwt);
//            if(!StringUtils.hasText(jwt)&&!request.getRequestURL().toString().contains("sign-in")&&!request.getRequestURL().toString().contains("sign-up")&&!request.getRequestURL().toString().contains("refresh-token")){
//                throw new AuthenticationException("Can not access");
//            }
            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
                String email = tokenProvider.getEmailFromJWT(jwt);
                log.debug("UserID: {}",email);
                UserDetails userDetails = userService.loadUserByEmail(email);
                log.info(userDetails.getUsername());
                if (userDetails != null) {
                    UsernamePasswordAuthenticationToken
                            authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (ExpiredJwtException | NotFoundException ex) {
            log.error("failed on set user authentication", ex);

            String isRefreshToken = request.getHeader("isRefreshToken");
            String requestURL = request.getRequestURL().toString();
            if (isRefreshToken != null && isRefreshToken.equals("true") && requestURL.contains("refresh-token")) {
                allowForRefreshToken((ExpiredJwtException) ex, request);
            } else
                request.setAttribute("exception", ex);
        }
        	log.info("filter");
        filterChain.doFilter(request, response);
    }
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
    public Map<String,String> refreshToken(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	Map<String,String> rs = new HashMap<>();
    	String email = tokenProvider.getEmailFromJWT(getJwtFromRequest(request));
    	UserDetails userDetails = userService.loadUserByEmail(email);

        log.info(userDetails.getUsername());
        if (userDetails != null) {
            UsernamePasswordAuthenticationToken
                    authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt  = tokenProvider.generateToken((AuthUserDetails) authentication.getPrincipal());
    		String newRefreshToken = tokenProvider.generateRefreshToken((AuthUserDetails) authentication.getPrincipal());
    		rs.put("token",jwt);
    		rs.put("refreshToken", newRefreshToken);
            return rs;
        }
        return null;
       
    }

    private void allowForRefreshToken(ExpiredJwtException ex, HttpServletRequest request) {

        // create a UsernamePasswordAuthenticationToken with null values.
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                null, null, null);
        // After setting the Authentication in the context, we specify
        // that the current user is authenticated. So it passes the
        // Spring Security Configurations successfully.
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        // Set the claims so that in controller we will be using it to create
        // new JWT
        request.setAttribute("claims", ex.getClaims());

    }
}
