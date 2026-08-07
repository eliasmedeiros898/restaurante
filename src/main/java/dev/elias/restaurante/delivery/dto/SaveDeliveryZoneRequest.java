package dev.elias.restaurante.delivery.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record SaveDeliveryZoneRequest(

        @NotBlank
        @Size(max = 120)
        String neighborhood,

        @NotNull
        @DecimalMin("0.00")
        BigDecimal deliveryFee,

        @NotNull
        @Min(0)
        Integer displayOrder,

        Boolean active
) {
}