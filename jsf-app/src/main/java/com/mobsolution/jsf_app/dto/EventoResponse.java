package com.mobsolution.jsf_app.dto;


import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EventoResponse {

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
        private List<EventDTO> evento;
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
