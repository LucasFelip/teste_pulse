package com.solohub.teste_pulse.api.dto.itemCarrinho;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemCarrinhoDTO {
    private Long id;
    private Long produtoId;
    private Integer quantidade;
    private BigDecimal precoUnitario;
}
