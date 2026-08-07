package dev.elias.restaurante.menu.dto;

import dev.elias.restaurante.catalog.entity.MealPlan;

import java.math.BigDecimal;

public record PublicMealPlanResponse(
        Long id,
        String code,
        String name,
        BigDecimal price,
        Integer meatQuantity,
        Boolean basicMeatsOnly
) {

    public static PublicMealPlanResponse from(MealPlan mealPlan) {
        return new PublicMealPlanResponse(
                mealPlan.getId(),
                mealPlan.getCode(),
                mealPlan.getName(),
                mealPlan.getPrice(),
                mealPlan.getMeatQuantity(),
                mealPlan.getBasicMeatsOnly()
        );
    }
}