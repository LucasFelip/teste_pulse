package com.solohub.teste_pulse.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true,onlyExplicitlyIncluded = true)
public class PedidoModel extends AbstractModel {
    private String formaPagamento;
    private List<ItemPedidoModel> itens = new ArrayList<>();
    private BigDecimal frete;
    private BigDecimal valorTotal;
    private String status;
    private OffsetDateTime dataPedido;
}
