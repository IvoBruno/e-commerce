package com.ecommerce.application.entities;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
public class ProductOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false, referencedColumnName = "id")
    @Getter @Setter
    private Product product;
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false, referencedColumnName = "id")
    @Getter @Setter
    private Order order;
    @Getter @Setter
    private Integer quantity;
    @Getter @Setter
    private BigDecimal unityPrice;

    public ProductOrder() {
    }

    public ProductOrder(Product product, Order order, Integer quantity, BigDecimal unityPrice) {
        this.product = product;
        this.order = order;
        this.quantity = quantity;
        this.unityPrice = unityPrice;
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
        ProductOrder other = (ProductOrder) obj;
        if (id == null) {
           return other.id == null;
        } else return id.equals(other.id);
    }

    @Override
    public String toString() {
        return "ProductOrder [id=" + id + ", product=" + product + ", order=" + order + ", quantity=" + quantity
                + ", unityPrice=" + unityPrice + "]";
    }
    
}
