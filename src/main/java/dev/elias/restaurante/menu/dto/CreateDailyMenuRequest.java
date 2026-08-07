package dev.elias.restaurante.menu.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalTime;

public record CreateDailyMenuRequest(

        @NotNull(message = "A data do cardápio é obrigatória")
        LocalDate menuDate,

        LocalTime openingTime,

        LocalTime closingTime,

        @Size(
                max = 500,
                message = "As observações devem possuir no máximo 500 caracteres"
        )
        String notes
) {
}