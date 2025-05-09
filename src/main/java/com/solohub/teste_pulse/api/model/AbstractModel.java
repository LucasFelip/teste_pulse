package com.solohub.teste_pulse.api.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

@Data
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public abstract class AbstractModel extends RepresentationModel<AbstractModel> {
    @EqualsAndHashCode.Include
    private Long id;
}
