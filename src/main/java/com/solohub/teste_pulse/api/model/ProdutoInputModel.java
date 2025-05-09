package com.solohub.teste_pulse.api.model;

import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

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
