package com.solohub.teste_pulse.api.assembler;

import com.solohub.teste_pulse.api.controller.TransportadoraController;
import com.solohub.teste_pulse.api.model.TransportadoraModel;
import com.solohub.teste_pulse.domain.model.Transportadora;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class TransportadoraAssembler extends BaseAssembler<Transportadora, TransportadoraModel, TransportadoraController> {
    public TransportadoraAssembler(ModelMapper mapper) {
        super(mapper, TransportadoraController.class, TransportadoraModel.class);
    }
}
