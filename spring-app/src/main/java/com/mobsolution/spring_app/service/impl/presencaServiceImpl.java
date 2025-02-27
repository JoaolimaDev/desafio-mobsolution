package com.mobsolution.spring_app.service.impl;

import java.time.LocalDate;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.mobsolution.spring_app.config.exceptionHandler.CustomException;
import com.mobsolution.spring_app.domain.dto.presencaDTO;
import com.mobsolution.spring_app.domain.model.evento;
import com.mobsolution.spring_app.domain.model.participante;
import com.mobsolution.spring_app.domain.model.presenca;
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
    public presenca postPresenca(presencaDTO presencaBody) {    


        participante participante_id = participanteRepository.findById(Long.valueOf(presencaBody.participante()))
        .orElseThrow(() -> new CustomException("Participante: " + presencaBody.participante()+ " inválido!", HttpStatus.BAD_REQUEST));

        evento eventoFound = eventoRepository.findById(participante_id.getEvento().getId())
        .orElseThrow(() -> new CustomException("Nenhum evento encontrado para o participante: " 
        + presencaBody.participante() + " inválido!", HttpStatus.BAD_REQUEST));

        LocalDate data = LocalDate.parse(presencaBody.data());

        if (data.isAfter(eventoFound.getDataFim()) || data.isBefore(eventoFound.getDataInicio())) {
            

            throw new CustomException("Data enviada fora do período do evento!", HttpStatus.BAD_REQUEST);
        }

        presenca presenca = new presenca();
        presenca.setData(data);
        presenca.setParticipante(participante_id);


        return presencaRepository.save(presenca);
    }


    @Override
    public presenca putPresenca(String id, presencaDTO presencaBody) {

        presenca presenca = presencaRepository.findById(Long.valueOf(id))
        .orElseThrow(() -> new CustomException(id + " inválido!", HttpStatus.BAD_REQUEST));

        evento eventoFound = eventoRepository.findById(presenca.getParticipante().getId())
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
