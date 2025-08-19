package br.com.fateczl.apihae.adapter.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
//import org.springframework.security.access.AccessDeniedException;
//import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.validation.FieldError;
import jakarta.persistence.EntityNotFoundException;


import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. Erros de validação com @Valid
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException.class)
    public Map<String, String> handleEntityNotFoundException(EntityNotFoundException ex) {
        return Collections.singletonMap("mensagem", "Recurso não encontrado.");
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public Map<String, String> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        return Collections.singletonMap("mensagem", "Violação de integridade dos dados.");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Map<String, String> handleMissingParams(MissingServletRequestParameterException ex) {
        return Collections.singletonMap("mensagem", "Parâmetro obrigatório ausente: " + ex.getParameterName());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Map<String, String> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        return Collections.singletonMap("mensagem", "Requisição malformada. Verifique o corpo da requisição.");
    }

    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Map<String, String> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        return Collections.singletonMap("mensagem", "Método HTTP não suportado para este endpoint.");
    }

    /* 
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(BadCredentialsException.class)
    public Map<String, String> handleBadCredentials(BadCredentialsException ex) {
        return Collections.singletonMap("mensagem", "Credenciais inválidas.");
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    public Map<String, String> handleAccessDeniedException(AccessDeniedException ex) {
        return Collections.singletonMap("mensagem", "Você não tem permissão para acessar este recurso.");
    }
    */

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(NullPointerException.class)
    public Map<String, String> handleNullPointerException(NullPointerException ex) {
        return Collections.singletonMap("mensagem", "Erro inesperado. Um valor necessário não foi fornecido.");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NumberFormatException.class)
    public Map<String, String> handleNumberFormatException(NumberFormatException ex) {
        return Collections.singletonMap("mensagem", "Formato numérico inválido.");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public Map<String, String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return Collections.singletonMap("mensagem", ex.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public Map<String, String> handleAllUncaughtExceptions(Exception ex) {
        return Collections.singletonMap("mensagem",
                "Ocorreu um erro inesperado no servidor. Por favor, tente novamente mais tarde.");
    }
}
