package com.solohub.teste_pulse.domain.repository;

import com.solohub.teste_pulse.domain.model.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnderecoRepository extends JpaRepository<Endereco, Long> {
    List<Endereco> findByClienteId(Long clienteId);
    Optional<Endereco> findByClienteIdAndIsPrincipalTrue(Long clienteId);
}
