package com.mobsolution.jsf_app;

import com.mobsolution.jsf_app.dto.EventDTO;
import com.mobsolution.jsf_app.dto.EventoResponse;
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

    @PostConstruct
    public void init() {
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
                            headers.add("Cookie", "jwt=" + cookie.getValue());
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
                    return getRowCount();
                }

                String url = "http://localhost:8080/api/evento?page=0&size=1";

                RestTemplate restTemplate = new RestTemplate();
                HttpHeaders headers = new HttpHeaders();
                headers.setAccept(Collections.singletonList(MediaType.valueOf("application/hal+json")));

                FacesContext fc = FacesContext.getCurrentInstance();
                HttpServletRequest req = (HttpServletRequest) fc.getExternalContext().getRequest();
                if (req.getCookies() != null) {
                    for (Cookie cookie : req.getCookies()) {
                        System.out.println("Cookie encontrado: " + cookie.getName() + " = " + cookie.getValue());
                        if ("jwt".equals(cookie.getName())) {
                            headers.add("Cookie", "jwt=" + cookie.getValue());
                        }
                    }
                }
                

                HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
                ResponseEntity<EventoResponse> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, EventoResponse.class);
                EventoResponse response = responseEntity.getBody();

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
