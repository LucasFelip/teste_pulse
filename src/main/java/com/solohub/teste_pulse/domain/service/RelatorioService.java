package com.solohub.teste_pulse.domain.service;

import com.solohub.teste_pulse.domain.exceptions.ResourceNotFoundException;
import com.solohub.teste_pulse.domain.model.NotaFiscal;
import com.solohub.teste_pulse.domain.model.Pedido;
import com.solohub.teste_pulse.domain.model.record.RelatorioPedido;
import com.solohub.teste_pulse.domain.repository.NotaFiscalRepository;
import com.solohub.teste_pulse.domain.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RelatorioService {
    private final PedidoRepository pedidoRepository;
    private final NotaFiscalRepository notaFiscalRepository;

    public List<RelatorioPedido> gerarRelatorioPorCliente(Long clienteId){
        List<Pedido> pedidos = pedidoRepository.findByClienteId(clienteId);
        if (pedidos.isEmpty()) {
            throw new ResourceNotFoundException("Pedidos",clienteId);
        }

        return pedidos.stream()
                .map(pedido -> {
                    NotaFiscal nota = notaFiscalRepository.findByPedidoId(pedido.getId())
                            .orElseThrow(() -> new ResourceNotFoundException("NotaFiscal do pedido", pedido.getId()));
                    return new RelatorioPedido(pedido, nota);
                })
                .collect(Collectors.toList());
    }
}