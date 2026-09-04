package com.ecommerce.infrastructure.adapters.input.rest.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ecommerce.domain.model.User;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DtoValidationTest {

  private static Validator validator;

  @BeforeAll
  static void setUpValidator() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @Test
  @DisplayName("Should pass validation for a valid UserDTO")
  void shouldValidateValidUserDTO() {
    UserDTO user = new UserDTO(null, "Alice Smith", "alice@example.com", "password123", "123.456.789-00");
    Set<ConstraintViolation<UserDTO>> violations = validator.validate(user);
    assertTrue(violations.isEmpty());
  }

  @Test
  @DisplayName("Should fail validation for invalid UserDTO (blank fields, invalid email and short password)")
  void shouldFailInvalidUserDTO() {
    UserDTO user = new UserDTO(null, "", "invalid-email", "123", "invalid-cpf");
    Set<ConstraintViolation<UserDTO>> violations = validator.validate(user);

    assertEquals(4, violations.size());
  }

  @Test
  @DisplayName("Should mask password when creating UserDTO from domain model")
  void shouldMaskPasswordInUserDTOFromDomain() {
    User user = User.builder()
        .id(1L)
        .name("Bob")
        .email("bob@example.com")
        .password("super_secret_hash")
        .cpf("12345678901")
        .build();

    UserDTO dto = UserDTO.fromDomain(user);
    assertNull(dto.password(), "Password must be null in DTO to prevent leakage");
    assertEquals("Bob", dto.name());
  }

  @Test
  @DisplayName("Should pass validation for a valid ProductDTO")
  void shouldValidateValidProductDTO() {
    ProductDTO product = new ProductDTO(null, "Laptop", "High end", new BigDecimal("1999.99"), 10L, LocalDate.now(), 1L);
    Set<ConstraintViolation<ProductDTO>> violations = validator.validate(product);
    assertTrue(violations.isEmpty());
  }

  @Test
  @DisplayName("Should fail validation for ProductDTO with negative price and blank name")
  void shouldFailProductDTOWithNegativePrice() {
    ProductDTO product = new ProductDTO(null, " ", "High end", new BigDecimal("-10.00"), -1L, LocalDate.now(), null);
    Set<ConstraintViolation<ProductDTO>> violations = validator.validate(product);

    assertFalse(violations.isEmpty());
    assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("name")));
    assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("price")));
    assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("quantity")));
    assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("category_id")));
  }

  @Test
  @DisplayName("Should fail validation for CategoryDTO with blank name")
  void shouldFailCategoryDTOWithBlankName() {
    CategoryDTO category = new CategoryDTO(null, "", "Description");
    Set<ConstraintViolation<CategoryDTO>> violations = validator.validate(category);

    assertEquals(1, violations.size());
    assertEquals("name", violations.iterator().next().getPropertyPath().toString());
  }

  @Test
  @DisplayName("Should fail validation for OrderDTO without user_id")
  void shouldFailOrderDTOWithoutUserId() {
    OrderDTO order = new OrderDTO(null, null, BigDecimal.ZERO, null, "PENDING", null);
    Set<ConstraintViolation<OrderDTO>> violations = validator.validate(order);

    assertEquals(1, violations.size());
    assertEquals("user_id", violations.iterator().next().getPropertyPath().toString());
  }

  @Test
  @DisplayName("Should fail validation for ProductOrderDTO with zero quantity and negative price")
  void shouldFailProductOrderDTOWithInvalidQuantityAndPrice() {
    ProductOrderDTO item = new ProductOrderDTO(null, null, null, 0, new BigDecimal("-5.00"));
    Set<ConstraintViolation<ProductOrderDTO>> violations = validator.validate(item);

    assertEquals(4, violations.size());
  }

  @Test
  @DisplayName("Should fail validation for PaymentDTO with blank method and negative amount")
  void shouldFailPaymentDTOWithInvalidValues() {
    PaymentDTO payment = new PaymentDTO(null, " ", new BigDecimal("-100.00"), null, null);
    Set<ConstraintViolation<PaymentDTO>> violations = validator.validate(payment);

    assertEquals(2, violations.size());
  }
}

