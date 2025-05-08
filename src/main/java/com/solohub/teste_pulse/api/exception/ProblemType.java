package com.solohub.teste_pulse.api.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ProblemType {
    RECURSO_NAO_ENCONTRADO("/recurso-nao-encontrado", "Recurso não encontrado", HttpStatus.NOT_FOUND),
    ERRO_NEGOCIO("/erro-negocio", "Violação de regra de negócio", HttpStatus.BAD_REQUEST),
    ERRO_DE_SISTEMA("/erro-de-sistema", "Erro interno no servidor", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String uri;
    private final String title;
    private final HttpStatus status;

    ProblemType(String path, String title, HttpStatus status) {
        this.uri    = "https://api.teste-pulse.com.br" + path;
        this.title  = title;
        this.status = status;
    }
}
