package com.solohub.teste_pulse.api.model;

import com.solohub.teste_pulse.domain.model.enums.CartStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode (callSuper = true,onlyExplicitlyIncluded = true)
public class CarrinhoModel extends AbstractModel {
    private List<ItemCarrinhoModel> itens = new ArrayList<>();
    private CartStatus status;
}
