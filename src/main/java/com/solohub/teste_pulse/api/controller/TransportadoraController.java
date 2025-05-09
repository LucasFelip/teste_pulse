package com.solohub.teste_pulse.api.controller;

import com.solohub.teste_pulse.api.assembler.TransportadoraAssembler;
import com.solohub.teste_pulse.api.model.TransportadoraModel;
import com.solohub.teste_pulse.domain.service.TransportadoraService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/transportadoras")
@RequiredArgsConstructor
public class TransportadoraController {
    private final TransportadoraService transportadoraService;
    private final TransportadoraAssembler transportadoraAssembler;

    @Operation(
            summary = "Lista todas as transportadoras disponíveis",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de transportadoras",
                            content = @Content(array = @ArraySchema(
                                    schema = @Schema(implementation = TransportadoraModel.class))))
            }
    )
    @GetMapping
    public CollectionModel<TransportadoraModel> listarTodas() {
        var list = transportadoraService.listarTodas();
        return transportadoraAssembler.toCollectionModel(list);
    }
}
