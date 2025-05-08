package com.solohub.teste_pulse.api.dto.pedido;

import com.solohub.teste_pulse.api.dto.itemPedido.ItemPedidoDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PedidoDTO {
    private Long id;
    private Long clienteId;
    private Long enderecoId;
    private Long transportadoraId;
    private String formaPagamento;
    private List<ItemPedidoDTO> itens = new ArrayList<>();
    private BigDecimal frete;
    private BigDecimal valorTotal;
    private String status;
    private OffsetDateTime dataPedido;
}
