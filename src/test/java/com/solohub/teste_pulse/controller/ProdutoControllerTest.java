package com.solohub.teste_pulse.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.solohub.teste_pulse.api.controller.ProdutoController;
import com.solohub.teste_pulse.api.model.ProdutoInputModel;
import com.solohub.teste_pulse.api.model.ProdutoModel;
import com.solohub.teste_pulse.api.assembler.ProdutoAssembler;
import com.solohub.teste_pulse.domain.model.Produto;
import com.solohub.teste_pulse.domain.service.ProdutoService;
import com.solohub.teste_pulse.domain.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(ProdutoController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class ProdutoControllerTest {

    @Autowired
    MockMvc mvc;
    @Autowired
    ObjectMapper json;

    @MockBean
    ProdutoService service;
    @MockBean
    ModelMapper modelMapper;
    @MockBean
    ProdutoAssembler assembler;

    @Nested
    @DisplayName("POST /v1/produtos")
    class CriarProduto {

        @Test
        @DisplayName("201 quando dados válidos")
        void deveRetornar201QuandoSucesso() throws Exception {
            var input = new ProdutoInputModel();
            input.setNome("Caneta");
            input.setDescricao("Caneta azul");
            input.setPrecoUnitario(new BigDecimal("2.50"));
            input.setEstoque(100);
            input.setAtivo(true);

            // 2) monta o objeto de domínio que o service vai retornar
            var domain = Produto.builder()
                    .id(1L)
                    .nome(input.getNome())
                    .descricao(input.getDescricao())
                    .precoUnitario(input.getPrecoUnitario())
                    .estoque(input.getEstoque())
                    .ativo(input.getAtivo())
                    .build();

            // 3) monta o model que o assembler vai devolver
            var model = ProdutoModel.builder()
                    .nome(domain.getNome())
                    .descricao(domain.getDescricao())
                    .precoUnitario(domain.getPrecoUnitario())
                    .estoque(domain.getEstoque())
                    .ativo(domain.getAtivo())
                    .build();
            model.setId(1L);

            // configuro os mocks
            BDDMockito.given(modelMapper.map(input, Produto.class))
                    .willReturn(domain);
            BDDMockito.given(service.criar(domain))
                    .willReturn(domain);
            BDDMockito.given(assembler.toModel(domain))
                    .willReturn(model);

            // perform: não esquece o .content(...)
            mvc.perform(post("/v1/produtos")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content(json.writeValueAsString(input)))
                    .andExpect(status().isCreated())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.nome").value("Caneta"))
                    .andExpect(jsonPath("$.descricao").value("Caneta azul"))
                    .andExpect(jsonPath("$.precoUnitario").value(2.50))
                    .andExpect(jsonPath("$.estoque").value(100))
                    .andExpect(jsonPath("$.ativo").value(true));
        }
    }


    @Nested
    @DisplayName("GET /v1/produtos/{id}")
    class BuscarProduto {

        @Test
        @DisplayName("200 quando produto existe")
        void deveRetornar200QuandoExiste() throws Exception {
            var domain = Produto.builder()
                    .id(5L)
                    .nome("X")
                    .descricao("Desc")
                    .precoUnitario(new BigDecimal("9.99"))
                    .estoque(5)
                    .ativo(true)
                    .build();

            var model = ProdutoModel.builder()
                    .nome("X")
                    .descricao("Desc")
                    .precoUnitario(new BigDecimal("9.99"))
                    .estoque(5)
                    .ativo(true)
                    .build();
            model.setId(5L);

            BDDMockito.given(service.buscarPorId(5L))
                    .willReturn(Optional.of(domain));
            BDDMockito.given(assembler.toModel(domain))
                    .willReturn(model);

            mvc.perform(get("/v1/produtos/{id}", 5L))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(5))
                    .andExpect(jsonPath("$.nome").value("X"));
        }

        @Test
        @DisplayName("404 quando não existe")
        void deveRetornar404QuandoNaoExiste() throws Exception {
            BDDMockito.given(service.buscarPorId(anyLong()))
                    .willThrow(new ResourceNotFoundException("Produto", 9L));

            mvc.perform(get("/v1/produtos/{id}", 9L))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.detail")
                            .value("Produto com ID 9 não foi encontrado"));
        }
    }

    @Nested
    @DisplayName("DELETE /v1/produtos/{id}")
    class DeletarProduto {

        @Test
        @DisplayName("204 quando excluído com sucesso")
        void deveRetornar204QuandoSucesso() throws Exception {
            mvc.perform(delete("/v1/produtos/{id}", 3L))
                    .andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("404 quando produto não existe")
        void deveRetornar404QuandoNaoExiste() throws Exception {
            BDDMockito.doThrow(new ResourceNotFoundException("Produto", 7L))
                    .when(service).deletar(7L);

            mvc.perform(delete("/v1/produtos/{id}", 7L))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.detail")
                            .value("Produto com ID 7 não foi encontrado"));
        }
    }
}
