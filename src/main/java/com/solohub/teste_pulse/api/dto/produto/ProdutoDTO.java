package com.solohub.teste_pulse.api.dto.produto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProdutoDTO {
    private Long id;
    private String nome;
    private String descricao;
    private BigDecimal precoUnitario;
    private Integer estoque;
    private Boolean ativo;
}
