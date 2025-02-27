package com.mobsolution.spring_app.service;

import com.mobsolution.spring_app.domain.dto.presencaDTO;
import com.mobsolution.spring_app.domain.model.presenca;

public interface presencaService {
    
    public presenca postPresenca(presencaDTO  presencaBody);
    public presenca putPresenca(String id, presencaDTO presencaBody);
}
