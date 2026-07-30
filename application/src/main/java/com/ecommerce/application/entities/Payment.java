package com.ecommerce.application.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;
    @Getter
    private String paymentMethod;
    @Getter 
    private BigDecimal amount;
    @Getter @Setter
    private String status;
    @Getter
    private LocalDateTime createdAt;

    public Payment() {
    }

    public Payment(String paymentMethod, BigDecimal amount, String status, LocalDateTime createdAt) {
        this.paymentMethod = paymentMethod;
        this.amount = amount;
        this.status = status;
        this.createdAt = createdAt;
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
        Payment other = (Payment) obj;
        if (id == null) {
           return other.id == null;
        } else return id.equals(other.id);
    }

    @Override
    public String toString() {
        return "Payment [id=" + id + ", paymentMethod=" + paymentMethod + ", amount=" + amount + ", status=" + status
                + ", createdAt=" + createdAt + "]";
    }
    
}
