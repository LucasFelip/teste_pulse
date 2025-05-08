package com.solohub.teste_pulse.api.dto.itemCarrinho;

import com.solohub.teste_pulse.domain.model.ItemCarrinho;

public class ItemCarrinhoAssembler {
    public ItemCarrinhoDTO toDto(ItemCarrinho ic) {
        return ItemCarrinhoDTO.builder()
                .id(ic.getId())
                .produtoId(ic.getProduto().getId())
                .quantidade(ic.getQuantidade())
                .precoUnitario(ic.getPrecoUnitario())
                .build();
    }
}
