package com.solohub.teste_pulse.api.dto.pedido;

import com.solohub.teste_pulse.api.dto.itemPedido.ItemPedidoAssembler;
import com.solohub.teste_pulse.domain.model.Pedido;

public class PedidoAssembler {
    private final ItemPedidoAssembler itemAsm = new ItemPedidoAssembler();

    public PedidoDTO toDto(Pedido p) {
        var itensDto = p.getItens().stream().map(itemAsm::toDto).toList();
        return PedidoDTO.builder()
                .id(p.getId())
                .clienteId(p.getCliente().getId())
                .enderecoId(p.getEnderecoEntrega().getId())
                .transportadoraId(p.getTransportadora().getId())
                .formaPagamento(p.getFormaPagamento().name())
                .itens(itensDto)
                .frete(p.getFrete())
                .valorTotal(p.getValorTotal())
                .status(p.getStatus().name())
                .dataPedido(p.getDataPedido())
                .build();
    }
}