package com.mobsolution.jsf_app;


import jakarta.inject.Named;
import lombok.Data;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;

import java.io.IOException;
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
                // Login successful: redirect to dashboard.xhtml
                FacesContext.getCurrentInstance().getExternalContext().redirect("dashboard.xhtml");
            } 

        } catch (IOException e) {
            e.printStackTrace();
            PrimeFaces.current().executeScript("alert('Erro ao efetuar login: " + e.getMessage() + "');");
        } catch (Exception ex) {
            ex.printStackTrace();
            PrimeFaces.current().executeScript("alert('Erro inesperado: " + ex.getMessage() + "');");
        }
        
    }
}
