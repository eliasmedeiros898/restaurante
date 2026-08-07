package dev.elias.restaurante.catalog.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record MealPlanRequest(

        @NotBlank(
                message = "O código do plano é obrigatório."
        )
        @Size(
                max = 50,
                message = "O código deve possuir no máximo 50 caracteres."
        )
        String code,

        @NotBlank(
                message = "O nome do plano é obrigatório."
        )
        @Size(
                max = 100,
                message = "O nome deve possuir no máximo 100 caracteres."
        )
        String name,

        @NotNull(
                message = "O preço do plano é obrigatório."
        )
        @DecimalMin(
                value = "0.01",
                message = "O preço deve ser maior que zero."
        )
        BigDecimal price,

        @NotNull(
                message = "A quantidade de carnes é obrigatória."
        )
        @Min(
                value = 1,
                message = "O plano deve permitir pelo menos uma carne."
        )
        Integer meatQuantity,

        @NotNull(
                message = "Informe se o plano aceita somente carnes básicas."
        )
        Boolean basicMeatsOnly,

        Boolean active
) {
}