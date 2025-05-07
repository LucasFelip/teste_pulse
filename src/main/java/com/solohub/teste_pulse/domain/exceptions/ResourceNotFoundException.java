package com.solohub.teste_pulse.domain.exceptions;

public class ResourceNotFoundException extends BusinessException {
    private final String resourceName;
    private final Object resourceId;

    public ResourceNotFoundException(String resourceName, Object resourceId) {
        super(String.format("%s com ID %s não foi encontrado", resourceName, resourceId));
        this.resourceName = resourceName;
        this.resourceId   = resourceId;
    }

    public String getResourceName() { return resourceName; }
    public Object getResourceId()   { return resourceId; }
}
