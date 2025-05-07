package com.solohub.teste_pulse.domain.service;

import com.solohub.teste_pulse.domain.exceptions.ResourceNotFoundException;
import com.solohub.teste_pulse.domain.model.Pedido;
import com.solohub.teste_pulse.domain.model.enums.CartStatus;
import com.solohub.teste_pulse.domain.model.enums.FormaPagamento;
import com.solohub.teste_pulse.domain.repository.CarrinhoRepository;
import com.solohub.teste_pulse.domain.repository.EnderecoRepository;
import com.solohub.teste_pulse.domain.repository.PedidoRepository;
import com.solohub.teste_pulse.domain.repository.TransportadoraRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CheckoutService {
    private final CarrinhoRepository carrinhoRepository;
    private final PedidoRepository pedidoRepository;
    private final EnderecoRepository enderecoRepository;
    private final TransportadoraRepository transportadoraRepository;
    private final FaturamentoService faturamentoService;

    @Transactional
    public Pedido checkout (Long clienteId, Long carrinhoId, Long enderecoId, Long transportadoraId, FormaPagamento formaPagamento) {
        var carrinho = carrinhoRepository.findById(carrinhoId)
                .orElseThrow(() -> new ResourceNotFoundException("Carrinho",carrinhoId));
        var endereco = enderecoRepository.findById(enderecoId)
                .orElseThrow(() -> new ResourceNotFoundException("Endereco",enderecoId));
        var transportadora = transportadoraRepository.findById(transportadoraId)
                .orElseThrow(() -> new ResourceNotFoundException("Transportadora",transportadoraId));

        Pedido pedido = Pedido.fromCarrinho(carrinho, endereco, transportadora,formaPagamento);
        pedidoRepository.save(pedido);

        faturamentoService.emitirNotaFiscal(pedido);

        carrinho.setStatus(CartStatus.ENCERRADO);
        carrinhoRepository.save(carrinho);

        return pedido;
    }
}
