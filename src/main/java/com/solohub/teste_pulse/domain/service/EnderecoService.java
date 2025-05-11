package com.solohub.teste_pulse.domain.service;

import com.solohub.teste_pulse.domain.exceptions.ResourceNotFoundException;
import com.solohub.teste_pulse.domain.model.Cliente;
import com.solohub.teste_pulse.domain.model.Endereco;
import com.solohub.teste_pulse.domain.repository.ClienteRepository;
import com.solohub.teste_pulse.domain.repository.EnderecoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EnderecoService {
    private final EnderecoRepository enderecoRepository;
    private final ClienteRepository clienteRepository;

    public Endereco criar(Long clienteId, Endereco endereco) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente",clienteId));

        buscarPrincipal(clienteId).ifPresent(enderecoPrincipal -> {
            enderecoPrincipal.setIsPrincipal(false);
            enderecoRepository.save(enderecoPrincipal);
        });

        endereco.setCliente(cliente);
        return enderecoRepository.save(endereco);
    }

    public List<Endereco> listarPorCliente(Long clienteId) {
        return enderecoRepository.findByClienteId(clienteId);
    }

    public Optional<Endereco> buscarPrincipal(Long clienteId) {
        return enderecoRepository.findByClienteIdAndIsPrincipalTrue(clienteId);
    }

    public void deletar(Long id) {
        enderecoRepository.deleteById(id);
    }
}
