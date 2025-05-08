package com.solohub.teste_pulse.api.dto.notaFiscal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotaFiscalDTO {
    private Long id;
    private Long pedidoId;
    private String numero;
    private OffsetDateTime dataEmissao;
    private String jsonNota;
}
