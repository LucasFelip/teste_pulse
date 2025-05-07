package com.solohub.teste_pulse.service;

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
import com.solohub.teste_pulse.domain.service.CarrinhoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
public class CarrinhoServiceTest {
    @Mock
    private CarrinhoRepository carrinhoRepository;
    @Mock
    private ClienteRepository clienteRepository;
    @Mock
    private ProdutoRepository produtoRepository;
    @InjectMocks
    private CarrinhoService service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void criarCarrinho_quandoClienteExiste_retornaCarrinho() {
        Long clienteId = 1L;
        Cliente cliente = Cliente.builder().id(clienteId).build();
        when(clienteRepository.findById(clienteId)).thenReturn(Optional.of(cliente));
        Carrinho saved = Carrinho.builder().id(2L).cliente(cliente).status(CartStatus.ABERTO).build();
        when(carrinhoRepository.save(any())).thenReturn(saved);

        Carrinho result = service.criarCarrinho(clienteId);

        assertNotNull(result);
        assertEquals(2L, result.getId());
        assertEquals(CartStatus.ABERTO, result.getStatus());
        verify(carrinhoRepository).save(any(Carrinho.class));
    }

    @Test
    void criarCarrinho_quandoClienteNaoExiste_lancaResourceNotFound() {
        when(clienteRepository.findById(5L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,
                () -> service.criarCarrinho(5L));
    }

    @Test
    void adicionarItemCarrinho_quandoTudoValido_adicionaItem() {
        Long carrinhoId = 10L, produtoId = 20L;
        Carrinho carrinho = Carrinho.builder().id(carrinhoId).build();
        Produto produto = Produto.builder().id(produtoId).precoUnitario(BigDecimal.valueOf(15)).build();
        when(carrinhoRepository.findById(carrinhoId)).thenReturn(Optional.of(carrinho));
        when(produtoRepository.findById(produtoId)).thenReturn(Optional.of(produto));
        when(carrinhoRepository.save(any())).thenReturn(carrinho);

        Carrinho result = service.adicionarItemCarrinho(carrinhoId, produtoId, 2);

        assertEquals(1, result.getItens().size());
        ItemCarrinho item = result.getItens().get(0);
        assertEquals(produto, item.getProduto());
        assertEquals(2, item.getQuantidade());
        assertEquals(BigDecimal.valueOf(15), item.getPrecoUnitario());
    }

    @Test
    void adicionarItemCarrinho_quandoQuantidadeInvalida_lancaInvalidQuantity() {
        Long carrinhoId = 1L, produtoId = 1L;
        Carrinho carrinho = Carrinho.builder().id(carrinhoId).build();
        when(carrinhoRepository.findById(carrinhoId)).thenReturn(Optional.of(carrinho));
        when(produtoRepository.findById(produtoId))
                .thenReturn(Optional.of(Produto.builder().id(produtoId).precoUnitario(BigDecimal.ONE).build()));

        assertThrows(InvalidQuantityException.class,
                () -> service.adicionarItemCarrinho(carrinhoId, produtoId, 0));
    }

    @Test
    void adicionarItemCarrinho_quandoCarrinhoNaoExiste_lancaResourceNotFound() {
        when(carrinhoRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,
                () -> service.adicionarItemCarrinho(99L, 1L, 1));
    }

    @Test
    void buscarCarrinhoAberto_retornaOptional() {
        when(carrinhoRepository.findByClienteIdAndStatus(3L, CartStatus.ABERTO))
                .thenReturn(Optional.of(new Carrinho()));
        Optional<Carrinho> result = service.buscarCarrinhoAberto(3L);
        assertTrue(result.isPresent());
    }
}
