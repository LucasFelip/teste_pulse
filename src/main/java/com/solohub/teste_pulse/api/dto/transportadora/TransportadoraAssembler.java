package com.solohub.teste_pulse.api.dto.transportadora;

import com.solohub.teste_pulse.domain.model.Transportadora;

public class TransportadoraAssembler {
    public TransportadoraDTO toDto(Transportadora t) {
        return TransportadoraDTO.builder()
                .id(t.getId())
                .nome(t.getNome())
                .freteFixo(t.getFreteFixo())
                .build();
    }
}
