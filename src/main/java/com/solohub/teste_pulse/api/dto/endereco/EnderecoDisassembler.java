package com.solohub.teste_pulse.api.dto.endereco;

import  com.solohub.teste_pulse.domain.model.Endereco;

public class EnderecoDisassembler {
    public Endereco toDomain(EnderecoDTO dto) {
        return Endereco.builder()
                .id(dto.getId())
                .rua(dto.getRua())
                .numero(dto.getNumero())
                .complemento(dto.getComplemento())
                .bairro(dto.getBairro())
                .cidade(dto.getCidade())
                .estado(dto.getEstado())
                .cep(dto.getCep())
                .pais(dto.getPais())
                .isPrincipal(dto.getIsPrincipal())
                .build();
    }
}
