package com.solohub.teste_pulse.domain.repository;

import com.solohub.teste_pulse.domain.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, String> {
    List<Produto> findByAtivoTrue();
}
