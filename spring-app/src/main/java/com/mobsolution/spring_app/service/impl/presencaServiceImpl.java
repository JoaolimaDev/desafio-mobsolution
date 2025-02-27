package com.mobsolution.spring_app.service.impl;

import java.time.LocalDate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.mobsolution.spring_app.config.exceptionHandler.CustomException;
import com.mobsolution.spring_app.domain.dto.presencaDTO;
import com.mobsolution.spring_app.domain.model.Evento;
import com.mobsolution.spring_app.domain.model.Participante;
import com.mobsolution.spring_app.domain.model.Presenca;
import com.mobsolution.spring_app.domain.repository.eventoRepository;
import com.mobsolution.spring_app.domain.repository.participanteRepository;
import com.mobsolution.spring_app.domain.repository.presencaRepository;
import com.mobsolution.spring_app.service.presencaService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class presencaServiceImpl implements presencaService {

    public final presencaRepository presencaRepository;
    public final participanteRepository participanteRepository;
    public final eventoRepository eventoRepository;


    @Override
    public Presenca postPresenca(presencaDTO presencaBody) {    


        Participante participante_id = participanteRepository.findById(Long.valueOf(presencaBody.participante()))
        .orElseThrow(() -> new CustomException("Participante: " + presencaBody.participante()+ " inválido!", HttpStatus.BAD_REQUEST));

        Evento eventoFound = eventoRepository.findById(participante_id.getEvento().getId())
        .orElseThrow(() -> new CustomException("Nenhum evento encontrado para o participante: " 
        + presencaBody.participante() + " inválido!", HttpStatus.BAD_REQUEST));

        LocalDate data = LocalDate.parse(presencaBody.data());

        if (data.isAfter(eventoFound.getDataFim()) || data.isBefore(eventoFound.getDataInicio())) {
            

            throw new CustomException("Data enviada fora do período do evento!", HttpStatus.BAD_REQUEST);
        }

        Presenca presenca = new Presenca();
        presenca.setData(data);
        presenca.setParticipante(participante_id);


        return presencaRepository.save(presenca);
    }


    @Override
    public Presenca putPresenca(String id, presencaDTO presencaBody) {

        Presenca presenca = presencaRepository.findById(Long.valueOf(id))
        .orElseThrow(() -> new CustomException(id + " inválido!", HttpStatus.BAD_REQUEST));

        Evento eventoFound = eventoRepository.findById(presenca.getParticipante().getId())
        .orElseThrow(() -> new CustomException("Nenhum evento encontrado para o participante: " 
        + presenca.getParticipante().getId() + " inválido!", HttpStatus.BAD_REQUEST));

        LocalDate data = LocalDate.parse(presencaBody.data());

        if (data.isAfter(eventoFound.getDataFim()) || data.isBefore(eventoFound.getDataInicio())) {
            

            throw new CustomException("Data enviada fora do período do evento!", HttpStatus.BAD_REQUEST);
        }


        presenca.setData(data);

        return presencaRepository.save(presenca);

    }
    
}
