package com.solohub.teste_pulse.domain.service;

import com.solohub.teste_pulse.domain.exceptions.InvalidQuantityException;
import com.solohub.teste_pulse.domain.exceptions.ResourceNotFoundException;
import com.solohub.teste_pulse.domain.model.Carrinho;
import com.solohub.teste_pulse.domain.model.Cliente;
import com.solohub.teste_pulse.domain.model.ItemCarrinho;
import com.solohub.teste_pulse.domain.model.Produto;
import com.solohub.teste_pulse.domain.model.enums.CartStatus;
import com.solohub.teste_pulse.domain.repository.CarrinhoRepository;
import com.solohub.teste_pulse.domain.repository.ClienteRepository;
import com.solohub.teste_pulse.domain.repository.ProdutoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CarrinhoService {
    private final CarrinhoRepository carrinhoRepository;
    private final ClienteRepository clienteRepository;
    private final ProdutoRepository produtoRepository;

    public Carrinho criarCarrinho(Long clienteId) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente", clienteId));

        buscarCarrinhoAberto(clienteId).ifPresent(carrinhoAberto -> {
            carrinhoAberto.setStatus(CartStatus.ENCERRADO);
            carrinhoRepository.save(carrinhoAberto);
        });

        Carrinho carrinho = Carrinho.builder()
                .cliente(cliente)
                .status(CartStatus.ABERTO)
                .build();
        return carrinhoRepository.save(carrinho);
    }

    @Transactional
    public Carrinho adicionarItemCarrinho(Long carrinhoId, Long produtoId, int quantidade) {
        Carrinho carrinho = carrinhoRepository.findById(carrinhoId)
                .orElseThrow(() -> new ResourceNotFoundException("Carrinho",carrinhoId));
        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new ResourceNotFoundException("Produto",produtoId));
        if (quantidade <= 0) {
            throw new InvalidQuantityException(quantidade);
        }
        ItemCarrinho itemCarrinho = ItemCarrinho.builder()
                .carrinho(carrinho)
                .produto(produto)
                .quantidade(quantidade)
                .precoUnitario(produto.getPrecoUnitario())
                .build();
        carrinho.addItem(itemCarrinho);
        return carrinhoRepository.save(carrinho);
    }

    public Optional<Carrinho> buscarCarrinhoAberto(Long clienteId) {
        return carrinhoRepository.findByClienteIdAndStatus(clienteId, CartStatus.ABERTO);
    }
}
