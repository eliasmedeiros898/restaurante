package dev.elias.restaurante.catalog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record MenuItemRequest(

        @NotNull(
                message =
                        "A categoria é obrigatória"
        )
        Long categoryId,

        @NotBlank(
                message =
                        "O nome é obrigatório"
        )
        @Size(
                max = 120,
                message =
                        "O nome deve possuir no máximo 120 caracteres"
        )
        String name,

        @Size(
                max = 500,
                message =
                        "A descrição deve possuir no máximo 500 caracteres"
        )
        String description,

        Boolean basicPlanEligible,

        Boolean active
) {
}