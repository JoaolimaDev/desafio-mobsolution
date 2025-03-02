package com.mobsolution.jsf_app.dto;


import lombok.Data;
import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EventDTO {
    private String nome;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    
    // Map the _links field for each event
    private EventLinks _links;
}
