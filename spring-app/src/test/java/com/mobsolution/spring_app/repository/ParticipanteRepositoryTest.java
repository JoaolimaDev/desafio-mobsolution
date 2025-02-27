package com.mobsolution.spring_app.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mobsolution.spring_app.domain.model.Evento;
import com.mobsolution.spring_app.domain.model.Participante;
import com.mobsolution.spring_app.domain.repository.participanteRepository;

@ExtendWith(MockitoExtension.class)
public class ParticipanteRepositoryTest {


    @Mock
    private participanteRepository participanteRepository;

    private Participante participante;

    @BeforeEach
    public void setUp() {

        Evento evento = new Evento();
        evento.setId(1L);
        evento.setNome("Evento Teste");
        evento.setDataInicio(LocalDate.of(2023, 1, 1));
        evento.setDataFim(LocalDate.of(2023, 1, 2));

        participante = new Participante();
        participante.setCpf("11936545489");
        participante.setEmail("teste@gmaill.com");
        participante.setEvento(evento);
        participante.setNome("teste");
        participante.setId(1L);

    }


    @Test
    public void testFindById() {

        when(participanteRepository.findById(1L)).thenReturn(Optional.of(participante));


        Optional<Participante> resultado = participanteRepository.findById(1L);

        assertTrue(resultado.isPresent());
        assertEquals("teste", resultado.get().getNome());
    }


    @Test
    public void testSave() {

       when(participanteRepository.save(any(Participante.class))).thenReturn(participante);

        Participante resultado = participanteRepository.save(participante);
        assertNotNull(resultado);
        assertEquals("teste", resultado.getNome());
    }
}
