package com.solohub.teste_pulse.api.assembler;

import com.solohub.teste_pulse.api.controller.ProdutoController;
import com.solohub.teste_pulse.api.model.ProdutoModel;
import com.solohub.teste_pulse.domain.model.Produto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ProdutoAssembler extends BaseAssembler<Produto, ProdutoModel, ProdutoController> {
    public ProdutoAssembler(ModelMapper mapper) {
        super(mapper, ProdutoController.class, ProdutoModel.class);
    }
}
