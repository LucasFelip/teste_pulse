package com.solohub.teste_pulse.service;

import com.solohub.teste_pulse.domain.exceptions.ResourceNotFoundException;
import com.solohub.teste_pulse.domain.model.NotaFiscal;
import com.solohub.teste_pulse.domain.model.Pedido;
import com.solohub.teste_pulse.domain.model.enums.FormaPagamento;
import com.solohub.teste_pulse.domain.model.enums.PedidoStatus;
import com.solohub.teste_pulse.domain.model.record.RelatorioPedido;
import com.solohub.teste_pulse.domain.repository.NotaFiscalRepository;
import com.solohub.teste_pulse.domain.repository.PedidoRepository;
import com.solohub.teste_pulse.domain.service.RelatorioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
public class RelatorioServiceTest {
    @Mock private PedidoRepository pedidoRepository;
    @Mock private NotaFiscalRepository notaFiscalRepository;
    @InjectMocks private RelatorioService service;

    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void gerarRelatorioPorCliente_quandoPedidosENotas_existe_retornaLista() {
        Long clienteId = 1L;
        Pedido pedido = Pedido.builder()
                .id(100L)
                .valorTotal(BigDecimal.valueOf(200))
                .frete(BigDecimal.valueOf(10))
                .dataPedido(OffsetDateTime.now())
                .formaPagamento(FormaPagamento.CARTAO)
                .status(PedidoStatus.PAGO)
                .build();
        NotaFiscal nota = NotaFiscal.builder()
                .id(50L)
                .pedido(pedido)
                .numero("NF100")
                .dataEmissao(OffsetDateTime.now())
                .jsonNota("{}")
                .build();

        when(pedidoRepository.findByClienteId(clienteId))
                .thenReturn(List.of(pedido));
        when(notaFiscalRepository.findByPedidoId(100L))
                .thenReturn(Optional.of(nota));

        List<RelatorioPedido> rel = service.gerarRelatorioPorCliente(clienteId);

        assertEquals(1, rel.size());
        RelatorioPedido rp = rel.get(0);
        assertEquals(pedido, rp.pedido());
        assertEquals(nota, rp.notaFiscal());
    }

    @Test
    void gerarRelatorioPorCliente_quandoSemPedidos_lancaResourceNotFound() {
        when(pedidoRepository.findByClienteId(2L))
                .thenReturn(Collections.emptyList());

        assertThrows(ResourceNotFoundException.class,
                () -> service.gerarRelatorioPorCliente(2L));
    }

    @Test
    void gerarRelatorioPorCliente_quandoNotaNaoEncontrada_lancaResourceNotFound() {
        Long clienteId = 3L;
        Pedido pedido = Pedido.builder().id(200L).build();
        when(pedidoRepository.findByClienteId(clienteId))
                .thenReturn(List.of(pedido));
        when(notaFiscalRepository.findByPedidoId(200L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.gerarRelatorioPorCliente(clienteId));
    }
}
