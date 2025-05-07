package com.solohub.teste_pulse.service;


import com.solohub.teste_pulse.domain.exceptions.ResourceNotFoundException;
import com.solohub.teste_pulse.domain.model.Carrinho;
import com.solohub.teste_pulse.domain.model.Cliente;
import com.solohub.teste_pulse.domain.model.Endereco;
import com.solohub.teste_pulse.domain.model.Pedido;
import com.solohub.teste_pulse.domain.model.Transportadora;
import com.solohub.teste_pulse.domain.model.enums.CartStatus;
import com.solohub.teste_pulse.domain.model.enums.FormaPagamento;
import com.solohub.teste_pulse.domain.repository.CarrinhoRepository;
import com.solohub.teste_pulse.domain.repository.EnderecoRepository;
import com.solohub.teste_pulse.domain.repository.PedidoRepository;
import com.solohub.teste_pulse.domain.repository.TransportadoraRepository;
import com.solohub.teste_pulse.domain.service.CheckoutService;
import com.solohub.teste_pulse.domain.service.FaturamentoService;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
public class CheckoutServiceTest {
    @Mock private CarrinhoRepository carrinhoRepository;
    @Mock private PedidoRepository pedidoRepository;
    @Mock private EnderecoRepository enderecoRepository;
    @Mock private TransportadoraRepository transportadoraRepository;
    @Mock private FaturamentoService faturamentoService;
    @InjectMocks private CheckoutService service;

    private Long clienteId = 1L;
    private Long carrinhoId = 2L;
    private Long enderecoId = 3L;
    private Long transportadoraId = 4L;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void checkout_quandoTudoValido_retornaPedidoEAlteraCarrinho(){
        Cliente cliente = Cliente.builder().id(clienteId).build();
        Carrinho carrinho = Carrinho.builder()
                .id(carrinhoId)
                .cliente(cliente)
                .status(CartStatus.ABERTO)
                .itens(new java.util.ArrayList<>())
                .build();
        Endereco endereco = Endereco.builder().id(enderecoId).build();
        Transportadora transportadora = Transportadora.builder()
                .id(transportadoraId)
                .freteFixo(BigDecimal.valueOf(5))
                .build();
        when(carrinhoRepository.findById(carrinhoId)).thenReturn(Optional.of(carrinho));
        when(enderecoRepository.findById(enderecoId)).thenReturn(Optional.of(endereco));
        when(transportadoraRepository.findById(transportadoraId)).thenReturn(Optional.of(transportadora));
        when(pedidoRepository.save(any(Pedido.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Pedido result = service.checkout(
                carrinhoId,
                enderecoId,
                transportadoraId,
                FormaPagamento.PIX
        );

        assertNotNull(result);
        assertEquals(endereco, result.getEnderecoEntrega());
        assertEquals(transportadora, result.getTransportadora());
        assertEquals(FormaPagamento.PIX, result.getFormaPagamento());

        verify(faturamentoService).emitirNotaFiscal(result);
        assertEquals(CartStatus.ENCERRADO, carrinho.getStatus());
        verify(carrinhoRepository).save(carrinho);
    }

    @Test
    void checkout_quandoCarrinhoNaoEncontrado_lancaResourceNotFound() {
        when(carrinhoRepository.findById(carrinhoId)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () ->
                service.checkout(carrinhoId, enderecoId, transportadoraId, FormaPagamento.CARTAO)
        );
    }

    @Test
    void checkout_quandoEnderecoNaoEncontrado_lancaResourceNotFound() {
        Carrinho carrinho = Carrinho.builder().id(carrinhoId).itens(new java.util.ArrayList<>()).build();
        when(carrinhoRepository.findById(carrinhoId)).thenReturn(Optional.of(carrinho));
        when(enderecoRepository.findById(enderecoId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                service.checkout(carrinhoId, enderecoId, transportadoraId, FormaPagamento.CARTAO)
        );
    }

    @Test
    void checkout_quandoTransportadoraNaoEncontrada_lancaResourceNotFound() {
        Carrinho carrinho = Carrinho.builder().id(carrinhoId).itens(new java.util.ArrayList<>()).build();
        Endereco endereco = Endereco.builder().id(enderecoId).build();

        when(carrinhoRepository.findById(carrinhoId)).thenReturn(Optional.of(carrinho));
        when(enderecoRepository.findById(enderecoId)).thenReturn(Optional.of(endereco));
        when(transportadoraRepository.findById(transportadoraId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                service.checkout(carrinhoId, enderecoId, transportadoraId, FormaPagamento.CARTAO)
        );
    }
}
