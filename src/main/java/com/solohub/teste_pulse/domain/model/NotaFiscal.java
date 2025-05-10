package com.solohub.teste_pulse.domain.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "notas_fiscais")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"pedido"})
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id",
        scope = NotaFiscal.class)
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
public class NotaFiscal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "pedido_id", unique = true, nullable = false)
    private Pedido pedido;

    @Column(unique = true, nullable = false)
    private String numero;

    @CreationTimestamp
    private OffsetDateTime dataEmissao;
    
    private String jsonNota;

    @PrePersist
    private void generateNumero() {
        if (this.numero == null) {
            this.numero = UUID.randomUUID().toString();
        }
    }
}
