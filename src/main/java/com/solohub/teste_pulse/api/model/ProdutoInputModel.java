package com.solohub.teste_pulse.api.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProdutoInputModel {
    @NotBlank
    private String nome;
    private String descricao;
    @NotBlank
    private BigDecimal precoUnitario;
    @NotBlank
    private Integer estoque;
    private Boolean ativo = true;
}
