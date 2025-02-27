package com.mobsolution.spring_app.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mobsolution.spring_app.domain.model.Participante;
import com.mobsolution.spring_app.domain.model.Presenca;
import com.mobsolution.spring_app.domain.repository.presencaRepository;

@ExtendWith(MockitoExtension.class)
public class PresencaRepositoryTest {

    @Mock
    private presencaRepository presencaRepository;

    private Presenca presenca;
    private Participante participante;

    @BeforeEach
    public void setUp() {

        participante = new Participante();
     
        participante.setId(1L);

   
        presenca = new Presenca();
        presenca.setId(1L);
        presenca.setData(LocalDate.now());
        presenca.setParticipante(participante);
    }

    @Test
    public void testFindAllByParticipante() {
   
        List<Presenca> listaPresencas = new ArrayList<>();
        listaPresencas.add(presenca);
        when(presencaRepository.findAllByParticipante(participante)).thenReturn(listaPresencas);

        List<Presenca> resultado = presencaRepository.findAllByParticipante(participante);

     
        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        assertEquals(presenca.getId(), resultado.get(0).getId());
    }

    @Test
    public void testSave() {
      
        when(presencaRepository.save(any(Presenca.class))).thenReturn(presenca);

     
        Presenca presencaSalva = presencaRepository.save(presenca);


        assertNotNull(presencaSalva);
        assertEquals(presenca.getId(), presencaSalva.getId());
    }
}
