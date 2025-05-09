package com.solohub.teste_pulse.api.assembler;

import com.solohub.teste_pulse.api.controller.EnderecoController;
import com.solohub.teste_pulse.api.model.EnderecoModel;
import com.solohub.teste_pulse.domain.model.Endereco;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class EnderecoAssembler extends BaseAssembler<Endereco, EnderecoModel, EnderecoController> {
    public EnderecoAssembler(ModelMapper mapper) {
        super(mapper, EnderecoController.class, EnderecoModel.class);
    }
}

