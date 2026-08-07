package dev.elias.restaurante.delivery.dto;

import dev.elias.restaurante.delivery.entity.DeliveryZone;

import java.math.BigDecimal;

public record DeliveryZoneResponse(
        Long id,
        String neighborhood,
        BigDecimal deliveryFee,
        Integer displayOrder,
        Boolean active
) {

    public static DeliveryZoneResponse from(
            DeliveryZone zone
    ) {
        return new DeliveryZoneResponse(
                zone.getId(),
                zone.getNeighborhood(),
                zone.getDeliveryFee(),
                zone.getDisplayOrder(),
                zone.getActive()
        );
    }
}