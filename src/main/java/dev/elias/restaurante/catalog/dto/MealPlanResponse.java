package dev.elias.restaurante.catalog.dto;

import dev.elias.restaurante.catalog.entity.MealPlan;

import java.math.BigDecimal;

public record MealPlanResponse(
        Long id,
        String code,
        String name,
        BigDecimal price,
        Integer meatQuantity,
        Boolean basicMeatsOnly,
        Boolean active
) {

    public static MealPlanResponse from(MealPlan plan) {
        return new MealPlanResponse(
                plan.getId(),
                plan.getCode(),
                plan.getName(),
                plan.getPrice(),
                plan.getMeatQuantity(),
                plan.getBasicMeatsOnly(),
                plan.getActive()
        );
    }
}