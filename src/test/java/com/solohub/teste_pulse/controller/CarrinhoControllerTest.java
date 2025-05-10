package com.solohub.teste_pulse.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.solohub.teste_pulse.api.assembler.CarrinhoAssembler;
import com.solohub.teste_pulse.api.controller.CarrinhoController;
import com.solohub.teste_pulse.api.model.CarrinhoModel;
import com.solohub.teste_pulse.api.model.ItemCarrinhoModel;
import com.solohub.teste_pulse.domain.model.Carrinho;
import com.solohub.teste_pulse.domain.model.enums.CartStatus;
import com.solohub.teste_pulse.domain.service.CarrinhoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CarrinhoController.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
public class CarrinhoControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper json;

    @MockBean
    private CarrinhoService service;

    @MockBean
    private CarrinhoAssembler assembler;

    @Nested
    @DisplayName("POST /v1/carrinhos?clienteId=")
    class CriarCarrinho {
        @Test
        @DisplayName("deve retornar 201 e corpo com CarrinhoModel")
        void deveCriar() throws Exception {
            Long clienteId = 10L;
            Carrinho domain = Carrinho.builder()
                    .id(5L)
                    .status(CartStatus.ABERTO)
                    .dataCriacao(OffsetDateTime.now())
                    .build();
            CarrinhoModel model = CarrinhoModel.builder()
                    .clienteId(clienteId)
                    .status(CartStatus.ABERTO)
                    .build();
            model.setId(5L);
            BDDMockito.given(service.criarCarrinho(clienteId)).willReturn(domain);
            BDDMockito.given(assembler.toModel(domain)).willReturn(model);

            mvc.perform(post("/v1/carrinhos")
                            .queryParam("clienteId", clienteId.toString()))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(5))
                    .andExpect(jsonPath("$.clienteId").value(10))
                    .andExpect(jsonPath("$.status").value("ABERTO"));
        }
    }

    @Nested
    @DisplayName("POST /v1/carrinhos/{id}/itens")
    class AdicionarItem {
        @Test
        @DisplayName("deve retornar 201 e CarrinhoModel atualizado")
        void deveAdicionarItem() throws Exception {
            Long carrinhoId = 7L;
            ItemCarrinhoModel input = ItemCarrinhoModel.builder()
                    .produtoId(3L)
                    .quantidade(2)
                    .build();
            Carrinho domain = Carrinho.builder()
                    .id(carrinhoId)
                    .status(CartStatus.ABERTO)
                    .build();
            CarrinhoModel model = CarrinhoModel.builder()
                    .status(CartStatus.ABERTO)
                    .build();
            model.setId(carrinhoId);

            BDDMockito.given(service.adicionarItemCarrinho(carrinhoId, 3L, 2))
                    .willReturn(domain);
            BDDMockito.given(assembler.toModel(domain)).willReturn(model);

            mvc.perform(post("/v1/carrinhos/{id}/itens", carrinhoId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json.writeValueAsString(input)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(7))
                    .andExpect(jsonPath("$.status").value("ABERTO"));
        }
    }

    @Nested
    @DisplayName("GET /v1/carrinhos/aberto")
    class BuscarAberto {
        @Test
        @DisplayName("deve retornar 200 e CarrinhoModel quando existe")
        void deve200QuandoExiste() throws Exception {
            Long clienteId = 20L;
            Carrinho domain = Carrinho.builder()
                    .id(8L)
                    .status(CartStatus.ABERTO)
                    .build();
            CarrinhoModel model = CarrinhoModel.builder()
                    .status(CartStatus.ABERTO)
                    .build();
            model.setId(8L);

            BDDMockito.given(service.buscarCarrinhoAberto(clienteId))
                    .willReturn(Optional.of(domain));
            BDDMockito.given(assembler.toModel(domain)).willReturn(model);

            mvc.perform(get("/v1/carrinhos/aberto")
                            .queryParam("clienteId", clienteId.toString()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(8))
                    .andExpect(jsonPath("$.status").value("ABERTO"));
        }

        @Test
        @DisplayName("deve retornar 404 quando não existe carrinho aberto")
        void deve404QuandoNaoExiste() throws Exception {
            BDDMockito.given(service.buscarCarrinhoAberto(anyLong()))
                    .willReturn(Optional.empty());

            mvc.perform(get("/v1/carrinhos/aberto")
                            .queryParam("clienteId", "99"))
                    .andExpect(status().isNotFound());
        }
    }
}