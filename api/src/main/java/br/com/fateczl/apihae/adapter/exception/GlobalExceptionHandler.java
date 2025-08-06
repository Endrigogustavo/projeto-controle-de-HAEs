package br.com.fateczl.apihae.adapter.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.validation.FieldError;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Trata exceções de validação de argumentos de método (@Valid) para todos os
     * controladores.
     * Retorna um mapa com os nomes dos campos e suas mensagens de erro de
     * validação.
     * Status HTTP: 400 Bad Request.
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            // Verifica se o erro é um FieldError para obter o nome do campo
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    /**
     * Trata as exceções de lógica de negócio (IllegalArgumentException) lançadas
     * pelos serviços.
     * Retorna uma mensagem de erro genérica em português.
     * Status HTTP: 400 Bad Request.
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST) // Geralmente, IllegalArgumentException indica dados inválidos
    @ExceptionHandler(IllegalArgumentException.class)
    public Map<String, String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return Collections.singletonMap("mensagem", ex.getMessage());
    }

    /**
     * Trata quaisquer outras exceções não capturadas explicitamente.
     * Retorna uma mensagem de erro genérica de servidor.
     * Status HTTP: 500 Internal Server Error.
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public Map<String, String> handleAllUncaughtExceptions(Exception ex) {
        // Em um ambiente de produção, você logaria o `ex.printStackTrace()` para
        // depuração
        // e retornaria uma mensagem mais genérica para o cliente.
        // ex.printStackTrace();
        return Collections.singletonMap("mensagem",
                "Ocorreu um erro inesperado no servidor. Por favor, tente novamente mais tarde.");
    }
}