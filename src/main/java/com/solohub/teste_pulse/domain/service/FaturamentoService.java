package com.solohub.teste_pulse.domain.service;

import com.solohub.teste_pulse.domain.model.NotaFiscal;
import com.solohub.teste_pulse.domain.model.Pedido;
import com.solohub.teste_pulse.domain.repository.NotaFiscalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FaturamentoService {
    private final NotaFiscalRepository notaFiscalRepository;

    public NotaFiscal emitirNotaFiscal(Pedido pedido) {
        NotaFiscal notaFiscal = NotaFiscal.builder()
                .pedido(pedido)
                .jsonNota("{ 'pedido': " + pedido.getId() + "\n" +
                        " 'valor do pedido': " + pedido.getValorTotal() +"\n" +
                        " 'data do pedido': " + pedido.getDataPedido() +"\n }")
                .build();
        return notaFiscalRepository.save(notaFiscal);
    }
}
