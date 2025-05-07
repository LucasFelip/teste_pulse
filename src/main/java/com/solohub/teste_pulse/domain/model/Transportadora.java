package com.solohub.teste_pulse.domain.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "transportadoras")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transportadora {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private BigDecimal freteFixo;

    @OneToMany(mappedBy = "transportadora", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Pedido> pedidos = new ArrayList<>();
}
