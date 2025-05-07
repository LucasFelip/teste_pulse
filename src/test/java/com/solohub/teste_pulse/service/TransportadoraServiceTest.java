package com.solohub.teste_pulse.service;


import com.solohub.teste_pulse.domain.model.Transportadora;
import com.solohub.teste_pulse.domain.repository.TransportadoraRepository;
import com.solohub.teste_pulse.domain.service.TransportadoraService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
public class TransportadoraServiceTest {
    @Mock private TransportadoraRepository transportadoraRepository;
    @InjectMocks private TransportadoraService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void listarTodas_retornatodasTransportadoras() {
        List<Transportadora> list = Arrays.asList(
                Transportadora.builder().id(1L).nome("T1").freteFixo(BigDecimal.valueOf(10)).build(),
                Transportadora.builder().id(2L).nome("T2").freteFixo(BigDecimal.valueOf(20)).build()
        );
        when(transportadoraRepository.findAll()).thenReturn(list);

        List<Transportadora> result = service.listarTodas();

        assertEquals(2, result.size());
        assertEquals("T1", result.get(0).getNome());
        verify(transportadoraRepository).findAll();
    }
}
