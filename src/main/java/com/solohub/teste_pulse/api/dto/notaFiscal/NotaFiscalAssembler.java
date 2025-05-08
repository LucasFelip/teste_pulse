package com.solohub.teste_pulse.api.dto.notaFiscal;

import com.solohub.teste_pulse.domain.model.NotaFiscal;

public class NotaFiscalAssembler {
    public NotaFiscalDTO toDto(NotaFiscal nf) {
        return NotaFiscalDTO.builder()
                .id(nf.getId())
                .pedidoId(nf.getPedido().getId())
                .numero(nf.getNumero())
                .dataEmissao(nf.getDataEmissao())
                .jsonNota(nf.getJsonNota())
                .build();
    }
}
