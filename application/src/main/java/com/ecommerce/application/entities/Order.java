package com.ecommerce.application.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "id")
    @Getter
    private User user;
    @Getter @Setter
    private BigDecimal totalAmount;
    @Getter 
    private LocalDateTime createdAt;
    @Getter @Setter
    private String status;
    @OneToOne
    @JoinColumn(name = "payment_id", referencedColumnName = "id")
    @Getter @Setter
    private Payment payment;

    public Order() {
    }

    public Order(User user, BigDecimal totalAmount, LocalDateTime createdAt, String status, Payment payment) {
        this.user = user;
        this.totalAmount = totalAmount;
        this.createdAt = createdAt;
        this.status = status;
        this.payment = payment;
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
        Order other = (Order) obj;
        if (id == null) {
           return other.id == null;
        } else return id.equals(other.id);
    }

    @Override
    public String toString() {
        return "Order [id=" + id + ", user=" + user + ", totalAmount=" + totalAmount + ", createdAt=" + createdAt
                + ", status=" + status + ", payment=" + payment + "]";
    }

    
}
