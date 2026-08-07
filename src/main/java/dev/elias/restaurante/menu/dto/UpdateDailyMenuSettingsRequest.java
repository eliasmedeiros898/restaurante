package dev.elias.restaurante.menu.dto;

import jakarta.validation.constraints.Size;

import java.time.LocalTime;

public record UpdateDailyMenuSettingsRequest(

        LocalTime openingTime,

        LocalTime closingTime,

        @Size(
                max = 500,
                message = "As observações devem possuir no máximo 500 caracteres"
        )
        String notes
) {
}