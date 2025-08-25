package com.example.shop.controller;

import com.example.shop.model.Order;
import com.example.shop.model.Views;
import com.example.shop.service.OrderService;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/users/{userId}/orders")
@RequiredArgsConstructor
public class OrderController {


    private final OrderService orderService;

    // Получение всех заказов пользователя
    @GetMapping
    @JsonView(Views.OrderSummary.class)
    public ResponseEntity<List<Order>> getUserOrders(@PathVariable Long userId) {
        List<Order> orders = orderService.getOrdersByUserId(userId);
        return ResponseEntity.ok(orders);
    }

    // Получение заказа по ID
    @GetMapping("/{orderId}")
    @JsonView(Views.OrderDetails.class)
    public ResponseEntity<Order> getOrderById(@PathVariable Long userId, @PathVariable Long orderId) {
        Optional<Order> order = orderService.getOrderById(orderId);
        return order.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Создание заказа
    @PostMapping
    @JsonView(Views.OrderDetails.class)
    public ResponseEntity<Order> createOrder(@PathVariable Long userId, @Valid @RequestBody Order order) {
        try {
            Order createdOrder = orderService.createOrder(userId, order);
            return ResponseEntity.status(201).body(createdOrder);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Обновление заказа
    @PutMapping("/{orderId}")
    @JsonView(Views.OrderDetails.class)
    public ResponseEntity<Order> updateOrder(@PathVariable Long userId,
                                             @PathVariable Long orderId,
                                             @Valid @RequestBody Order order) {
        try {
            Order updatedOrder = orderService.updateOrder(orderId, order);
            return ResponseEntity.ok(updatedOrder);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Удаление заказа
    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long userId, @PathVariable Long orderId) {
        try {
            orderService.deleteOrder(orderId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
