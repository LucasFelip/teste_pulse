package com.solohub.teste_pulse.domain.repository;

import com.solohub.teste_pulse.domain.model.NotaFiscal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NotaFiscalRepository extends JpaRepository<NotaFiscal, Long> {
    Optional<NotaFiscal> findByPedidoId(Long pedidoId);
}

