package com.solohub.teste_pulse.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.solohub.teste_pulse.api.controller.CheckoutController;
import com.solohub.teste_pulse.api.model.CheckoutInputModel;
import com.solohub.teste_pulse.api.model.PedidoModel;
import com.solohub.teste_pulse.api.assembler.PedidoAssembler;
import com.solohub.teste_pulse.domain.exceptions.ResourceNotFoundException;
import com.solohub.teste_pulse.domain.model.Pedido;
import com.solohub.teste_pulse.domain.model.enums.FormaPagamento;
import com.solohub.teste_pulse.domain.model.enums.PedidoStatus;
import com.solohub.teste_pulse.domain.service.CheckoutService;
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

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(CheckoutController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class CheckoutControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper json;

    @MockBean
    private CheckoutService checkoutService;

    @MockBean
    private PedidoAssembler assembler;

    @Nested
    @DisplayName("POST /v1/checkout")
    class EfetuarCheckout {

        @Test
        @DisplayName("201 quando todos os dados são válidos")
        void deveRetornar201QuandoSucesso() throws Exception {
            CheckoutInputModel input = new CheckoutInputModel();
            input.setCarrinhoId(1L);
            input.setEnderecoId(2L);
            input.setTransportadoraId(3L);
            input.setFormaPagamento(FormaPagamento.PIX.name());

            Pedido domain = Pedido.builder()
                    .id(100L)
                    .valorTotal(new BigDecimal("123.45"))
                    .dataPedido(OffsetDateTime.now())
                    .status(PedidoStatus.PENDENTE)
                    .build();

            PedidoModel model = PedidoModel.builder()
                    .clienteId(null)  // not returned by this endpoint
                    .enderecoId(2L)
                    .transportadoraId(3L)
                    .formaPagamento(FormaPagamento.PIX.name())
                    .frete(new BigDecimal("0.00"))
                    .valorTotal(new BigDecimal("123.45"))
                    .status(domain.getStatus().name())
                    .dataPedido(domain.getDataPedido())
                    .build();
            model.setId(100L);

            BDDMockito.given(checkoutService.checkout(1L,2L,3L,FormaPagamento.PIX))
                    .willReturn(domain);
            BDDMockito.given(assembler.toModel(domain)).willReturn(model);

            mvc.perform(post("/v1/checkout")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json.writeValueAsString(input)))
                    .andExpect(status().isCreated())
                    .andExpect(content().contentType("application/hal+json"))
                    .andExpect(jsonPath("$.id").value(100))
                    .andExpect(jsonPath("$.enderecoId").value(2))
                    .andExpect(jsonPath("$.transportadoraId").value(3))
                    .andExpect(jsonPath("$.formaPagamento").value("PIX"))
                    .andExpect(jsonPath("$.valorTotal").value(123.45));
        }

        @Test
        @DisplayName("404 quando checkout lança ResourceNotFoundException")
        void deveRetornar404QuandoRecursoNaoEncontrado() throws Exception {
            CheckoutInputModel input = new CheckoutInputModel();
            input.setCarrinhoId(9L);
            input.setEnderecoId(2L);
            input.setTransportadoraId(3L);
            input.setFormaPagamento(FormaPagamento.CARTAO.name());

            BDDMockito.given(checkoutService.checkout(
                            anyLong(), anyLong(), anyLong(), any()))
                    .willThrow(new ResourceNotFoundException("Carrinho", 9L));

            mvc.perform(post("/v1/checkout")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json.writeValueAsString(input)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.detail").value("Carrinho com ID 9 não foi encontrado"));
        }
    }
}
