package com.solohub.teste_pulse.service;

import com.solohub.teste_pulse.domain.exceptions.ResourceNotFoundException;
import com.solohub.teste_pulse.domain.model.Produto;
import com.solohub.teste_pulse.domain.repository.ProdutoRepository;
import com.solohub.teste_pulse.domain.service.ProdutoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
public class ProdutoServiceTest {
    @Mock private ProdutoRepository produtoRepository;
    @InjectMocks private ProdutoService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void criar_quandoChamaSave_retornaProdutoSalvo() {
        Produto toSave = Produto.builder()
                .nome("Item A")
                .descricao("Desc")
                .precoUnitario(BigDecimal.valueOf(10))
                .ativo(true)
                .build();
        Produto saved = Produto.builder()
                .id(1L)
                .nome("Item A")
                .descricao("Desc")
                .precoUnitario(BigDecimal.valueOf(10))
                .ativo(true)
                .build();
        when(produtoRepository.save(toSave)).thenReturn(saved);

        Produto result = service.criar(toSave);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(produtoRepository).save(toSave);
    }

    @Test
    void buscarPorId_quandoExiste_retornaOptionalComProduto() {
        Long id = 2L;
        Produto produto = Produto.builder()
                .id(id)
                .nome("Item B")
                .build();
        when(produtoRepository.findById(id)).thenReturn(Optional.of(produto));

        Optional<Produto> result = service.buscarPorId(id);

        assertTrue(result.isPresent());
        assertEquals(produto, result.get());
    }

    @Test
    void buscarPorId_quandoNaoExiste_lancaResourceNotFound() {
        when(produtoRepository.findById(5L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.buscarPorId(5L));
    }

    @Test
    void listarAtivos_retornaApenasProdutosAtivos() {
        List<Produto> list = Arrays.asList(
                Produto.builder().id(1L).ativo(true).build(),
                Produto.builder().id(2L).ativo(true).build()
        );
        when(produtoRepository.findByAtivoTrue()).thenReturn(list);

        List<Produto> result = service.listarAtivos();

        assertEquals(2, result.size());
        verify(produtoRepository).findByAtivoTrue();
    }

    @Test
    void deletar_chamaDeleteById() {
        Long id = 3L;
        doNothing().when(produtoRepository).deleteById(id);

        service.deletar(id);

        verify(produtoRepository).deleteById(id);
    }
}
