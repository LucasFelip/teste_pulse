package com.solohub.teste_pulse.api.dto.produto;

import com.solohub.teste_pulse.domain.model.Produto;

public class ProdutoDisassembler {
    public Produto toDomain(ProdutoDTO dto) {
        return Produto.builder()
                .id(dto.getId())
                .nome(dto.getNome())
                .descricao(dto.getDescricao())
                .precoUnitario(dto.getPrecoUnitario())
                .estoque(dto.getEstoque())
                .ativo(dto.getAtivo())
                .build();
    }
}
