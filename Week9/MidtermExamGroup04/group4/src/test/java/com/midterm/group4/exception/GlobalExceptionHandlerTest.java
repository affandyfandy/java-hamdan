package com.midterm.group4.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        globalExceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void testHandleObjectNotFoundException() {
        ObjectNotFoundException ex = new ObjectNotFoundException("Object not found");
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleObjectNotFoundException(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND.value(), Objects.requireNonNull(response.getBody()).getStatus());
        assertEquals("Object not found", response.getBody().getMessage());
    }

    @Test
    void testHandleInvalidInputException() {
        InvalidInputException ex = new InvalidInputException("Invalid input");
        ResponseEntity<Map<String, String>> response = globalExceptionHandler.handleInvalidInputException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid input", Objects.requireNonNull(response.getBody()).get("error"));
    }

    @Test
    void testHandleValidationExceptions() {
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "objectName");
        bindingResult.addError(new FieldError("objectName", "field", "must not be null"));

        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindingResult);
        ResponseEntity<Map<String, String>> response = globalExceptionHandler.handleValidationExceptions(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("must not be null", response.getBody().get("field"));
    }

    @Test
    void testHandleGenericException() {
        Exception ex = new Exception("Something went wrong");
        ResponseEntity<Map<String, String>> response = globalExceptionHandler.handleGenericException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An unexpected error occurred: Something went wrong", response.getBody().get("error"));
    }
}
