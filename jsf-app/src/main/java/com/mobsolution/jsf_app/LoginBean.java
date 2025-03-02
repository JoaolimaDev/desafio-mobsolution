package com.mobsolution.jsf_app;


import jakarta.inject.Named;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;

import java.io.Serializable;
import java.util.Collections;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.mobsolution.jsf_app.dto.LoginRequest;
import com.mobsolution.jsf_app.dto.LoginResponse;


import org.primefaces.PrimeFaces;

@Named
@ViewScoped
@Data
public class LoginBean implements Serializable {

    private String username;
    private String password;

    public void login() {
       
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(username);
        loginRequest.setPassword(password);

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.valueOf("application/hal+json")));
        HttpEntity<LoginRequest> requestEntity = new HttpEntity<>(loginRequest, headers);

        try {
            // Post the login request to the API endpoint
            ResponseEntity<LoginResponse> responseEntity = restTemplate.postForEntity(
                    "http://localhost:8080/api/auth/login", requestEntity, LoginResponse.class);
            LoginResponse response = responseEntity.getBody();

            if (response != null && "OK".equals(response.getStatus())) {
              String jwtToken = response.getMensagem();

                // Criar um cookie com o JWT
                Cookie jwtCookie = new Cookie("jwt", jwtToken);
                jwtCookie.setHttpOnly(false);  // Garante que o cookie seja acessível apenas pelo servidor
                jwtCookie.setSecure(true);    // Habilite se estiver usando HTTPS
                jwtCookie.setPath("/");       // Disponível para todo o site
                jwtCookie.setMaxAge(3600);    // Expira em 1 hora

                // Adicionar o cookie na resposta HTTP
                FacesContext facesContext = FacesContext.getCurrentInstance();
                HttpServletResponse httpResponse = (HttpServletResponse) facesContext.getExternalContext().getResponse();
                httpResponse.addCookie(jwtCookie);

                // Redireciona para o dashboard
                FacesContext.getCurrentInstance().getExternalContext().redirect("dashboard.xhtml");
               
            } 

        } catch (Exception ex) {
            ex.printStackTrace();
            PrimeFaces.current().executeScript("alert('Erro inesperado: " + ex.getMessage() + "');");
        }
        
    }
}
