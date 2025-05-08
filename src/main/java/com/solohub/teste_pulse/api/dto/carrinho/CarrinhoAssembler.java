package com.solohub.teste_pulse.api.dto.carrinho;

import com.solohub.teste_pulse.api.dto.itemCarrinho.ItemCarrinhoAssembler;
import com.solohub.teste_pulse.api.dto.itemCarrinho.ItemCarrinhoDTO;
import com.solohub.teste_pulse.domain.model.Carrinho;

import java.util.List;

public class CarrinhoAssembler {
    private final ItemCarrinhoAssembler itemAsm = new ItemCarrinhoAssembler();

    public CarrinhoDTO toDto(Carrinho c) {
        List<ItemCarrinhoDTO> itensDto = c.getItens().stream()
                .map(itemAsm::toDto)
                .toList();
        return CarrinhoDTO.builder()
                .id(c.getId())
                .clienteId(c.getCliente().getId())
                .itens(itensDto)
                .status(c.getStatus())
                .build();
    }
}
