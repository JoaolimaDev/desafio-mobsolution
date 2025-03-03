package com.mobsolution.jsf_app;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ParticipanteDTO {
    private String nome;
    private String email;
    private String cpf;
    private String evento;


    private ParticipanteLinks _links;
}
