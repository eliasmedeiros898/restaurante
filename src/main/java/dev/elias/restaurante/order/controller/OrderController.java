package dev.elias.restaurante.order.controller;

import dev.elias.restaurante.order.dto.CreateOrderRequest;
import dev.elias.restaurante.order.dto.OrderResponse;
import dev.elias.restaurante.order.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(
            OrderService orderService
    ) {
        this.orderService = orderService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse createPublicOrder(
            @Valid
            @RequestBody
            CreateOrderRequest request
    ) {
        return orderService.createPublicOrder(
                request
        );
    }

    @GetMapping("/number/{orderNumber}")
    public OrderResponse findByOrderNumber(
            @PathVariable Long orderNumber
    ) {
        return orderService.findByOrderNumber(
                orderNumber
        );
    }
}