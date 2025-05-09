package com.solohub.teste_pulse.api.controller;

import com.solohub.teste_pulse.api.assembler.PedidoAssembler;
import com.solohub.teste_pulse.api.model.CheckoutInputModel;
import com.solohub.teste_pulse.api.model.PedidoModel;
import com.solohub.teste_pulse.domain.model.Pedido;
import com.solohub.teste_pulse.domain.model.enums.FormaPagamento;
import com.solohub.teste_pulse.domain.service.CheckoutService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/checkout")
@RequiredArgsConstructor
public class CheckoutController {
    private final CheckoutService checkoutService;
    private final PedidoAssembler pedidoAssembler;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Efetua o checkout de um carrinho",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados necessários para checkout: IDs de carrinho, endereço e transportadora, e forma de pagamento",
                    required = true,
                    content = @Content(schema = @Schema(implementation = CheckoutInputModel.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Pedido gerado com sucesso",
                            content = @Content(schema = @Schema(implementation = PedidoModel.class))),
                    @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos",
                            content = @Content),
                    @ApiResponse(responseCode = "404", description = "Carrinho, endereço ou transportadora não encontrado",
                            content = @Content)
            }
    )
    public PedidoModel checkout(@RequestBody @Valid CheckoutInputModel inputModel) {
        FormaPagamento formaPagamento = FormaPagamento.valueOf(inputModel.getFormaPagamento());
        Pedido pedido = checkoutService.checkout(inputModel.getCarrinhoId(), inputModel.getEnderecoId(), inputModel.getTransportadoraId(), formaPagamento);
        return pedidoAssembler.toModel(pedido);
    }
}
