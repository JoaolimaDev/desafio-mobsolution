package com.mobsolution.spring_app.domain.dto;

import jakarta.validation.constraints.Pattern;

public record presencaDTO(

    @Pattern(regexp = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$", message = "Data deve estar no formato YYYY-MM-DD")
    String data,

    String participante

) {}
