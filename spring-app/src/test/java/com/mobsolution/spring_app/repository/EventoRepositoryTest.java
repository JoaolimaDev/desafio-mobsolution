package com.mobsolution.spring_app.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;


import java.util.Optional;

import org.junit.jupiter.api.Test;
import com.mobsolution.spring_app.domain.model.Evento;
import com.mobsolution.spring_app.domain.repository.eventoRepository;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class EventoRepositoryTest {

    @Mock
    private eventoRepository eventoRepository;

    private Evento evento;

    @BeforeEach
    public void setUp() {
        evento = new Evento();
        evento.setId(1L);
        evento.setNome("Evento Teste");
        evento.setDataInicio(LocalDate.of(2023, 1, 1));
        evento.setDataFim(LocalDate.of(2023, 1, 2));
    }

    @Test
    public void testFindById() {
     
        when(eventoRepository.findById(1L)).thenReturn(Optional.of(evento));

        Optional<Evento> resultado = eventoRepository.findById(1L);

        assertTrue(resultado.isPresent());
        assertEquals("Evento Teste", resultado.get().getNome());
    }

    @Test
    public void testSave() {
        
        when(eventoRepository.save(any(Evento.class))).thenReturn(evento);

        Evento eventoSalvo = eventoRepository.save(evento);

        assertNotNull(eventoSalvo);
        assertEquals("Evento Teste", eventoSalvo.getNome());
    }
}
