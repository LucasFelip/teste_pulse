package com.solohub.teste_pulse.api.dto.transportadora;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransportadoraDTO {
    private Long id;
    private String nome;
    private BigDecimal freteFixo;
}
