package com.solohub.teste_pulse.domain.model;

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
public class Pedido {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @ManyToOne(optional = false)
    @JoinColumn(name = "carrinho_entrega_id")
    private Endereco enderecoEntrega;

    @ManyToOne(optional = false)
    @JoinColumn(name = "transportadora_id")
    private Transportadora transportadora;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ItemPedido> itens = new ArrayList<>();

    private BigDecimal frete;

    @Enumerated(EnumType.STRING)
    private FormaPagamento formaPagamento;

    private BigDecimal valorTotal;

    @CreationTimestamp
    private OffsetDateTime dataPedido;

    @Enumerated(EnumType.STRING)
    private PedidoStatus status = PedidoStatus.PENDENTE;

    public static Pedido fromCarrinho(Carrinho carrinho, Endereco endereco, Transportadora transportadora, FormaPagamento formaPagamento) {
        BigDecimal frete = transportadora.getFreteFixo();
        BigDecimal totalItens = carrinho.calcularTotal();

        List<ItemPedido> itemPedidos = new ArrayList<>();
        carrinho.getItens().forEach(item -> {
            var ip = ItemPedido.builder()
                    .produto(item.getProduto())
                    .quantidade(item.getQuantidade())
                    .precoUnitario(item.getPrecoUnitario())
                    .build();
            ip.setPedido(null);
            itemPedidos.add(ip);
        });
        var pedido = Pedido.builder()
                .cliente(carrinho.getCliente())
                .enderecoEntrega(endereco)
                .frete(frete)
                .transportadora(transportadora)
                .formaPagamento(formaPagamento)
                .valorTotal(totalItens.add(frete))
                .build();
        itemPedidos.forEach(ip -> {
            ip.setPedido(pedido);
            pedido.getItens().add(ip);
        });
        pedido.setStatus(PedidoStatus.PAGO);
        return pedido;
    }
}