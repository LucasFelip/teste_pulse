package com.solohub.teste_pulse.api.controller;

import com.solohub.teste_pulse.api.assembler.ProdutoAssembler;
import com.solohub.teste_pulse.api.model.ProdutoInputModel;
import com.solohub.teste_pulse.api.model.ProdutoModel;
import com.solohub.teste_pulse.domain.exceptions.ResourceNotFoundException;
import com.solohub.teste_pulse.domain.model.Produto;
import com.solohub.teste_pulse.domain.service.ProdutoService;
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
@RequestMapping("/v1/produtos")
@RequiredArgsConstructor
public class ProdutoController {
    private final ProdutoService produtoService;
    private final ModelMapper modelMapper;
    private final ProdutoAssembler produtoAssembler;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Cria um novo produto",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody (
                    description = "Dados do produto a ser criado",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ProdutoInputModel.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Produto criado com sucesso",
                            content = @Content(schema = @Schema(implementation = ProdutoModel.class))),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos",
                            content = @Content)
            }
    )
    public ProdutoModel criar(@RequestBody @Valid ProdutoInputModel input) {
        Produto produto = modelMapper.map(input, Produto.class);
        Produto salvo = produtoService.criar(produto);
        return produtoAssembler.toModel(salvo);
    }

    @GetMapping("/ativos")
    @Operation(
            summary = "Lista todos os produtos ativos",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de produtos ativos",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProdutoModel.class))))
            }
    )
    public CollectionModel<ProdutoModel> listarAtivos() {
        List<Produto> ativos = produtoService.listarAtivos();
        return produtoAssembler.toCollectionModel(ativos);
    }

    @Operation(
            summary = "Busca um produto por ID",
            parameters = @Parameter(name = "id", in = ParameterIn.PATH,
                    description = "ID do produto", required = true),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Produto encontrado",
                            content = @Content(schema = @Schema(implementation = ProdutoModel.class))),
                    @ApiResponse(responseCode = "404", description = "Produto não encontrado",
                            content = @Content)
            }
    )
    @GetMapping("/{id}")
    public ProdutoModel buscar(@PathVariable Long id) {
        Produto produto = produtoService.buscarPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto", id));
        return produtoAssembler.toModel(produto);
    }

    @Operation(
            summary = "Exclui um produto por ID",
            parameters = @Parameter(name = "id", in = ParameterIn.PATH,
                    description = "ID do produto a ser excluído", required = true),
            responses = {
                    @ApiResponse(responseCode = "204", description = "Produto excluído com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Produto não encontrado",
                            content = @Content)
            }
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long id) {
        produtoService.deletar(id);
    }
}
