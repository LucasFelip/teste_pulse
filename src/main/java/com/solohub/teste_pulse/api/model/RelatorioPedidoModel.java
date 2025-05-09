package com.solohub.teste_pulse.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true,onlyExplicitlyIncluded = true)
public class RelatorioPedidoModel extends AbstractModel {
    private Long notaFiscalId;
    private String numeroNota;
    private OffsetDateTime dataEmissao;
    private BigDecimal valorTotal;
    private BigDecimal frete;
}
