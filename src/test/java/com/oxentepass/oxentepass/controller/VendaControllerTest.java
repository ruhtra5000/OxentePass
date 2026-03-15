package com.oxentepass.oxentepass.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.oxentepass.oxentepass.service.VendaService;

/**
 * @author Victor Cauã
 * Testes de integração para VendaController
 */

@WebMvcTest(VendaController.class)
@ActiveProfiles("test")
class VendaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private VendaService vendaService;

    // Teste para finalizar uma venda com sucesso
    @Test
    void deveFinalizarVenda() throws Exception {

        mockMvc.perform(post("/venda/finalizar/1"))
        .andExpect(status().isOk())
        .andExpect(content().string("Venda finalizada com sucesso!"));
    }


    // Teste para cancelar uma venda com sucesso
    @Test
    void deveCancelarVenda() throws Exception {

        mockMvc.perform(post("/venda/cancelar/1"))
        .andExpect(status().isOk())
        .andExpect(content().string("Venda cancelada com sucesso!"));
    }

}