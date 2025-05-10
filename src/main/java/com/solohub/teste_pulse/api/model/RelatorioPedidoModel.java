package com.solohub.teste_pulse.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true,onlyExplicitlyIncluded = true)
public class RelatorioPedidoModel extends AbstractModel {
    private PedidoModel pedido;
    private NotaFiscalModel notaFiscal;
}
