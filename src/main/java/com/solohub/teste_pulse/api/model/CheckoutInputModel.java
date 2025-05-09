package com.solohub.teste_pulse.api.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CheckoutInputModel {
    @NotNull
    private Long carrinhoId;
    @NotNull
    private Long enderecoId;
    @NotNull
    private Long transportadoraId;
    @NotNull
    private String formaPagamento;
}
