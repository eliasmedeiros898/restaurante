package dev.elias.restaurante.delivery.controller;

import dev.elias.restaurante.delivery.dto.DeliveryZoneResponse;
import dev.elias.restaurante.delivery.service.DeliveryZoneService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public/delivery-zones")
public class PublicDeliveryZoneController {

    private final DeliveryZoneService service;

    public PublicDeliveryZoneController(
            DeliveryZoneService service
    ) {
        this.service = service;
    }

    @GetMapping
    public List<DeliveryZoneResponse> findAll() {
        return service.findPublicActiveZones();
    }
}