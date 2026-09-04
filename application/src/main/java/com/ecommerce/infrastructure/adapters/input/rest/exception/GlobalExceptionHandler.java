package com.ecommerce.infrastructure.adapters.input.rest.exception;

import com.ecommerce.domain.exception.DomainException;
import com.ecommerce.domain.exception.InsufficientStockException;
import com.ecommerce.domain.exception.ResourceNotFoundException;
import jakarta.validation.ConstraintViolationException;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex,
      HttpHeaders headers,
      HttpStatusCode status,
      WebRequest request
  ) {
    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
        status,
        "Invalid request parameters. Check 'errors' for field-level details."
    );
    problemDetail.setTitle("Validation Failed");
    problemDetail.setProperty("timestamp", Instant.now());

    Map<String, String> errors = new LinkedHashMap<>();
    for (FieldError error : ex.getBindingResult().getFieldErrors()) {
      errors.put(error.getField(), error.getDefaultMessage());
    }
    problemDetail.setProperty("errors", errors);

    return ResponseEntity.status(status).headers(headers).body(problemDetail);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ProblemDetail handleConstraintViolation(ConstraintViolationException ex) {
    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
        HttpStatus.BAD_REQUEST,
        "Constraint violation occurred."
    );
    problemDetail.setTitle("Constraint Violation");
    problemDetail.setProperty("timestamp", Instant.now());

    Map<String, String> errors = new LinkedHashMap<>();
    ex.getConstraintViolations().forEach(violation ->
        errors.put(violation.getPropertyPath().toString(), violation.getMessage())
    );
    problemDetail.setProperty("errors", errors);
    return problemDetail;
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  public ProblemDetail handleNotFound(ResourceNotFoundException ex) {
    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
    problemDetail.setTitle("Resource Not Found");
    problemDetail.setProperty("timestamp", Instant.now());
    return problemDetail;
  }

  @ExceptionHandler(InsufficientStockException.class)
  public ProblemDetail handleInsufficientStock(InsufficientStockException ex) {
    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_CONTENT, ex.getMessage());
    problemDetail.setTitle("Insufficient Stock");
    problemDetail.setProperty("timestamp", Instant.now());
    return problemDetail;
  }

  @ExceptionHandler(DomainException.class)
  public ProblemDetail handleDomainException(DomainException ex) {
    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
    problemDetail.setTitle("Business Rule Violation");
    problemDetail.setProperty("timestamp", Instant.now());
    return problemDetail;
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ProblemDetail handleIllegalArgument(IllegalArgumentException ex) {
    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
    problemDetail.setTitle("Bad Request");
    problemDetail.setProperty("timestamp", Instant.now());
    return problemDetail;
  }

  @ExceptionHandler(Exception.class)
  public ProblemDetail handleGenericException(Exception ex) {
    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
        HttpStatus.INTERNAL_SERVER_ERROR,
        "An unexpected internal error occurred."
    );
    problemDetail.setTitle("Internal Server Error");
    problemDetail.setProperty("timestamp", Instant.now());
    return problemDetail;
  }
}
