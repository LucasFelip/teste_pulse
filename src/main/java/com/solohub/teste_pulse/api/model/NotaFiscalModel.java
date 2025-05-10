package com.solohub.teste_pulse.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true,onlyExplicitlyIncluded = true)
public class NotaFiscalModel extends AbstractModel{
    private String numero;
    private OffsetDateTime dataEmissao;
    private String jsonNota;
}
