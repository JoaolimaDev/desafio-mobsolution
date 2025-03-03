package com.mobsolution.jsf_app.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mobsolution.jsf_app.ParticipanteDTO;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ParticipanteResponse {
    
      // Map the _embedded section
    @JsonProperty("_embedded")
    private Embedded embedded;
    
    // Map the top-level _links
    @JsonProperty("_links")
    private GlobalLinks links;
    
    private Page page;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Embedded {
        // List of events
        private List<ParticipanteDTO> participante;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Page {
        private int size;
        private int totalElements;
        private int totalPages;
        private int number;
    }


}
