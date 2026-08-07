package dev.elias.restaurante.order.dto;

import dev.elias.restaurante.order.entity.MealType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CreateOrderItemRequest(

        @NotNull(message = "O plano da refeição é obrigatório")
        Long mealPlanId,

        @NotNull(message = "O tipo da refeição é obrigatório")
        MealType mealType,

        @NotNull(message = "A quantidade é obrigatória")
        @Min(value = 1, message = "A quantidade deve ser pelo menos 1")
        Integer quantity,

        @NotEmpty(message = "Selecione os itens da refeição")
        List<@Valid CreateOrderSelectionRequest> selections,

        @Size(
                max = 500,
                message = "A observação deve possuir no máximo 500 caracteres"
        )
        String itemNotes
) {
}