package dev.elias.restaurante.catalog.dto;

import dev.elias.restaurante.catalog.entity.MenuItem;

public record MenuItemResponse(
        Long id,
        String name,
        String description,
        Boolean basicPlanEligible,
        Boolean active,
        Long categoryId,
        String categoryCode,
        String categoryName
) {

    public static MenuItemResponse from(MenuItem item) {
        var category = item.getCategory();

        Boolean basicPlanEligible =
                "MEAT".equals(category.getCode())
                        ? item.getBasicPlanEligible()
                        : null;

        return new MenuItemResponse(
                item.getId(),
                item.getName(),
                item.getDescription(),
                basicPlanEligible,
                item.getActive(),
                category.getId(),
                category.getCode(),
                category.getName()
        );
    }
}