package com.solohub.teste_pulse.api.dto.relatorio;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RelatorioPedidoDTO {
    private Long pedidoId;
    private Long notaFiscalId;
    private String numeroNota;
    private OffsetDateTime dataEmissao;
    private BigDecimal valorTotal;
    private BigDecimal frete;
}
