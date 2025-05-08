package com.solohub.teste_pulse.api.exception;

import com.solohub.teste_pulse.domain.exceptions.BusinessException;
import com.solohub.teste_pulse.domain.exceptions.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.OffsetDateTime;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Problem> handleNotFound(ResourceNotFoundException ex) {
        var pt = ProblemType.RECURSO_NAO_ENCONTRADO;
        var problem = createProblem(pt, ex.getMessage());
        return ResponseEntity.status(pt.getStatus()).body(problem);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Problem> handleBusiness(BusinessException ex) {
        var pt = ProblemType.ERRO_NEGOCIO;
        var problem = createProblem(pt, ex.getMessage());
        return ResponseEntity.status(pt.getStatus()).body(problem);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Problem> handleUncaught(Exception ex, WebRequest request) {
        ex.printStackTrace();
        var pt = ProblemType.ERRO_DE_SISTEMA;
        var detail = "Ocorreu um erro interno inesperado. Tente novamente mais tarde.";
        var problem = createProblem(pt, detail);
        return ResponseEntity.status(pt.getStatus()).body(problem);
    }

    private Problem createProblem(ProblemType pt, String detail) {
        return Problem.builder()
                .type(pt.getUri())
                .title(pt.getTitle())
                .status(pt.getStatus().value())
                .detail(detail)
                .timestamp(OffsetDateTime.now())
                .build();
    }
}
