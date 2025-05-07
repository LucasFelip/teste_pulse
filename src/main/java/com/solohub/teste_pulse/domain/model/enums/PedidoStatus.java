package com.solohub.teste_pulse.domain.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PedidoStatus {
    PENDENTE,
    PAGO,
    CANCELADO,
}
