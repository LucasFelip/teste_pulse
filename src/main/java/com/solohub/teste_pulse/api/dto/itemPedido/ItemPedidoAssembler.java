package com.solohub.teste_pulse.api.dto.itemPedido;

import com.solohub.teste_pulse.domain.model.ItemPedido;

public class ItemPedidoAssembler {
    public ItemPedidoDTO toDto(ItemPedido ip) {
        return ItemPedidoDTO.builder()
                .id(ip.getId())
                .produtoId(ip.getProduto().getId())
                .quantidade(ip.getQuantidade())
                .precoUnitario(ip.getPrecoUnitario())
                .build();
    }
}
