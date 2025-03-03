package com.mobsolution.jsf_app;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.mobsolution.jsf_app.dto.EventDTO;
import com.mobsolution.jsf_app.dto.ParticipanteResponse;
import com.mobsolution.jsf_app.dto.participanteResquestDTO;

import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;


@Named
@ViewScoped
public class participanteBean implements Serializable {
    

    @Getter @Setter
    private LazyDataModel<ParticipanteDTO> eventModel;

    @Getter @Setter
    private List<ParticipanteDTO> filteredEvents; // Store filtered results
        
    @Getter @Setter
    private List<EventDTO> eventOptions;           // List of events retrieved from API
        
    @Getter @Setter
    private String selectedEvento;   

    @Getter @Setter
    private String globalFilter = "";  // Store global filter text


    @Getter @Setter
    private ParticipanteDTO selectedEvent;

    public void prepararFormulario(ParticipanteDTO participanteDTO) {

        this.selectedEvent = participanteDTO;

        if (selectedEvent.get_links().getEvento().getHref() != null) {


            String url = selectedEvent.get_links().getEvento().getHref();

            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.valueOf("application/hal+json")));
    
            // Retrieve JWT token from cookies
            FacesContext fc = FacesContext.getCurrentInstance();
            HttpServletRequest req = (HttpServletRequest) fc.getExternalContext().getRequest();
            if (req.getCookies() != null) {
                for (Cookie cookie : req.getCookies()) {
                    if ("jwt".equals(cookie.getName())) {
                        // Add the Authorization header with the Bearer token
                        headers.add("Authorization", "Bearer " + cookie.getValue());
                        break;
                    }
                }
            }
           
    
    
            HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
            ResponseEntity<EventDTO> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, EventDTO.class);
            EventDTO response = responseEntity.getBody();
    
           this.selectedEvento = response.get_links().getSelf().getHref();
    
            
        }

    }    


    public void novoEvento() {
        this.selectedEvent = new ParticipanteDTO();
        
    }



    public void salvar(){
  

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.valueOf("application/hal+json")));
        headers.setContentType(MediaType.APPLICATION_JSON);  // Set Content-Type to JSON
        String url = "";

        if (selectedEvent.get_links() != null) {
            // Extract event ID from the URL
           String href = selectedEvent.get_links().getParticipante().getHref();
           url = href;
        }

        participanteResquestDTO requestBody = new participanteResquestDTO(selectedEvent.getNome(), selectedEvent.getCpf(), selectedEvent.getEmail(), 
        selectedEvento);
    
        // Extract JWT from cookies and add to headers
        FacesContext fc = FacesContext.getCurrentInstance();
        HttpServletRequest req = (HttpServletRequest) fc.getExternalContext().getRequest();
        if (req.getCookies() != null) {
            for (Cookie cookie : req.getCookies()) {
                if ("jwt".equals(cookie.getName())) {
                    headers.add("Authorization", "Bearer " + cookie.getValue());  // Add JWT to Authorization header
                }
            }
        }
    
        // Create the HttpEntity with the request body and headers
        HttpEntity<participanteResquestDTO> requestEntity = new HttpEntity<>(requestBody, headers);



        if (selectedEvent.get_links() == null) {
            url = "http://localhost:8080/api/participante";  

        
            try {
                // Make the POST request
                ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
              
                // After POST, redirect to dashboard
                FacesContext.getCurrentInstance().getExternalContext().redirect("dashboard.xhtml");
            } catch (HttpClientErrorException e) {
                System.out.println("Erro na requisição: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
            } catch (Exception e) {
                System.out.println("Erro inesperado: " + e.getMessage());
            }
    
          
            return;
        }




        try {
   
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, String.class);
            

            System.out.println(response);
               FacesContext.getCurrentInstance().getExternalContext().redirect("dashboardParticipante.xhtml");

        } catch (HttpClientErrorException e) {
            System.out.println("Erro na requisição: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
        } catch (Exception e) {
            System.out.println("Erro inesperado: " + e.getMessage());
        }
      

    }



    @PostConstruct
    public void init() {
        selectedEvent = new ParticipanteDTO(); // Garante que não seja null
       
        
        
        eventModel = new LazyDataModel<ParticipanteDTO>() {
            
            @Override
            public List<ParticipanteDTO> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
                int page = first / pageSize;
                String url = "http://localhost:8080/api/participante?page=" + page + "&size=" + pageSize;

                RestTemplate restTemplate = new RestTemplate();
                HttpHeaders headers = new HttpHeaders();
                headers.setAccept(Collections.singletonList(MediaType.valueOf("application/hal+json")));

                // Retrieve JWT token from cookies
                FacesContext fc = FacesContext.getCurrentInstance();
                HttpServletRequest req = (HttpServletRequest) fc.getExternalContext().getRequest();
                if (req.getCookies() != null) {
                    for (Cookie cookie : req.getCookies()) {
                        if ("jwt".equals(cookie.getName())) {
                            // Add the Authorization header with the Bearer token
                            headers.add("Authorization", "Bearer " + cookie.getValue());
                            break;
                        }
                    }
                }
               


                HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
                ResponseEntity<ParticipanteResponse> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, ParticipanteResponse.class);
                ParticipanteResponse response = responseEntity.getBody();

                List<ParticipanteDTO> events = (response != null && response.getEmbedded() != null)
                        ? response.getEmbedded().getParticipante()
                        : Collections.emptyList();


                // Apply the global filter manually
                return applyGlobalFilter(events);


            }

            @Override
            public int count(Map<String, FilterMeta> filterBy) {
                if (getRowCount() > 0) {
                  
                    return getRowCount();
                }


                return getRowCount();
            }
        };
    }

      /**
     * Applies global filtering logic to the event list.
     */
    private List<ParticipanteDTO> applyGlobalFilter(List<ParticipanteDTO> participanteDTOs) {
        if (globalFilter == null || globalFilter.trim().isEmpty()) {
            return participanteDTOs;
        }

        String filterText = globalFilter.trim().toLowerCase();

        return participanteDTOs.stream()
                .filter(event ->
                        (event.getNome() != null && event.getNome().toLowerCase().contains(filterText)) ||
                        (event.getCpf() != null && event.getCpf().toLowerCase().contains(filterText)) ||
                        (event.getEmail() != null && event.getEmail().toLowerCase().contains(filterText)) ||
                        (event.get_links() != null && event.get_links().getSelf().getHref().toLowerCase().contains(filterText)) ||
                        (event.get_links() != null && event.get_links().getEvento().getHref().toLowerCase().contains(filterText))
                )
                .collect(Collectors.toList());
    }

}
    