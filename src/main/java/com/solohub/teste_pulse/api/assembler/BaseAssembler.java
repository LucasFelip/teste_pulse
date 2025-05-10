package com.solohub.teste_pulse.api.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;

public abstract class BaseAssembler<E, D extends RepresentationModel<?>, C>
        extends RepresentationModelAssemblerSupport<E, D> {

    protected final ModelMapper mapper;

    protected BaseAssembler(ModelMapper mapper,
                            Class<C> controllerClass,
                            Class<D> dtoClass) {
        super(controllerClass, dtoClass);
        this.mapper = mapper;
    }

    @Override
    public D toModel(E entity) {
        D dto = instantiateModel(entity);
        System.out.println("entity: " + entity);
        mapper.map(entity, dto);
        return dto;
    }

    @Override
    public CollectionModel<D> toCollectionModel(Iterable<? extends E> entities) {
        return super.toCollectionModel(entities);
    }
}
