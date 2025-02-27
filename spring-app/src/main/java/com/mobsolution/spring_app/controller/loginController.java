package com.mobsolution.spring_app.controller;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mobsolution.spring_app.config.security.utils.JwtUtilities;
import com.mobsolution.spring_app.domain.dto.LoginRequest;
import com.mobsolution.spring_app.domain.dto.Response;
import com.mobsolution.spring_app.domain.model.User;
import com.mobsolution.spring_app.domain.repository.UserRepository;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name="Autênticação")
public class loginController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final long expirationTime = 3600000L;
    private final JwtUtilities jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<Response> login(@RequestBody @Valid 
    LoginRequest loginRequest, HttpServletResponse responseCookie){
        
        Optional<User> userOptional = userRepository.findByUsername(loginRequest.username());

        if (userOptional.isPresent()) {

            User user = userOptional.get();

            if (passwordEncoder.matches(loginRequest.password(), user.getPassword())) {

                String token = jwtUtil.generateToken(new org.springframework.security.core.userdetails.User(
                    user.getUsername(),
                    user.getPassword(),
                    AuthorityUtils.createAuthorityList(user.getRole())
                ));


                Cookie jwtCookie = new Cookie("jwt", token);
                jwtCookie.setHttpOnly(true);  
                jwtCookie.setSecure(true);   
                jwtCookie.setPath("/");
                jwtCookie.setMaxAge((int) (expirationTime / 1000));  
    
                responseCookie.addCookie(jwtCookie);
            

                Response response = new Response("Login realizado com sucesso!", HttpStatus.OK.name());

                return new ResponseEntity<>(response, HttpStatus.OK);                
            }
            
        }

        Response response = new Response("Usuário ou senha inválidos!", HttpStatus.BAD_REQUEST.name());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);                
    }

    @GetMapping("/logout")
    public ResponseEntity<Response> logout(HttpServletResponse response) {
        
        Cookie jwtCookie = new Cookie("jwt", null);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setSecure(true); 
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(0);

        response.addCookie(jwtCookie);

        Response responseBody = new Response("Logout realizado com sucesso!", HttpStatus.OK.name());


        return new ResponseEntity<>(responseBody, HttpStatus.NO_CONTENT);                
    }
    
}
