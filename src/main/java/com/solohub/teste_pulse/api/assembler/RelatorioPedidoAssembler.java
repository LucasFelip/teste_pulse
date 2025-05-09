package com.solohub.teste_pulse.api.assembler;

import com.solohub.teste_pulse.api.controller.RelatorioController;
import com.solohub.teste_pulse.api.model.RelatorioPedidoModel;
import com.solohub.teste_pulse.domain.model.record.RelatorioPedido;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class RelatorioPedidoAssembler extends BaseAssembler<RelatorioPedido, RelatorioPedidoModel, RelatorioController> {
    public RelatorioPedidoAssembler(ModelMapper mapper) {
        super(mapper, RelatorioController.class, RelatorioPedidoModel.class);
    }
}
