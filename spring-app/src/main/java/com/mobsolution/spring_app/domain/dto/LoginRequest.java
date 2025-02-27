package com.mobsolution.spring_app.domain.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "Por favor preencha o nome do usuário!") String username,
        @NotBlank(message = "Por favor preencha a senha!") String password) {
}