package com.solohub.teste_pulse.controller;

import com.solohub.teste_pulse.api.controller.TransportadoraController;
import com.solohub.teste_pulse.api.assembler.TransportadoraAssembler;
import com.solohub.teste_pulse.api.model.TransportadoraModel;
import com.solohub.teste_pulse.domain.model.Transportadora;
import com.solohub.teste_pulse.domain.service.TransportadoraService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TransportadoraController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class TransportadoraControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private TransportadoraService service;

    @MockBean
    private TransportadoraAssembler assembler;

    @Nested
    @DisplayName("GET /v1/transportadoras")
    class ListarTransportadoras {

        @Test
        @DisplayName("200 e retorna lista de transportadoras")
        void deveRetornar200ELista() throws Exception {
            Transportadora t1 = new Transportadora();
            t1.setId(1L);
            t1.setNome("FastShip");
            t1.setFreteFixo(new BigDecimal("20.00"));

            Transportadora t2 = new Transportadora();
            t2.setId(2L);
            t2.setNome("SuperExpress");
            t2.setFreteFixo(new BigDecimal("15.50"));

            List<Transportadora> listaDominio = List.of(t1, t2);
            BDDMockito.given(service.listarTodas()).willReturn(listaDominio);

            TransportadoraModel m1 = TransportadoraModel.builder()
                    .nome(t1.getNome())
                    .freteFixo(t1.getFreteFixo())
                    .build();
            m1.setId(t1.getId());

            TransportadoraModel m2 = TransportadoraModel.builder()
                    .nome(t2.getNome())
                    .freteFixo(t2.getFreteFixo())
                    .build();
            m2.setId(t2.getId());

            BDDMockito.given(assembler.toCollectionModel(listaDominio))
                    .willReturn(CollectionModel.of(List.of(m1, m2)));

            mvc.perform(get("/v1/transportadoras")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$._embedded.transportadoraModelList", hasSize(2)))
                    .andExpect(jsonPath("$._embedded.transportadoraModelList[0].id").value(1))
                    .andExpect(jsonPath("$._embedded.transportadoraModelList[0].nome").value("FastShip"))
                    .andExpect(jsonPath("$._embedded.transportadoraModelList[0].freteFixo").value(20.00))
                    .andExpect(jsonPath("$._embedded.transportadoraModelList[1].id").value(2))
                    .andExpect(jsonPath("$._embedded.transportadoraModelList[1].nome").value("SuperExpress"))
                    .andExpect(jsonPath("$._embedded.transportadoraModelList[1].freteFixo").value(15.50));
        }

        @Test
        @DisplayName("200 e retorna lista vazia quando não há transportadoras")
        void deveRetornar200EListaVazia() throws Exception {
            BDDMockito.given(service.listarTodas()).willReturn(List.of());
            BDDMockito.given(assembler.toCollectionModel(List.of()))
                    .willReturn(CollectionModel.empty());

            mvc.perform(get("/v1/transportadoras")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$._embedded.transportadoraModelList").doesNotExist());
        }
    }
}
