package com.solohub.teste_pulse.api.controller;

import com.solohub.teste_pulse.api.assembler.CarrinhoAssembler;
import com.solohub.teste_pulse.api.model.CarrinhoModel;
import com.solohub.teste_pulse.api.model.ItemCarrinhoModel;
import com.solohub.teste_pulse.domain.exceptions.ResourceNotFoundException;
import com.solohub.teste_pulse.domain.model.Carrinho;
import com.solohub.teste_pulse.domain.service.CarrinhoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/carrinhos")
@RequiredArgsConstructor
public class CarrinhoController {
    private final CarrinhoService carrinhoService;
    private final CarrinhoAssembler carrinhoAssembler;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Cria um novo carrinho para um cliente",
            parameters = @Parameter(name = "clienteId", in = ParameterIn.QUERY,
                    description = "ID do cliente que receberá o carrinho", required = true),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Carrinho criado com sucesso",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = CarrinhoModel.class))),
                    @ApiResponse(responseCode = "404", description = "Cliente não encontrado",
                            content = @Content)
            }
    )
    public CarrinhoModel criarCarrinho(@RequestParam("clienteId") Long clienteId) {
        Carrinho carrinho = carrinhoService.criarCarrinho(clienteId);
        return carrinhoAssembler.toModel(carrinho);
    }

    @PostMapping("/{carrinhoId}/itens")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Adiciona um item a um carrinho existente",
            parameters = @Parameter(name = "carrinhoId", in = ParameterIn.PATH,
                    description = "ID do carrinho", required = true),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Objeto com produtoId e quantidade",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ItemCarrinhoModel.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Item adicionado com sucesso",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = CarrinhoModel.class))),
                    @ApiResponse(responseCode = "404", description = "Carrinho ou Produto não encontrado",
                            content = @Content),
                    @ApiResponse(responseCode = "400", description = "Quantidade inválida",
                            content = @Content)
            }
    )
    public CarrinhoModel adicionarItem(
            @PathVariable Long carrinhoId,
            @RequestBody @Valid ItemCarrinhoModel input
    ) {
        Carrinho carrinho = carrinhoService.adicionarItemCarrinho(
                carrinhoId,
                input.getProdutoId(),
                input.getQuantidade()
        );
        return carrinhoAssembler.toModel(carrinho);
    }

    @GetMapping("/aberto")
    @Operation(
            summary = "Recupera o carrinho aberto de um cliente",
            parameters = @Parameter(name = "clienteId", in = ParameterIn.QUERY,
                    description = "ID do cliente", required = true),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Carrinho aberto encontrado",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = CarrinhoModel.class))),
                    @ApiResponse(responseCode = "404", description = "Nenhum carrinho aberto para o cliente",
                            content = @Content)
            }
    )
    public CarrinhoModel buscarAberto(@RequestParam("clienteId") Long clienteId) {
        Carrinho carrinho = carrinhoService.buscarCarrinhoAberto(clienteId)
                .orElseThrow(() -> new ResourceNotFoundException("Carrinho aberto", clienteId));
        return carrinhoAssembler.toModel(carrinho);
    }
}
