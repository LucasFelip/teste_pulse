package com.solohub.teste_pulse.api.controller;

import com.solohub.teste_pulse.api.assembler.RelatorioPedidoAssembler;
import com.solohub.teste_pulse.api.model.RelatorioPedidoModel;
import com.solohub.teste_pulse.domain.service.RelatorioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/clientes/{clienteId}/relatorios/pedidos")
@RequiredArgsConstructor
public class RelatorioController {
    private final RelatorioService relatorioService;
    private final RelatorioPedidoAssembler relatorioPedidoAssembler;

    @GetMapping
    @Operation(
            summary = "Lista pedidos de um cliente com suas notas fiscais",
            parameters = @Parameter(name = "clienteId", in = ParameterIn.PATH,
                    description = "ID do cliente", required = true),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Relatório gerado com sucesso",
                            content = @Content(array = @ArraySchema(
                                    schema = @Schema(implementation = RelatorioPedidoModel.class)))),
                    @ApiResponse(responseCode = "404", description = "Nenhum pedido encontrado para este cliente",
                            content = @Content)
            }
    )
    public CollectionModel<RelatorioPedidoModel> listarPedidos(@PathVariable Long clienteId) {
        var rel = relatorioService.gerarRelatorioPorCliente(clienteId);
        return relatorioPedidoAssembler.toCollectionModel(rel);
    }
}
