package com.example.shop;

import com.example.shop.controller.OrderController;
import com.example.shop.model.Order;
import com.example.shop.model.User;
import com.example.shop.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)

public class OrderControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private OrderService orderService;

    @Test
    void getUserOrders_ShouldReturnOrderSummaryView() throws Exception {
        Order order1 = Order.builder()
                .amount(new BigDecimal("100.00"))
                .id(1L)
                .productName("Product 1")
                .build();

        Order order2 = Order.builder()
                .amount(new BigDecimal("200.00"))
                .id(2L)
                .user(new User())
                .productName("Product 2")
                .build();

        when(orderService.getOrdersByUserId(1L)).thenReturn(Arrays.asList(order1, order2));

        mockMvc.perform(get("/api/users/1/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].productName").value("Product 1"))
                .andExpect(jsonPath("$[0].amount").value(100.00))
                .andExpect(jsonPath("$[0].user").doesNotExist()) // User не должен быть в summary view
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].productName").value("Product 2"))
                .andExpect(jsonPath("$[1].amount").value(200.00));
    }

    @Test
    void getOrderById_ShouldReturnOrderDetailsView() throws Exception {
        Order order = Order.builder()
                .amount(new BigDecimal("100.00"))
                .id(1L)
                .user(new User())
                .productName("Product 1")
                .build();

        when(orderService.getOrderById(1L)).thenReturn(Optional.of(order));

        mockMvc.perform(get("/api/users/1/orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.productName").value("Product 1"))
                .andExpect(jsonPath("$.amount").value(100.00))
                .andExpect(jsonPath("$.user").exists()); // User должен быть в details view
    }
}
