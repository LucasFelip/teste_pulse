package com.solohub.teste_pulse.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.solohub.teste_pulse.api.controller.EnderecoController;
import com.solohub.teste_pulse.api.model.EnderecoInputModel;
import com.solohub.teste_pulse.api.model.EnderecoModel;
import com.solohub.teste_pulse.api.assembler.EnderecoAssembler;
import com.solohub.teste_pulse.domain.model.Endereco;
import com.solohub.teste_pulse.domain.service.EnderecoService;
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
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(EnderecoController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class EnderecoControllerTest {

    @Autowired
    MockMvc mvc;
    @Autowired
    ObjectMapper json;

    @MockBean
    EnderecoService service;
    @MockBean
    EnderecoAssembler assembler;
    @MockBean
    ModelMapper modelMapper;

    @Nested
    @DisplayName("POST /v1/clientes/{clienteId}/enderecos")
    class CriarEndereco {
        @Test
        @DisplayName("201 quando dados válidos")
        void deveRetornar201QuandoSucesso() throws Exception {
            var clienteId = 1L;
            var input = new EnderecoInputModel();
            input.setRua("Rua A"); input.setNumero("123"); input.setCidade("Cidade");
            input.setEstado("EST"); input.setCep("00000-000"); input.setPais("Brasil");
            input.setBairro("Bairro"); input.setComplemento("Complemento");
            input.setIsPrincipal(true);

            var domain = Endereco.builder()
                    .id(5L).rua(input.getRua())
                    .numero(input.getNumero()).bairro(input.getBairro())
                    .cidade(input.getCidade()).estado(input.getEstado())
                    .cep(input.getCep()).pais(input.getPais())
                    .complemento(input.getComplemento())
                    .isPrincipal(input.getIsPrincipal())
                    .build();

            var model = EnderecoModel.builder()
                    .rua(domain.getRua()).numero(domain.getNumero())
                    .bairro(domain.getBairro()).cidade(domain.getCidade())
                    .estado(domain.getEstado()).cep(domain.getCep())
                    .pais(domain.getPais()).complemento(domain.getComplemento())
                    .isPrincipal(domain.getIsPrincipal())
                    .build();
            model.setId(5L);

            BDDMockito.given(modelMapper.map(input, Endereco.class))
                    .willReturn(domain);
            BDDMockito.given(service.criar(clienteId, domain))
                    .willReturn(domain);
            BDDMockito.given(assembler.toModel(domain))
                    .willReturn(model);

            mvc.perform(post("/v1/clientes/{clienteId}/enderecos", clienteId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json.writeValueAsString(input)))
                    .andExpect(status().isCreated())
                    .andExpect(content().contentType("application/hal+json"))
                    .andExpect(jsonPath("$.id").value(5))
                    .andExpect(jsonPath("$.rua").value("Rua A"))
                    .andExpect(jsonPath("$.cidade").value("Cidade"))
                    .andExpect(jsonPath("$.isPrincipal").value(true));
        }

        @Test
        @DisplayName("404 quando cliente não encontrado")
        void deveRetornar404QuandoClienteNaoExiste() throws Exception {
            var clienteId = 2L;
            BDDMockito.given(service.criar(eq(clienteId), any()))
                    .willThrow(new ResourceNotFoundException("Cliente", clienteId));

            mvc.perform(post("/v1/clientes/{clienteId}/enderecos", clienteId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{}"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.detail")
                            .value("Cliente com ID 2 não foi encontrado"));
        }
    }

    @Nested
    @DisplayName("GET /v1/clientes/{clienteId}/enderecos/principal")
    class BuscarPrincipal {
        @Test
        @DisplayName("200 quando existe endereço principal")
        void deveRetornar200QuandoExiste() throws Exception {
            var clienteId = 3L;
            var domain = Endereco.builder().id(7L).rua("Principal").build();
            var model  = EnderecoModel.builder().rua("Principal").build();
            model.setId(7L);

            BDDMockito.given(service.buscarPrincipal(clienteId))
                    .willReturn(Optional.of(domain));
            BDDMockito.given(assembler.toModel(domain))
                    .willReturn(model);

            mvc.perform(get("/v1/clientes/{clienteId}/enderecos/principal", clienteId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(7))
                    .andExpect(jsonPath("$.rua").value("Principal"));
        }

        @Test
        @DisplayName("404 quando não há endereço principal")
        void deveRetornar404QuandoNaoExiste() throws Exception {
            var clienteId = 4L;
            BDDMockito.given(service.buscarPrincipal(clienteId))
                    .willReturn(Optional.empty());

            mvc.perform(get("/v1/clientes/{clienteId}/enderecos/principal", clienteId))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.detail")
                            .value("Endereço principal com ID 4 não foi encontrado"));
        }
    }

    @Nested
    @DisplayName("DELETE /v1/clientes/{clienteId}/enderecos/{enderecoId}")
    class DeletarEndereco {
        @Test
        @DisplayName("204 quando excluído com sucesso")
        void deveRetornar204QuandoSucesso() throws Exception {
            mvc.perform(delete("/v1/clientes/{clienteId}/enderecos/{enderecoId}", 1, 2))
                    .andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("404 quando endereço não existe")
        void deveRetornar404QuandoNaoExiste() throws Exception {
            BDDMockito.doThrow(new ResourceNotFoundException("Endereço", 9L))
                    .when(service).deletar(9L);

            mvc.perform(delete("/v1/clientes/{clienteId}/enderecos/{enderecoId}", 1, 9))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.detail")
                            .value("Endereço com ID 9 não foi encontrado"));
        }
    }
}
