package com.example.shop.model;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({Views.UserDetails.class, Views.OrderSummary.class})
    private Long id;

    @NotBlank(message = "Название товара обязательно")
    @JsonView({Views.UserDetails.class, Views.OrderSummary.class})
    private String productName;

    @NotNull(message = "Цена обязательна")
    @DecimalMin(value = "0.0", inclusive = false, message = "Цена должна быть больше 0")
    @JsonView({Views.UserDetails.class, Views.OrderSummary.class})
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @JsonView({Views.UserDetails.class, Views.OrderSummary.class})
    private OrderStatus status;

    @JsonView({Views.UserDetails.class, Views.OrderSummary.class})
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonView(Views.OrderDetails.class)
    private User user;
}
