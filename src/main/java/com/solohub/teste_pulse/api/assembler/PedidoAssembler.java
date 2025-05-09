package com.solohub.teste_pulse.api.assembler;

import com.solohub.teste_pulse.api.controller.CheckoutController;
import com.solohub.teste_pulse.api.model.PedidoModel;
import com.solohub.teste_pulse.domain.model.Pedido;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class PedidoAssembler extends BaseAssembler<Pedido, PedidoModel, CheckoutController> {
    public PedidoAssembler(ModelMapper mapper) {
        super(mapper, CheckoutController.class, PedidoModel.class);
    }
}
