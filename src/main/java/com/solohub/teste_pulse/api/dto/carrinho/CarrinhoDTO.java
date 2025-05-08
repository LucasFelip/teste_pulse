package com.solohub.teste_pulse.api.dto.carrinho;

import com.solohub.teste_pulse.api.dto.itemCarrinho.ItemCarrinhoDTO;
import com.solohub.teste_pulse.domain.model.enums.CartStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarrinhoDTO {
    private Long id;
    private Long clienteId;
    private List<ItemCarrinhoDTO> itens = new ArrayList<>();
    private CartStatus status;
}
