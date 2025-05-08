package com.solohub.teste_pulse.api.dto.endereco;

import com.solohub.teste_pulse.domain.model.Endereco;

public class EnderecoAssembler {
    public EnderecoDTO toDto(Endereco endereco) {
        return EnderecoDTO.builder()
                .id(endereco.getId())
                .rua(endereco.getRua())
                .numero(endereco.getNumero())
                .complemento(endereco.getComplemento())
                .bairro(endereco.getBairro())
                .cidade(endereco.getCidade())
                .estado(endereco.getEstado())
                .cep(endereco.getCep())
                .pais(endereco.getPais())
                .isPrincipal(endereco.getIsPrincipal())
                .build();
    }
}
