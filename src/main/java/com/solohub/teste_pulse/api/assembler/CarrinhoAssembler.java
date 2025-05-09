package com.solohub.teste_pulse.api.assembler;

import com.solohub.teste_pulse.api.controller.CarrinhoController;
import com.solohub.teste_pulse.api.model.CarrinhoModel;
import com.solohub.teste_pulse.domain.model.Carrinho;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class CarrinhoAssembler extends BaseAssembler<Carrinho, CarrinhoModel, CarrinhoController> {
    public CarrinhoAssembler(ModelMapper modelMapper) {
        super(modelMapper, CarrinhoController.class, CarrinhoModel.class);
    }
}
