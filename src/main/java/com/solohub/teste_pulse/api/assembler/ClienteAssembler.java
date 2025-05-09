package com.solohub.teste_pulse.api.assembler;

import com.solohub.teste_pulse.api.controller.ClienteController;
import com.solohub.teste_pulse.api.model.ClienteModel;
import com.solohub.teste_pulse.domain.model.Cliente;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ClienteAssembler extends BaseAssembler<Cliente, ClienteModel, ClienteController> {
    public ClienteAssembler(ModelMapper modelMapper) {
        super(modelMapper, ClienteController.class, ClienteModel.class);
    }
}
