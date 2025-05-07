package com.solohub.teste_pulse.domain.repository;

import com.solohub.teste_pulse.domain.model.Carrinho;
import com.solohub.teste_pulse.domain.model.enums.CartStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarrinhoRepository extends JpaRepository<Carrinho, Long> {
    Optional<Carrinho> findByClienteIdAndStatus(Long clienteId, CartStatus status);
}
