package dev.elias.restaurante.order.controller;

import dev.elias.restaurante.order.dto.CreateOrderRequest;
import dev.elias.restaurante.order.dto.OrderResponse;
import dev.elias.restaurante.order.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/public/orders")
public class PublicOrderController {

    private final OrderService service;

    public PublicOrderController(OrderService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse create(
            @Valid @RequestBody CreateOrderRequest request
    ) {
        return service.createPublicOrder(request);
    }
}