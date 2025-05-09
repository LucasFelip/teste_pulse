package com.solohub.teste_pulse.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "notas_fiscais")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotaFiscal {
    @Id @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "pedido_id", unique = true)
    private Pedido pedido;

    @Column(unique = true, nullable = false)
    private String numero;

    @CreationTimestamp
    private OffsetDateTime dataEmissao;

    private String jsonNota;

    public void prePersist() {
        if (this.numero == null) {
            this.numero = UUID.randomUUID().toString();
        }
    }
}
