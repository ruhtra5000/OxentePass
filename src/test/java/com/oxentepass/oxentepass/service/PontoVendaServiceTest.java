package com.oxentepass.oxentepass.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.oxentepass.oxentepass.controller.request.PontoVendaRequest;
import com.oxentepass.oxentepass.entity.PontoVenda;
import com.oxentepass.oxentepass.exceptions.RecursoDuplicadoException;
import com.oxentepass.oxentepass.repository.PontoVendaRepository;
import com.oxentepass.oxentepass.service.implementation.PontoVendaServiceImpl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * @author Guilherme Paes
 * Testes unitários para PontoVendaService
 */

@ExtendWith(MockitoExtension.class)
class PontoVendaServiceTest {

    @InjectMocks
    private PontoVendaServiceImpl service;

    @Mock
    private PontoVendaRepository repository;

    @Test
    void deveCadastrarPontoVendaComCepLimpo() {

        PontoVendaRequest dto = new PontoVendaRequest("Loja Teste", "Detalhes", "55000-000", "Bairro", "Rua", 10);

        PontoVenda pv = dto.paraEntidade();

        when(repository.existsByNomeAndEnderecoCepAndEnderecoNumero(any(), any(), anyInt()))
                .thenReturn(false);

        service.cadastrarPontoVenda(pv);

        assertEquals("55000000", pv.getEndereco().getCep(), "O CEP deve ser salvo sem hífens");
        verify(repository, times(1)).save(pv);
    }

    @Test
    void deveRejeitarCadastroPontoVendaDuplicado() {
        // Arrange
        PontoVendaRequest dto = new PontoVendaRequest("Loja Duplicada", "Detalhes", "55292-000", "Bairro", "Rua", 10);
        PontoVenda pv = dto.paraEntidade();

        when(repository.existsByNomeAndEnderecoCepAndEnderecoNumero(any(), any(), anyInt()))
                .thenReturn(true);

        // Act & Assert
        assertThrows(RecursoDuplicadoException.class, () -> service.cadastrarPontoVenda(pv));
        verify(repository, never()).save(any());
    }
}