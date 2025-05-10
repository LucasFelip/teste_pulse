package com.solohub.teste_pulse.domain.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.solohub.teste_pulse.domain.model.enums.FormaPagamento;
import com.solohub.teste_pulse.domain.model.enums.PedidoStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pedidos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"cliente", "enderecoEntrega", "transportadora", "itens"})
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id",
        scope = Pedido.class)
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @ManyToOne(optional = false)
    @JoinColumn(name = "carrinho_entrega_id", nullable = false)
    private Endereco enderecoEntrega;

    @ManyToOne(optional = false)
    @JoinColumn(name = "transportadora_id", nullable = false)
    private Transportadora transportadora;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ItemPedido> itens = new ArrayList<>();

    private BigDecimal frete;

    @Enumerated(EnumType.STRING)
    private com.solohub.teste_pulse.domain.model.enums.FormaPagamento formaPagamento;

    private BigDecimal valorTotal;

    @CreationTimestamp
    private OffsetDateTime dataPedido;

    @Enumerated(EnumType.STRING)
    private com.solohub.teste_pulse.domain.model.enums.PedidoStatus status;

    public static Pedido fromCarrinho(Carrinho carrinho, Endereco endereco, Transportadora transportadora, com.solohub.teste_pulse.domain.model.enums.FormaPagamento formaPagamento) {
        BigDecimal frete = transportadora.getFreteFixo();
        BigDecimal totalItens = carrinho.calcularTotal();

        Pedido pedido = Pedido.builder()
                .cliente(carrinho.getCliente())
                .enderecoEntrega(endereco)
                .transportadora(transportadora)
                .frete(frete)
                .formaPagamento(formaPagamento)
                .valorTotal(totalItens.add(frete))
                .status(com.solohub.teste_pulse.domain.model.enums.PedidoStatus.PENDENTE)
                .build();

        carrinho.getItens().forEach(item -> {
            ItemPedido ip = ItemPedido.builder()
                    .produto(item.getProduto())
                    .quantidade(item.getQuantidade())
                    .precoUnitario(item.getPrecoUnitario())
                    .build();
            ip.setPedido(pedido);
            pedido.getItens().add(ip);
        });

        return pedido;
    }
}