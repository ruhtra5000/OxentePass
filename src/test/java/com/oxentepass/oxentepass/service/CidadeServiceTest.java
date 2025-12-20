package com.oxentepass.oxentepass.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.oxentepass.oxentepass.entity.Cidade;
import com.oxentepass.oxentepass.exceptions.RecursoDuplicadoException;
import com.oxentepass.oxentepass.repository.CidadeRepository;
import com.oxentepass.oxentepass.service.implementation.CidadeServiceImpl;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@ExtendWith(MockitoExtension.class)
public class CidadeServiceTest {
    
    @Mock
    private CidadeRepository cidadeRepository;

    @InjectMocks
    private CidadeServiceImpl cidadeService;

    @Test
    @Rollback
    public void testeCadastroCidadeComSucesso() {
        Cidade cidade = new Cidade();
        cidade.setNome("Salvador");

        when(cidadeRepository.findByNome("Salvador")).thenReturn(Optional.empty());

        cidadeService.criarCidade(cidade);

        verify(cidadeRepository, times(1)).findByNome("Salvador");
        verify(cidadeRepository, times(1)).save(cidade);
    }

    @Test
    @Rollback
    public void testeCadastroCidadeDuplicada() {
        Cidade cidade = new Cidade();
        cidade.setNome("Salvador");

        when(cidadeRepository.findByNome("Salvador")).thenReturn(Optional.of(new Cidade()));

        RecursoDuplicadoException err = assertThrows(RecursoDuplicadoException.class, () -> {
            cidadeService.criarCidade(cidade);
        });

        assertEquals(
            "Uma cidade com o nome \"Salvador\" já se encontra cadastrada.",
            err.getMessage()
        );

        verify(cidadeRepository, times(1)).findByNome("Salvador");
        verify(cidadeRepository, never()).save(any());
    }
}
