package com.oxentepass.oxentepass.service;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.oxentepass.oxentepass.entity.IngressoVenda;
import com.oxentepass.oxentepass.entity.Venda;
import com.oxentepass.oxentepass.exceptions.RecursoNaoEncontradoException;
import com.oxentepass.oxentepass.repository.VendaRepository;
import com.oxentepass.oxentepass.service.implementation.VendaServiceImpl;
import com.querydsl.core.types.Predicate;

/**
 * @author Victor Cauã
 * Testes unitários para VendaService
 */


@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@Transactional
class VendaServiceTest {

    @Mock
    private VendaRepository vendaRepository;

    @InjectMocks
    private VendaServiceImpl vendaService;

    private Venda venda;

    // Configuração inicial antes de cada teste
    @BeforeEach
    void setup() {
        venda = new Venda();
        venda.setIngressos(new ArrayList<>());
    }

    // Teste para criação de venda
    @Test
    void deveCriarVenda() {
        vendaService.criarVenda(venda);
        verify(vendaRepository).save(venda);
    }

    // Teste para buscar venda por ID
    @Test
    void deveBuscarVendaPorId() {
        when(vendaRepository.findById(1L)).thenReturn(Optional.of(venda));

        Venda resultado = vendaService.buscarVendaPorId(1L);

        assertNotNull(resultado);
    }

    // Teste para lançar exceção ao buscar venda inexistente
    @Test
    void deveFinalizarVenda() {
        when(vendaRepository.findById(1L)).thenReturn(Optional.of(venda));

        when(vendaRepository.save(any())).thenReturn(venda);

        Venda resultado = vendaService.finalizarVenda(1L);

        assertNotNull(resultado);
        verify(vendaRepository).save(venda);
    }

    // Teste para cancelar venda
    @Test
    void deveCancelarVenda() {
        when(vendaRepository.findById(1L)).thenReturn(Optional.of(venda));

        vendaService.cancelarVenda(1L);

        verify(vendaRepository).save(venda);
    }

    // Teste para listar todas as vendas com paginação
    @Test
    void deveListarTodasVendas() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Venda> page = new PageImpl<>(new ArrayList<>());

        when(vendaRepository.findAll(pageable)).thenReturn(page);

        Page<Venda> resultado = vendaService.listarTodasVendas(pageable);

        assertNotNull(resultado);
    }

    // Teste para filtrar vendas com predicado e paginação
    @Test
    void deveFiltrarVendas() {
        Pageable pageable = PageRequest.of(0, 10);
        Predicate predicate = mock(Predicate.class);
        Page<Venda> page = new PageImpl<>(new ArrayList<>());

        when(vendaRepository.findAll(predicate, pageable)).thenReturn(page);

        Page<Venda> resultado = vendaService.filtrarVendas(predicate, pageable);

        assertNotNull(resultado);
    }

    // Teste para remover ingresso de uma venda
    @Test
    void deveRemoverIngresso() {
        IngressoVenda ingresso = new IngressoVenda();
        ingresso.setId(10L);
        venda.getIngressos().add(ingresso);

        when(vendaRepository.findById(1L)).thenReturn(Optional.of(venda));

        when(vendaRepository.save(any())).thenReturn(venda);

        Venda resultado = vendaService.removerIngresso(10L, 1L);

        assertTrue(resultado.getIngressos().isEmpty());
    }

    // Teste para lançar exceção ao remover ingresso de venda inexistente
    @Test
    void deveLancarExcecaoAoRemoverIngressoInexistente() {
        when(vendaRepository.findById(1L)).thenReturn(Optional.of(venda));

        assertThrows(RecursoNaoEncontradoException.class, () ->
            vendaService.removerIngresso(99L, 1L)
        );

        verify(vendaRepository, never()).save(any());
    }
}
