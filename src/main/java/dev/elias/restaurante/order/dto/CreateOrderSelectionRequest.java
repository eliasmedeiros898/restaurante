package dev.elias.restaurante.order.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CreateOrderSelectionRequest(

        @NotNull(
                message = "A categoria é obrigatória"
        )
        Long categoryId,

        @NotNull(
                message = "O item do cardápio é obrigatório"
        )
        Long menuItemId,

        @NotNull(
                message = "A quantidade da seleção é obrigatória"
        )
        @Min(
                value = 1,
                message = "A quantidade deve ser pelo menos 1"
        )
        Integer quantity
) {
}