package dev.elias.restaurante.delivery.controller;

import dev.elias.restaurante.delivery.dto.*;
import dev.elias.restaurante.delivery.service.DeliveryZoneService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/delivery-zones")
public class DeliveryZoneController {

    private final DeliveryZoneService service;

    public DeliveryZoneController(
            DeliveryZoneService service
    ) {
        this.service = service;
    }

    @GetMapping
    public List<DeliveryZoneResponse> findAll() {
        return service.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DeliveryZoneResponse create(
            @Valid
            @RequestBody
            SaveDeliveryZoneRequest request
    ) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public DeliveryZoneResponse update(
            @PathVariable Long id,
            @Valid
            @RequestBody
            SaveDeliveryZoneRequest request
    ) {
        return service.update(id, request);
    }
}