package com.oxentepass.oxentepass.controller;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(VendaController.class)
class VendaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void deveFinalizarVenda() throws Exception {

        mockMvc.perform(post("/venda/finalizar/1"))
        .andExpect(status().isOk())
        .andExpect(content().string("Venda finalizada com sucesso!"));
    }


    @Test
    void deveCancelarVenda() throws Exception {

        mockMvc.perform(post("/venda/cancelar/1"))
        .andExpect(status().isOk())
        .andExpect(content().string("Venda cancelada com sucesso!"));
    }

}