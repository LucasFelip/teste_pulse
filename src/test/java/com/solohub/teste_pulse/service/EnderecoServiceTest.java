package com.solohub.teste_pulse.service;

import com.solohub.teste_pulse.domain.exceptions.ResourceNotFoundException;
import com.solohub.teste_pulse.domain.model.Cliente;
import com.solohub.teste_pulse.domain.model.Endereco;
import com.solohub.teste_pulse.domain.repository.ClienteRepository;
import com.solohub.teste_pulse.domain.repository.EnderecoRepository;
import com.solohub.teste_pulse.domain.service.EnderecoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
public class EnderecoServiceTest {
    @Mock private EnderecoRepository enderecoRepository;
    @Mock private ClienteRepository clienteRepository;
    @InjectMocks private EnderecoService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void criar_quandoClienteExiste_salvaEndereco() {
        Long clienteId = 1L;
        Cliente cliente = Cliente.builder().id(clienteId).build();
        Endereco toSave = Endereco.builder().rua("Rua A").build();
        Endereco saved = Endereco.builder().id(10L).rua("Rua A").build();

        when(clienteRepository.findById(clienteId)).thenReturn(Optional.of(cliente));
        when(enderecoRepository.save(any(Endereco.class))).thenReturn(saved);

        Endereco result = service.criar(clienteId, toSave);

        assertNotNull(result);
        assertEquals(10L, result.getId());
        verify(enderecoRepository).save(toSave);
        assertEquals(cliente, toSave.getCliente());
    }

    @Test
    void criar_quandoClienteNaoExiste_lancaResourceNotFound() {
        Long clienteId = 2L;
        Endereco toSave = Endereco.builder().rua("Rua B").build();
        when(clienteRepository.findById(clienteId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.criar(clienteId, toSave));
    }

    @Test
    void listarPorCliente_retornaLista() {
        Long clienteId = 3L;
        List<Endereco> list = Arrays.asList(
                Endereco.builder().id(1L).rua("Rua1").build(),
                Endereco.builder().id(2L).rua("Rua2").build()
        );
        when(enderecoRepository.findByClienteId(clienteId)).thenReturn(list);

        List<Endereco> result = service.listarPorCliente(clienteId);

        assertEquals(2, result.size());
        assertEquals("Rua1", result.get(0).getRua());
        verify(enderecoRepository).findByClienteId(clienteId);
    }

    @Test
    void buscarPrincipal_retornaOptional() {
        Long clienteId = 4L;
        Endereco principal = Endereco.builder().id(5L).rua("RuaP").build();
        when(enderecoRepository.findByClienteIdAndIsPrincipalTrue(clienteId))
                .thenReturn(Optional.of(principal));

        Optional<Endereco> result = service.buscarPrincipal(clienteId);

        assertTrue(result.isPresent());
        assertEquals(principal, result.get());
    }

    @Test
    void deletar_chamaDeleteById() {
        Long enderecoId = 6L;
        doNothing().when(enderecoRepository).deleteById(enderecoId);

        service.deletar(enderecoId);

        verify(enderecoRepository).deleteById(enderecoId);
    }
}
