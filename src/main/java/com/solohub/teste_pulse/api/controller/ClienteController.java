package com.solohub.teste_pulse.api.controller;

import com.solohub.teste_pulse.api.assembler.ClienteAssembler;
import com.solohub.teste_pulse.api.model.ClienteInputModel;
import com.solohub.teste_pulse.api.model.ClienteModel;
import com.solohub.teste_pulse.domain.exceptions.ResourceNotFoundException;
import com.solohub.teste_pulse.domain.model.Cliente;
import com.solohub.teste_pulse.domain.service.ClienteService;
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
@RequestMapping("/v1/clientes")
@RequiredArgsConstructor
public class ClienteController {
    private final ClienteService clienteService;
    private final ModelMapper modelMapper;
    private final ClienteAssembler clienteAssembler;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Cria um novo cliente",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados do cliente a ser criado",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ClienteInputModel.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Cliente criado com sucesso",
                            content = @Content(schema = @Schema(implementation = ClienteModel.class))),
                    @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos",
                            content = @Content)
            }
    )
    public ClienteModel criar(@RequestBody @Valid ClienteInputModel input) {
        Cliente cliente = modelMapper.map(input, Cliente.class);
        Cliente salvo = clienteService.criar(cliente);
        return clienteAssembler.toModel(salvo);
    }

    @GetMapping
    @Operation(
            summary = "Lista todos os clientes",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de clientes",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = ClienteModel.class))))
            }
    )
    public List<ClienteModel> listar() {
        return clienteAssembler.toCollectionModel(clienteService.listarTodos()).getContent().stream().toList();
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Busca um cliente por ID",
            parameters = @Parameter(name = "id", in = ParameterIn.PATH,
                    description = "ID do cliente", required = true),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Cliente encontrado",
                            content = @Content(schema = @Schema(implementation = ClienteModel.class))),
                    @ApiResponse(responseCode = "404", description = "Cliente não encontrado",
                            content = @Content)
            }
    )
    public ClienteModel buscar(@PathVariable Long id) {
        Cliente cliente = clienteService.buscarPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente", id));
        return clienteAssembler.toModel(cliente);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Exclui um cliente por ID",
            parameters = @Parameter(name = "id", in = ParameterIn.PATH,
                    description = "ID do cliente a ser excluído", required = true),
            responses = {
                    @ApiResponse(responseCode = "204", description = "Cliente excluído com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Cliente não encontrado",
                            content = @Content)
            }
    )
    public void deletar(@PathVariable Long id) {
        clienteService.deletar(id);
    }
}
