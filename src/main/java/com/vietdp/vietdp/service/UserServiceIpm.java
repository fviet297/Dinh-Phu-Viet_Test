package com.vietdp.vietdp.service;

import com.vietdp.vietdp.dao.UserRepository;
import com.vietdp.vietdp.dto.LoginRequest;
import com.vietdp.vietdp.dto.UsersDTO;
import com.vietdp.vietdp.entity.Users;
import javassist.NotFoundException;
import org.jetbrains.annotations.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityExistsException;
import javax.persistence.NonUniqueResultException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceIpm implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    PasswordEncoder bCryptPasswordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    TokenProvider tokenProvider;

    @Autowired
    public JavaMailSender emailSender;


    @Override
    public UsersDTO signUp(LoginRequest loginRequest) throws MessagingException {
        List<Users> usersList = userRepository.findAll();
        usersList.forEach(i -> {
                    if (loginRequest.getEmail().equals(i.getEmail())) {
                        throw new EntityExistsException("Email exits!");
                    }
                }
        );
        Users users = new Users();
        users.setEmail(loginRequest.getEmail());
        users.setFirstName(loginRequest.getFirstName());
        users.setLastName(loginRequest.getLastName());
        users.setPassword(bCryptPasswordEncoder.encode(loginRequest.getPassword()));
        userRepository.save(users);

        UsersDTO userDTO = new UsersDTO();
        modelMapper.map(users, userDTO);

        MimeMessage message = emailSender.createMimeMessage();
        boolean multipart = true;

        MimeMessageHelper helper = new MimeMessageHelper(message, multipart, "utf-8");
        String htmlMsg = "<a>Thank you for registering !</a>" + "<br>";
//				+ "<a href='http://localhost:8080/vietshop/trang-chu'>Go to vShop</a>";
        message.setContent(htmlMsg, "text/html");
        helper.setFrom("fviet295@gmail.com");
        helper.setTo(loginRequest.getEmail());
        helper.setSubject("Thank you for registering !");
        this.emailSender.send(message);
        return userDTO;

    }

    @Override
    public Map<String, Object> login(@NotNull LoginRequest loginRequest) throws NotFoundException {
        Map<String, Object> rs = new HashMap<>();
//        Users user = userRepository.findByEmail(loginRequest.getEmail()).get();
//        if (user == null) {
//            throw new UsernameNotFoundException(loginRequest.getEmail());
//        }
        Authentication authentication = null;
        authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = tokenProvider.generateToken((AuthUserDetails) authentication.getPrincipal());
        String refreshToken = tokenProvider.generateRefreshToken((AuthUserDetails) authentication.getPrincipal());
        String email = tokenProvider.getEmailFromJWT(token);

        Users userDetails = userRepository.findByEmail(email).get();
        UsersDTO usersDTO = new UsersDTO();

        modelMapper.map(userDetails, usersDTO);

        rs.put("token", token);
        rs.put("refreshToken", refreshToken);
        rs.put("user", usersDTO);

        return rs;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("header" + request.getUserPrincipal());
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
//			tokenProvider.removeToken(auth.getDetails().);
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
    }

    @Override
    public UserDetails loadUserByEmail(String email) throws NotFoundException {
        // Kiểm tra xem user có tồn tại trong database không?
        Users user = userRepository.findByEmail(email).get();
        if (user == null) {
            throw new NotFoundException(email);
        }
        return new AuthUserDetails(user);
    }

    @Override
    public List<Users> list() {
        return userRepository.findAll();
    }

    @Override
    public Map<String, String> refreshToken(HttpServletRequest request, HttpServletResponse response) throws Throwable {
        return jwtAuthenticationFilter.refreshToken(request, response);
    }
}
