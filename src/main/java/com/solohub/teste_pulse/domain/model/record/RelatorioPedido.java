package com.solohub.teste_pulse.domain.model.record;

import com.solohub.teste_pulse.domain.model.NotaFiscal;
import com.solohub.teste_pulse.domain.model.Pedido;

public record RelatorioPedido(
        Pedido pedido,
        NotaFiscal notaFiscal
) {}
