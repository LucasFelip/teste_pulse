package com.solohub.teste_pulse.controller;

import com.solohub.teste_pulse.api.controller.RelatorioController;
import com.solohub.teste_pulse.api.assembler.RelatorioPedidoAssembler;
import com.solohub.teste_pulse.api.model.NotaFiscalModel;
import com.solohub.teste_pulse.api.model.PedidoModel;
import com.solohub.teste_pulse.api.model.RelatorioPedidoModel;
import com.solohub.teste_pulse.domain.model.NotaFiscal;
import com.solohub.teste_pulse.domain.model.Pedido;
import com.solohub.teste_pulse.domain.model.enums.FormaPagamento;
import com.solohub.teste_pulse.domain.model.enums.PedidoStatus;
import com.solohub.teste_pulse.domain.model.record.RelatorioPedido;
import com.solohub.teste_pulse.domain.service.RelatorioService;
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
import java.time.OffsetDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RelatorioController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class RelatorioControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private RelatorioService relatorioService;

    @MockBean
    private RelatorioPedidoAssembler assembler;

    @Nested
    @DisplayName("GET /v1/clientes/{clienteId}/relatorios/pedidos")
    class ListarRelatorio {

        @Test
        @DisplayName("200 quando há pedidos para o cliente")
        void deveRetornar200QuandoExistirPedidos() throws Exception {
            Long clienteId = 42L;

            Pedido pedido = Pedido.builder()
                    .id(7L)
                    .valorTotal(new BigDecimal("150.00"))
                    .frete(new BigDecimal("10.00"))
                    .dataPedido(OffsetDateTime.parse("2025-05-01T10:00:00Z"))
                    .formaPagamento(FormaPagamento.PIX)
                    .status(PedidoStatus.PAGO)
                    .build();
            NotaFiscal nota = NotaFiscal.builder()
                    .id(99L)
                    .numero("NF-123")
                    .dataEmissao(OffsetDateTime.parse("2025-05-02T12:00:00Z"))
                    .jsonNota("{}")
                    .build();
            RelatorioPedido registro = new RelatorioPedido(pedido, nota);
            var listaDominio = List.of(registro);

            BDDMockito.given(relatorioService.gerarRelatorioPorCliente(clienteId))
                    .willReturn(listaDominio);

            PedidoModel pedidoModel = PedidoModel.builder()
                    .formaPagamento(pedido.getFormaPagamento().name())
                    .valorTotal(pedido.getValorTotal())
                    .frete(pedido.getFrete())
                    .status(pedido.getStatus().name())
                    .dataPedido(pedido.getDataPedido())
                    .build();
            pedidoModel.setId(pedido.getId());

            NotaFiscalModel notaModel = NotaFiscalModel.builder()
                    .numero(nota.getNumero())
                    .dataEmissao(nota.getDataEmissao())
                    .jsonNota(nota.getJsonNota())
                    .build();
            notaModel.setId(nota.getId());

            RelatorioPedidoModel model = RelatorioPedidoModel.builder()
                    .pedido(pedidoModel)
                    .notaFiscal(notaModel)
                    .build();
            model.setId(pedido.getId());

            BDDMockito.given(assembler.toCollectionModel(listaDominio))
                    .willReturn(CollectionModel.of(List.of(model)));

            mvc.perform(get("/v1/clientes/{clienteId}/relatorios/pedidos", clienteId)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$._embedded.relatorioPedidoModelList").isArray())
                    .andExpect(jsonPath("$._embedded.relatorioPedidoModelList[0].pedido.id").value(7))
                    .andExpect(jsonPath("$._embedded.relatorioPedidoModelList[0].pedido.formaPagamento").value("PIX"))
                    .andExpect(jsonPath("$._embedded.relatorioPedidoModelList[0].pedido.valorTotal").value(150.00))
                    .andExpect(jsonPath("$._embedded.relatorioPedidoModelList[0].pedido.frete").value(10.00))
                    .andExpect(jsonPath("$._embedded.relatorioPedidoModelList[0].notaFiscal.id").value(99))
                    .andExpect(jsonPath("$._embedded.relatorioPedidoModelList[0].notaFiscal.numero").value("NF-123"));
        }

        @Test
        @DisplayName("404 quando não há pedidos para o cliente")
        void deveRetornar404QuandoNaoExistiremPedidos() throws Exception {
            Long clienteId = 99L;
            BDDMockito.given(relatorioService.gerarRelatorioPorCliente(clienteId))
                    .willThrow(new com.solohub.teste_pulse.domain.exceptions.ResourceNotFoundException("Pedidos", clienteId));

            mvc.perform(get("/v1/clientes/{clienteId}/relatorios/pedidos", clienteId)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.detail")
                            .value("Pedidos com ID 99 não foi encontrado"));
        }
    }
}
