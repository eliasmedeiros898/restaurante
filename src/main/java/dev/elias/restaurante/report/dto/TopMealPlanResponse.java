package dev.elias.restaurante.report.dto;

public record TopMealPlanResponse(
        Long mealPlanId,
        String name,
        Long quantity
) {
}