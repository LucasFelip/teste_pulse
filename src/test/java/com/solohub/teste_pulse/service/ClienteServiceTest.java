package com.solohub.teste_pulse.service;

import com.solohub.teste_pulse.domain.exceptions.ResourceNotFoundException;
import com.solohub.teste_pulse.domain.model.Cliente;
import com.solohub.teste_pulse.domain.repository.ClienteRepository;
import com.solohub.teste_pulse.domain.service.ClienteService;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
public class ClienteServiceTest {
    @Mock private ClienteRepository clienteRepository;
    @InjectMocks private ClienteService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void criar_quandoChamaSave_retornaClienteSalvo() {
        Cliente toSave = Cliente.builder().nome("lucas").email("lucas@example.com").build();
        Cliente saved = Cliente.builder().id(1L).nome("lucas").email("lucas@example.com").build();
        when(clienteRepository.save(toSave)).thenReturn(saved);

        Cliente result = service.criar(toSave);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("lucas", result.getNome());
        verify(clienteRepository).save(toSave);
    }

    @Test
    void buscarPorId_quandoExiste_retornaOptionalComCliente() {
        Long id = 2L;
        Cliente cliente = Cliente.builder().id(id).nome("ferreira").email("ferreira@example.com").build();
        when(clienteRepository.findById(id)).thenReturn(Optional.of(cliente));

        Optional<Cliente> result = service.buscarPorId(id);

        assertTrue(result.isPresent());
        assertEquals(cliente, result.get());
    }

    @Test
    void buscarPorId_quandoNaoExiste_lancaResourceNotFound() {
        when(clienteRepository.findById(5L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.buscarPorId(5L));
    }

    @Test
    void listarTodos_retornaListaDeClientes() {
        List<Cliente> list = Arrays.asList(
                Cliente.builder().id(1L).nome("lucas").build(),
                Cliente.builder().id(2L).nome("ferreira").build()
        );
        when(clienteRepository.findAll()).thenReturn(list);

        List<Cliente> result = service.listarTodos();

        assertEquals(2, result.size());
        assertEquals("lucas", result.get(0).getNome());
        verify(clienteRepository).findAll();
    }
}
