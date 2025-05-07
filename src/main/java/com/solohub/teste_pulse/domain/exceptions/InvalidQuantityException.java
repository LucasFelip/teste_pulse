package com.solohub.teste_pulse.domain.exceptions;

public class InvalidQuantityException extends BusinessException {
    private final int quantity;

    public InvalidQuantityException(int quantity) {
        super(String.format("Quantidade inválida: %d. Deve ser maior que zero.", quantity));
        this.quantity = quantity;
    }

    public int getQuantity() { return quantity; }
}
