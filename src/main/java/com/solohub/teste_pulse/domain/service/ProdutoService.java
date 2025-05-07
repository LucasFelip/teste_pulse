package com.solohub.teste_pulse.domain.service;

import com.solohub.teste_pulse.domain.exceptions.ResourceNotFoundException;
import com.solohub.teste_pulse.domain.model.Produto;
import com.solohub.teste_pulse.domain.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProdutoService {
    private final ProdutoRepository produtoRepository;

    public Produto criar(Produto produto) {
        return produtoRepository.save(produto);
    }

    public Optional<Produto> buscarPorId(Long id) {
        var produto = produtoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto",id));
        return Optional.of(produto);
    }

    public List<Produto> listarAtivos() {
        return produtoRepository.findByAtivoTrue();
    }

    public void deletar(Long id) {
        produtoRepository.deleteById(id);
    }
}
