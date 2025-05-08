package com.solohub.teste_pulse.api.dto.cliente;

import com.solohub.teste_pulse.domain.model.Cliente;

public class ClienteDisassembler {
    public Cliente toDomain(ClienteDTO dto) {
        return Cliente.builder()
                .id(dto.getId())
                .nome(dto.getNome())
                .email(dto.getEmail())
                .build();
    }
}
