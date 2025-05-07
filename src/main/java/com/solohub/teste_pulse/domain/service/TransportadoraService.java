package com.solohub.teste_pulse.domain.service;

import com.solohub.teste_pulse.domain.model.Transportadora;
import com.solohub.teste_pulse.domain.repository.TransportadoraRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransportadoraService {
    private final TransportadoraRepository transportadoraRepository;

    public List<Transportadora> listarTodas() {
        return transportadoraRepository.findAll();
    }
}
