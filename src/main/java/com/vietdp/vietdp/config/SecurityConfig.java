package com.vietdp.vietdp.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.vietdp.vietdp.service.CustomAccessDeniedHandler;
import com.vietdp.vietdp.service.CustomUserDetailsService;
import com.vietdp.vietdp.service.JwtAuthenticationFilter;

/**
 * @author Dinh Phu Viet
 * @version 0.1
 * @datetime 30/9/2022 9:06 AM
 */

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private JwtAuthenticationFilter jwtAuthenticationFilter;
	@Autowired
	private CustomUserDetailsService userDetailsService;

//	@Bean
//	public JwtAuthenticationFilter jwtAuthenticationFilter() {
//		return new JwtAuthenticationFilter();
//	}

	@Bean(BeanIds.AUTHENTICATION_MANAGER)
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
	public PasswordEncoder passwordEncoder() { 
		return new BCryptPasswordEncoder();
	}
	@Bean
    public CustomAccessDeniedHandler customAccessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService) 
				.passwordEncoder(passwordEncoder());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().cors().configurationSource(request -> {
            var cors = new CorsConfiguration();
            cors.setAllowedOrigins(List.of("*"));
            cors.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
            cors.setAllowedHeaders(List.of("*"));
            return cors;
        }).and()
		.authorizeRequests()
				.antMatchers(HttpMethod.POST,"/sign-in").permitAll() // accept_all
                .anyRequest().authenticated();
		http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

	}

//	@Override
//	public void configure(WebSecurity web) throws Exception {
//		System.out.println("Start WebSecurity...!!!");
//		web.ignoring().antMatchers(HttpMethod.POST, "*");
//		web.ignoring().antMatchers("/v3/api-docs", "/configuration/ui", "/oauth/login", "/swagger-resources/**",
//				"/configuration/security", "/swagger-ui.html", "/webjars/**");
//	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**").allowedOrigins("*").allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
						.allowedHeaders("*");
			}
		};
	}
}
