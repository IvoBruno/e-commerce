package com.ecommerce.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category {
  private Long id;
  private String name;
  private String description;

  public Category(String name, String description) {
    this.name = name;
    this.description = description;
  }
}

