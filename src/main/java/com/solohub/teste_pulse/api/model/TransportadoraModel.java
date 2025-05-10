package com.solohub.teste_pulse.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true,onlyExplicitlyIncluded = true)
public class TransportadoraModel extends AbstractModel {
    private String nome;
    private BigDecimal freteFixo;
    private List<PedidoModel> pedido = new ArrayList<>();
}
