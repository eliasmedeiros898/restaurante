package dev.elias.restaurante.order.controller;

import dev.elias.restaurante.order.dto.CreateOrderRequest;
import dev.elias.restaurante.order.dto.OrderResponse;
import dev.elias.restaurante.order.entity.OrderChannel;
import dev.elias.restaurante.order.entity.OrderStatus;
import dev.elias.restaurante.order.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/admin/orders")
public class AdminOrderController {

    private final OrderService service;

    public AdminOrderController(
            OrderService orderService
    ) {
        this.service = orderService;
    }

    @GetMapping
    public List<OrderResponse> findAll(
            @RequestParam(required = false)
            @DateTimeFormat(
                    iso = DateTimeFormat.ISO.DATE
            )
            LocalDate date,

            @RequestParam(required = false)
            OrderStatus status,

            @RequestParam(required = false)
            OrderChannel channel
    ) {
        return service.findAll(
                date,
                status,
                channel
        );
    }

    @GetMapping("/{id}")
    public OrderResponse findById(
            @PathVariable Long id
    ) {
        return service.findById(id);
    }

    @PostMapping("/counter")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse createCounterOrder(
            @Valid
            @RequestBody
            CreateOrderRequest request
    ) {
        return service.createCounterOrder(
                request
        );
    }

    @PatchMapping("/{id}/confirm")
    public OrderResponse confirm(
            @PathVariable Long id
    ) {
        return service.confirm(id);
    }

    @PatchMapping("/{id}/start-preparation")
    public OrderResponse startPreparation(
            @PathVariable Long id
    ) {
        return service.startPreparation(id);
    }

    @PatchMapping("/{id}/ready")
    public OrderResponse markAsReady(
            @PathVariable Long id
    ) {
        return service.markReady(id);
    }

    @PatchMapping("/{id}/deliver")
    public OrderResponse deliver(
            @PathVariable Long id
    ) {
        return service.deliver(id);
    }

    @PatchMapping("/{id}/cancel")
    public OrderResponse cancel(
            @PathVariable Long id
    ) {
        return service.cancel(id);
    }

    @PatchMapping("/{id}/pay")
    public OrderResponse markAsPaid(
            @PathVariable Long id
    ) {
        return service.markAsPaid(id);
    }
}