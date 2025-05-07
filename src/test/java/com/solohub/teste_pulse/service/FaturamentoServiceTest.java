package com.solohub.teste_pulse.service;

import com.solohub.teste_pulse.domain.model.NotaFiscal;
import com.solohub.teste_pulse.domain.model.Pedido;
import com.solohub.teste_pulse.domain.repository.NotaFiscalRepository;
import com.solohub.teste_pulse.domain.service.FaturamentoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
public class FaturamentoServiceTest {
    @Mock private NotaFiscalRepository notaFiscalRepository;
    @InjectMocks private FaturamentoService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void emitirNotaFiscal_devePersistirENotarCorreta() {
        Pedido pedido = Pedido.builder()
                .id(1L)
                .valorTotal(BigDecimal.valueOf(250))
                .dataPedido(OffsetDateTime.now())
                .build();

        when(notaFiscalRepository.save(any(NotaFiscal.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        NotaFiscal result = service.emitirNotaFiscal(pedido);

        assertNotNull(result);
        assertEquals(pedido, result.getPedido());

        String json = result.getJsonNota();
        assertTrue(json.contains("\"pedido\": 1") || json.contains("pedido': 1"));
        assertTrue(json.contains("valor do pedido") && json.contains("250"));
        assertTrue(json.contains("data do pedido"));
    }
}
