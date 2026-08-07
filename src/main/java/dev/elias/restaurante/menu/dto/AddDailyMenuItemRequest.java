package dev.elias.restaurante.menu.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record AddDailyMenuItemRequest(

        @NotNull(message = "Informe o item do catálogo")
        Long menuItemId,

        @NotNull(message = "Informe a ordem de exibição")
        @PositiveOrZero(
                message = "A ordem não pode ser negativa"
        )
        Integer displayOrder
) {
}