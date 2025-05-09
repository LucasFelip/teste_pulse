package com.solohub.teste_pulse.api.controller;

import com.solohub.teste_pulse.api.assembler.EnderecoAssembler;
import com.solohub.teste_pulse.api.model.EnderecoInputModel;
import com.solohub.teste_pulse.api.model.EnderecoModel;
import com.solohub.teste_pulse.domain.exceptions.ResourceNotFoundException;
import com.solohub.teste_pulse.domain.model.Endereco;
import com.solohub.teste_pulse.domain.service.EnderecoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/clientes/{clienteId}/enderecos")
@RequiredArgsConstructor
public class EnderecoController {
    private final EnderecoService enderecoService;
    private final EnderecoAssembler assembler;
    private final ModelMapper modelMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Cria um novo endereço para um cliente",
            parameters = @Parameter(name = "clienteId", in = ParameterIn.PATH,
                    description = "ID do cliente", required = true),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody (
                    description = "Dados do endereço a ser criado",
                    required = true,
                    content = @Content(schema = @Schema(implementation = EnderecoInputModel.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Endereço criado com sucesso",
                            content = @Content(schema = @Schema(implementation = EnderecoModel.class))),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos",
                            content = @Content),
                    @ApiResponse(responseCode = "404", description = "Cliente não encontrado",
                            content = @Content)
            }
    )
    public EnderecoModel criar(@PathVariable Long clienteId, @RequestBody @Valid EnderecoInputModel input) {
        Endereco enderecoDomain = modelMapper.map(input, Endereco.class);
        Endereco salvo = enderecoService.criar(clienteId, enderecoDomain);
        return assembler.toModel(salvo);
    }

    @GetMapping
    @Operation(
            summary = "Lista todos os endereços de um cliente",
            parameters = @Parameter(name = "clienteId", in = ParameterIn.PATH,
                    description = "ID do cliente", required = true),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de endereços",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = EnderecoModel.class))))
            }
    )
    public CollectionModel<EnderecoModel> listar(@PathVariable Long clienteId) {
        List<Endereco> lista = enderecoService.listarPorCliente(clienteId);
        return assembler.toCollectionModel(lista);
    }

    @GetMapping("/principal")
    @Operation(
            summary = "Recupera o endereço principal de um cliente",
            parameters = @Parameter(name = "clienteId", in = ParameterIn.PATH,
                    description = "ID do cliente", required = true),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Endereço principal encontrado",
                            content = @Content(schema = @Schema(implementation = EnderecoModel.class))),
                    @ApiResponse(responseCode = "404", description = "Nenhum endereço principal cadastrado",
                            content = @Content)
            }
    )
    public EnderecoModel buscarPrincipal(@PathVariable Long clienteId) {
        Endereco principal = enderecoService.buscarPrincipal(clienteId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Endereço principal", clienteId));
        return assembler.toModel(principal);
    }

    @DeleteMapping("/{enderecoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Exclui um endereço pelo ID",
            parameters = {
                    @Parameter(name = "clienteId", in = ParameterIn.PATH,
                            description = "ID do cliente", required = true),
                    @Parameter(name = "enderecoId", in = ParameterIn.PATH,
                            description = "ID do endereço", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "204", description = "Endereço excluído com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Endereço não encontrado",
                            content = @Content)
            }
    )
    public void deletar(@PathVariable Long enderecoId) {
        enderecoService.deletar(enderecoId);
    }
}
