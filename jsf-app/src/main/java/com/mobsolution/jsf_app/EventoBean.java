package com.mobsolution.jsf_app;

import com.mobsolution.jsf_app.dto.EventDTO;
import com.mobsolution.jsf_app.dto.EventoResponse;
import com.mobsolution.jsf_app.dto.ResquestDTO;

import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;
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
import lombok.Getter;
import lombok.Setter;
import java.util.stream.Collectors;


@Named
@ViewScoped
public class EventoBean implements Serializable {

    @Getter @Setter
    private LazyDataModel<EventDTO> eventModel;
    

    @Getter @Setter
    private String globalFilter = "";  // Store global filter text

    @Getter @Setter
    private List<EventDTO> filteredEvents; // Store filtered results


    @Getter @Setter
    private EventDTO selectedEvent;
    



    public void prepararFormulario(EventDTO eventDTO) {

        this.selectedEvent = eventDTO;
        
        if (selectedEvent != null) {
            System.out.println("Evento selecionado: " + selectedEvent.getNome());
        } else {
            System.out.println("Erro: selectedEvent está null");
        }
      
    }


    public void novoEvento() {
        this.selectedEvent = new EventDTO();
    }

    
    public void salvar(){
  
    
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.valueOf("application/hal+json")));
        headers.setContentType(MediaType.APPLICATION_JSON);  // Set Content-Type to JSON
        String url = "";
    
        if (selectedEvent.get_links() != null) {
             // Extract event ID from the URL
            String href = selectedEvent.get_links().getEvento().getHref();
            String eventId = href.substring(href.lastIndexOf("/evento/") + 8);
            url = "http://localhost:8080/api/evento/" + eventId;
        }

    
        // Create the request body
        ResquestDTO requestBody = new ResquestDTO(selectedEvent.getNome(), selectedEvent.getDataInicio().toString(), selectedEvent.getDataFim().toString());
    
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
        HttpEntity<ResquestDTO> requestEntity = new HttpEntity<>(requestBody, headers);


        if (selectedEvent.get_links() == null) {
            url = "http://localhost:8080/api/evento";  

            System.out.println("aquiiiiiiiiiiii");
        
            try {
                // Make the POST request
                ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
                System.out.println("Response: " + response.getStatusCode() + " - " + response.getBody());
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
            // Make the PUT request
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, String.class);
            System.out.println("Response: " + response.getStatusCode() + " - " + response.getBody());
               // Redireciona para o dashboard
               FacesContext.getCurrentInstance().getExternalContext().redirect("dashboard.xhtml");

        } catch (HttpClientErrorException e) {
            System.out.println("Erro na requisição: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
        } catch (Exception e) {
            System.out.println("Erro inesperado: " + e.getMessage());
        }
        System.out.println(url);
        
    }



    @PostConstruct
    public void init() {
        selectedEvent = new EventDTO(); // Garante que não seja null
       
        
        eventModel = new LazyDataModel<EventDTO>() {
            @Override
            public List<EventDTO> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
                int page = first / pageSize;
                String url = "http://localhost:8080/api/evento?page=" + page + "&size=" + pageSize;

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
                ResponseEntity<EventoResponse> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, EventoResponse.class);
                EventoResponse response = responseEntity.getBody();

                List<EventDTO> events = (response != null && response.getEmbedded() != null)
                        ? response.getEmbedded().getEvento()
                        : Collections.emptyList();


                // Apply the global filter manually
                return applyGlobalFilter(events);
            }

            @Override
            public int count(Map<String, FilterMeta> filterBy) {
                if (getRowCount() > 0) {
                    System.out.println("response");
                    return getRowCount();
                }

                String url = "http://localhost:8080/api/evento?page=0&size=20";

                RestTemplate restTemplate = new RestTemplate();
                HttpHeaders headers = new HttpHeaders();
                headers.setAccept(Collections.singletonList(MediaType.valueOf("application/hal+json")));

                FacesContext fc = FacesContext.getCurrentInstance();
                HttpServletRequest req = (HttpServletRequest) fc.getExternalContext().getRequest();
                if (req.getCookies() != null) {
                    for (Cookie cookie : req.getCookies()) {
                        
                        if ("jwt".equals(cookie.getName())) {
                            headers.add("Authorization", "Bearer " + cookie.getValue());

                        }
                    }
                }
                

                HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
                ResponseEntity<EventoResponse> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, EventoResponse.class);
                EventoResponse response = responseEntity.getBody();

                System.out.println("respose" + response);
                if (response != null && response.getPage() != null) {
                    setRowCount(response.getPage().getTotalElements());
                }
                return getRowCount();
            }
        };
    }




    /**
     * Applies global filtering logic to the event list.
     */
    private List<EventDTO> applyGlobalFilter(List<EventDTO> events) {
        if (globalFilter == null || globalFilter.trim().isEmpty()) {
            return events;
        }

        String filterText = globalFilter.trim().toLowerCase();

        return events.stream()
                .filter(event ->
                        (event.getNome() != null && event.getNome().toLowerCase().contains(filterText)) ||
                        (event.getDataInicio() != null && event.getDataInicio().toString().contains(filterText)) ||
                        (event.getDataFim() != null && event.getDataFim().toString().contains(filterText)) ||
                        (event.get_links() != null && event.get_links().getSelf().getHref().toLowerCase().contains(filterText)) ||
                        (event.get_links() != null && event.get_links().getEvento().getHref().toLowerCase().contains(filterText))
                )
                .collect(Collectors.toList());
    }
}
