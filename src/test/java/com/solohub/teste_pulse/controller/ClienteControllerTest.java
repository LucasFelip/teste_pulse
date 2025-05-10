package com.solohub.teste_pulse.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.solohub.teste_pulse.api.controller.ClienteController;
import com.solohub.teste_pulse.api.assembler.ClienteAssembler;
import com.solohub.teste_pulse.api.model.ClienteInputModel;
import com.solohub.teste_pulse.api.model.ClienteModel;
import com.solohub.teste_pulse.domain.exceptions.ResourceNotFoundException;
import com.solohub.teste_pulse.domain.model.Cliente;
import com.solohub.teste_pulse.domain.service.ClienteService;
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

import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(controllers = ClienteController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class ClienteControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper json;

    @MockBean
    private ClienteService service;

    @MockBean
    private ModelMapper modelMapper;

    @MockBean
    private ClienteAssembler assembler;

    @Nested
    @DisplayName("POST /v1/clientes")
    class CriarCliente {

        @Test
        @DisplayName("201 quando dados válidos")
        void deveRetornar201QuandoSucesso() throws Exception {
            var input = new ClienteInputModel();
            input.setNome("João");
            input.setEmail("joao@example.com");

            var domain = Cliente.builder()
                    .id(1L)
                    .nome("João")
                    .email("joao@example.com")
                    .build();

            var model = ClienteModel.builder()
                    .nome("João")
                    .email("joao@example.com")
                    .build();
            model.setId(1L);

            BDDMockito.given(modelMapper.map(input, Cliente.class)).willReturn(domain);
            BDDMockito.given(service.criar(domain)).willReturn(domain);
            BDDMockito.given(assembler.toModel(domain)).willReturn(model);

            mvc.perform(post("/v1/clientes")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json.writeValueAsString(input)))
                    .andExpect(status().isCreated())
                    .andExpect(content().contentType("application/hal+json"))
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.nome").value("João"))
                    .andExpect(jsonPath("$.email").value("joao@example.com"));
        }
    }

    @Nested
    @DisplayName("GET /v1/clientes")
    class ListarClientes {

        @Test
        @DisplayName("200 retorna lista de clientes")
        void deveRetornar200ELista() throws Exception {
            var domain1 = Cliente.builder().id(1L).nome("A").email("a@x.com").build();
            var domain2 = Cliente.builder().id(2L).nome("B").email("b@x.com").build();

            var model1 = ClienteModel.builder().nome("A").email("a@x.com").build();
            model1.setId(1L);
            var model2 = ClienteModel.builder().nome("B").email("b@x.com").build();
            model2.setId(2L);

            BDDMockito.given(service.listarTodos()).willReturn(List.of(domain1, domain2));
            BDDMockito.given(assembler.toCollectionModel(List.of(domain1, domain2)))
                    .willReturn(CollectionModel.of(List.of(model1, model2)));

            mvc.perform(get("/v1/clientes"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$[0].id").value(1))
                    .andExpect(jsonPath("$[1].id").value(2));
        }
    }

    @Nested
    @DisplayName("GET /v1/clientes/{id}")
    class BuscarCliente {

        @Test
        @DisplayName("200 quando cliente existe")
        void deveRetornar200QuandoExiste() throws Exception {
            var domain = Cliente.builder().id(5L).nome("X").email("x@x.com").build();
            var model  = ClienteModel.builder().nome("X").email("x@x.com").build();
            model.setId(5L);

            BDDMockito.given(service.buscarPorId(5L)).willReturn(Optional.of(domain));
            BDDMockito.given(assembler.toModel(domain)).willReturn(model);

            mvc.perform(get("/v1/clientes/{id}", 5L))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(5))
                    .andExpect(jsonPath("$.nome").value("X"))
                    .andExpect(jsonPath("$.email").value("x@x.com"));
        }

        @Test
        @DisplayName("404 quando não existe")
        void deveRetornar404QuandoNaoExiste() throws Exception {
            BDDMockito.given(service.buscarPorId(anyLong()))
                    .willThrow(new ResourceNotFoundException("Cliente", 9L));

            mvc.perform(get("/v1/clientes/{id}", 9L))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.detail")
                            .value("Cliente com ID 9 não foi encontrado"));
        }
    }

    @Nested
    @DisplayName("DELETE /v1/clientes/{id}")
    class DeletarCliente {

        @Test
        @DisplayName("204 quando excluído com sucesso")
        void deveRetornar204QuandoSucesso() throws Exception {
            mvc.perform(delete("/v1/clientes/{id}", 3L))
                    .andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("404 quando cliente não existe")
        void deveRetornar404QuandoNaoExiste() throws Exception {
            BDDMockito.willThrow(new ResourceNotFoundException("Cliente", 7L))
                    .given(service).deletar(7L);

            mvc.perform(delete("/v1/clientes/{id}", 7L))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.detail")
                            .value("Cliente com ID 7 não foi encontrado"));
        }
    }
}
