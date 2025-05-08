package com.solohub.teste_pulse.api.dto.relatorio;

import com.solohub.teste_pulse.domain.model.record.RelatorioPedido;

public class RelatorioAssembler {
    public RelatorioPedidoDTO toDto(RelatorioPedido rp) {
        return RelatorioPedidoDTO.builder()
                .pedidoId(rp.pedido().getId())
                .notaFiscalId(rp.notaFiscal().getId())
                .numeroNota(rp.notaFiscal().getNumero())
                .dataEmissao(rp.notaFiscal().getDataEmissao())
                .valorTotal(rp.pedido().getValorTotal())
                .frete(rp.pedido().getFrete())
                .build();
    }
}
