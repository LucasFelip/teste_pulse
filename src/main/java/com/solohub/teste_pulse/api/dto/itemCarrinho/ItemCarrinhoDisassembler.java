package com.solohub.teste_pulse.api.dto.itemCarrinho;

import com.solohub.teste_pulse.domain.model.ItemCarrinho;

public class ItemCarrinhoDisassembler {
    public ItemCarrinho toDomain(ItemCarrinhoDTO dto) {
        return ItemCarrinho.builder()
                .id(dto.getId())
                .quantidade(dto.getQuantidade())
                .precoUnitario(dto.getPrecoUnitario())
                .build();
    }
}
