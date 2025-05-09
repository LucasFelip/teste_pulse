package com.solohub.teste_pulse.api.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;

import java.util.List;
import java.util.stream.Collectors;

public abstract class BaseAssembler<E, D extends RepresentationModel<?>, C>
        extends RepresentationModelAssemblerSupport<E, D> {

    protected final ModelMapper mapper;
    protected final Class<C> controllerClass;

    protected BaseAssembler(ModelMapper mapper,
                            Class<C> controllerClass,
                            Class<D> dtoClass) {
        super(controllerClass, dtoClass);
        this.mapper = mapper;
        this.controllerClass = controllerClass;
    }

    @Override
    public D toModel(E entity) {
        return mapper.map(entity, getResourceType());
    }

    @Override
    public CollectionModel<D> toCollectionModel(Iterable<? extends E> entities) {
        List<D> dtos =
                ((List<E>) entities).stream()
                        .map(this::toModel)
                        .collect(Collectors.toList());
        return CollectionModel.of(dtos);
    }
}

