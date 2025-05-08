package com.solohub.teste_pulse.api.dto.cliente;

import com.solohub.teste_pulse.domain.model.Cliente;

public class ClienteAssembler {
    public ClienteDTO toDto(Cliente cliente) {
        return ClienteDTO.builder()
                .id(cliente.getId())
                .nome(cliente.getNome())
                .email(cliente.getEmail())
                .build();
    }
}
