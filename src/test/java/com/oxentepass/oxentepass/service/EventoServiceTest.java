package com.oxentepass.oxentepass.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.oxentepass.oxentepass.entity.Evento;
import com.oxentepass.oxentepass.entity.EventoSimples;
import com.oxentepass.oxentepass.exceptions.EstadoInvalidoException;
import com.oxentepass.oxentepass.repository.EventoRepository;

/**
 * @author Arthur de Sá
 * Testes de integração para EventoService
 */

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class EventoServiceTest {
    
    @Autowired
    private EventoRepository eventoRepository;
    
    @Autowired
    private EventoService eventoService;

    // Cadastro de Evento com sucesso
    @Test
    @Rollback
    public void testeCadastroEventoSucesso() {
        Evento evento = new EventoSimples();
        evento.setNome("Teste evento");
        evento.setDataHoraInicio(LocalDateTime.now().minus(20L, ChronoUnit.HOURS));
        evento.setDataHoraFim(LocalDateTime.now().minus(10L, ChronoUnit.HOURS));

        eventoService.criarEvento(evento);

        List<Evento> eventos = eventoRepository.findAll();
        assertEquals("Teste evento", eventos.get(0).getNome());
        assertEquals(1, eventos.size());
    }

    // Cadastro de Evento com momento de fim antes do momento de início, lançando exceção
    @Test
    @Rollback
    public void testeCadastroEventoErroData() {
        Evento evento = new EventoSimples();
        evento.setNome("Teste evento");
        evento.setDataHoraInicio(LocalDateTime.now().minus(10L, ChronoUnit.HOURS));
        evento.setDataHoraFim(LocalDateTime.now().minus(20L, ChronoUnit.HOURS));

        assertThrows(EstadoInvalidoException.class, () -> {
            eventoService.criarEvento(evento);
        });

        List<Evento> eventos = eventoRepository.findAll();
        assertEquals(0, eventos.size());
    }
}
