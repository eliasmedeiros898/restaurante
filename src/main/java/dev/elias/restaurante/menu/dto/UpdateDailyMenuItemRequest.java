package dev.elias.restaurante.menu.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateDailyMenuItemRequest(

        @NotNull(message = "A disponibilidade é obrigatória")
        Boolean available,

        @Size(
                max = 255,
                message = "O motivo deve possuir no máximo 255 caracteres"
        )
        String unavailableReason
) {
}