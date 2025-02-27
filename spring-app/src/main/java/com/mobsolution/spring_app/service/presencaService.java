package com.mobsolution.spring_app.service;

import java.util.List;

import com.mobsolution.spring_app.domain.dto.presencaDTO;
import com.mobsolution.spring_app.domain.model.Presenca;

public interface presencaService {
    
    public Presenca postPresenca(presencaDTO  presencaBody);
    public Presenca putPresenca(String id, presencaDTO presencaBody);
}
