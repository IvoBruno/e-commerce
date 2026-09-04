package com.ecommerce.infrastructure.adapters.input.rest.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.ecommerce.domain.exception.DomainException;
import com.ecommerce.domain.exception.InsufficientStockException;
import com.ecommerce.domain.exception.ResourceNotFoundException;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

class GlobalExceptionHandlerTest {

  private GlobalExceptionHandler exceptionHandler;

  @BeforeEach
  void setUp() {
    exceptionHandler = new GlobalExceptionHandler();
  }

  @Test
  @DisplayName("Should return 404 ProblemDetail for ResourceNotFoundException")
  void shouldHandleResourceNotFoundException() {
    ResourceNotFoundException ex = new ResourceNotFoundException("Category not found with id: 99");
    ProblemDetail problem = exceptionHandler.handleNotFound(ex);

    assertEquals(HttpStatus.NOT_FOUND.value(), problem.getStatus());
    assertEquals("Resource Not Found", problem.getTitle());
    assertEquals("Category not found with id: 99", problem.getDetail());
    assertNotNull(problem.getProperties().get("timestamp"));
  }

  @Test
  @DisplayName("Should return 422 ProblemDetail for InsufficientStockException")
  void shouldHandleInsufficientStockException() {
    InsufficientStockException ex = new InsufficientStockException("Insufficient stock for product id: 5");
    ProblemDetail problem = exceptionHandler.handleInsufficientStock(ex);

    assertEquals(HttpStatus.UNPROCESSABLE_CONTENT.value(), problem.getStatus());
    assertEquals("Insufficient Stock", problem.getTitle());
    assertEquals("Insufficient stock for product id: 5", problem.getDetail());
    assertNotNull(problem.getProperties().get("timestamp"));
  }

  @Test
  @DisplayName("Should return 401 ProblemDetail for BadCredentialsException")
  void shouldHandleBadCredentialsException() {
    org.springframework.security.authentication.BadCredentialsException ex =
        new org.springframework.security.authentication.BadCredentialsException("Invalid email or password");
    ProblemDetail problem = exceptionHandler.handleBadCredentials(ex);

    assertEquals(HttpStatus.UNAUTHORIZED.value(), problem.getStatus());
    assertEquals("Authentication Failed", problem.getTitle());
    assertEquals("Invalid email or password", problem.getDetail());
    assertNotNull(problem.getProperties().get("timestamp"));
  }

  @Test
  @DisplayName("Should return 400 ProblemDetail for DomainException")
  void shouldHandleDomainException() {
    DomainException ex = new DomainException("Order is already cancelled");
    ProblemDetail problem = exceptionHandler.handleDomainException(ex);

    assertEquals(HttpStatus.BAD_REQUEST.value(), problem.getStatus());
    assertEquals("Business Rule Violation", problem.getTitle());
    assertEquals("Order is already cancelled", problem.getDetail());
    assertNotNull(problem.getProperties().get("timestamp"));
  }

  @Test
  @DisplayName("Should return 400 ProblemDetail for IllegalArgumentException")
  void shouldHandleIllegalArgumentException() {
    IllegalArgumentException ex = new IllegalArgumentException("Malformed identifier");
    ProblemDetail problem = exceptionHandler.handleIllegalArgument(ex);

    assertEquals(HttpStatus.BAD_REQUEST.value(), problem.getStatus());
    assertEquals("Bad Request", problem.getTitle());
    assertEquals("Malformed identifier", problem.getDetail());
    assertNotNull(problem.getProperties().get("timestamp"));
  }

  @Test
  @DisplayName("Should return 500 ProblemDetail for generic Exception without leaking stacktrace")
  void shouldHandleGenericException() {
    Exception ex = new RuntimeException("Database timeout");
    ProblemDetail problem = exceptionHandler.handleGenericException(ex);

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), problem.getStatus());
    assertEquals("Internal Server Error", problem.getTitle());
    assertEquals("An unexpected internal error occurred.", problem.getDetail());
    assertNotNull(problem.getProperties().get("timestamp"));
  }

  @Test
  @DisplayName("Should return 400 ProblemDetail with field-level errors for MethodArgumentNotValidException")
  @SuppressWarnings("unchecked")
  void shouldHandleMethodArgumentNotValidException() {
    MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
    BindingResult bindingResult = mock(BindingResult.class);

    FieldError fieldError1 = new FieldError("userDTO", "name", "Name is required");
    FieldError fieldError2 = new FieldError("userDTO", "email", "Email must be valid");

    when(ex.getBindingResult()).thenReturn(bindingResult);
    when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError1, fieldError2));

    org.springframework.http.ResponseEntity<Object> response = exceptionHandler.handleMethodArgumentNotValid(
        ex,
        new org.springframework.http.HttpHeaders(),
        HttpStatus.BAD_REQUEST,
        mock(org.springframework.web.context.request.WebRequest.class)
    );

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertTrue(response.getBody() instanceof ProblemDetail);
    ProblemDetail problem = (ProblemDetail) response.getBody();

    assertEquals(HttpStatus.BAD_REQUEST.value(), problem.getStatus());
    assertEquals("Validation Failed", problem.getTitle());
    assertNotNull(problem.getProperties().get("errors"));

    Map<String, String> errors = (Map<String, String>) problem.getProperties().get("errors");
    assertEquals("Name is required", errors.get("name"));
    assertEquals("Email must be valid", errors.get("email"));
  }
}
