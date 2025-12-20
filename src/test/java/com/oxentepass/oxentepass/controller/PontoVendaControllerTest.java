package com.oxentepass.oxentepass.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import tools.jackson.databind.ObjectMapper;
import com.oxentepass.oxentepass.controller.request.PontoVendaRequest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class PontoVendaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deveCriarPontoVendaRetornandoHttp201() throws Exception {

        PontoVendaRequest request = new PontoVendaRequest(
                "Lojinha da Esquina",
                "Venda de Ingressos",
                "55290-000",
                "Heliópolis",
                "Av. Rui Barbosa",
                123);

        mockMvc.perform(post("/pontovenda")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Ponto de Venda " + request.nome() + " criado com sucesso"));
    }

    @Test
    void deveRejeitarCepInvalidoRetornandoHttp400() throws Exception {
        PontoVendaRequest request = new PontoVendaRequest(
                "Loja Errada",
                "Detalhes",
                "123",
                "Bairro",
                "Rua",
                1);

        mockMvc.perform(post("/pontovenda")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}