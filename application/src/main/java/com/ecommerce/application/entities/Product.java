package com.ecommerce.application.entities;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;
    @Getter @Setter
    private String name;
    @Getter @Setter
    private String description;
    @Getter @Setter
    private BigDecimal price;
    @Getter @Setter
    private Long quantity;
    @Getter
    private LocalDate createdAt;
    @ManyToOne
    @JoinColumn(name = "category_id")
    @Getter @Setter
    private Category category;

    public Product() {
    }

    public Product(String name, String description, BigDecimal price, Long quantity, LocalDate createdAt,
            Category category) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.createdAt = createdAt;
        this.category = category;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Product other = (Product) obj;
        if (id == null) {
           return other.id == null;
        } else return id.equals(other.id);
    }

    @Override
    public String toString() {
        return "Product [id=" + id + ", name=" + name + ", description=" + description + ", price=" + price
                + ", quantity=" + quantity + ", createdAt=" + createdAt + ", category=" + category + "]";
    }
    
    
}
