package com.solohub.teste_pulse.api.dto.produto;

import com.solohub.teste_pulse.domain.model.Produto;

public class ProdutoAssembler {
    public ProdutoDTO toDto(Produto produto) {
        return ProdutoDTO.builder()
                .id(produto.getId())
                .nome(produto.getNome())
                .descricao(produto.getDescricao())
                .precoUnitario(produto.getPrecoUnitario())
                .estoque(produto.getEstoque())
                .ativo(produto.getAtivo())
                .build();
    }
}
