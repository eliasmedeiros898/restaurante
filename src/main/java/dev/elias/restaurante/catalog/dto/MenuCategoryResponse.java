package dev.elias.restaurante.catalog.dto;

import dev.elias.restaurante.catalog.entity.MenuCategory;

public record MenuCategoryResponse(
        Long id,
        String code,
        String name,
        Integer displayOrder,
        Integer minimumSelections,
        Integer maximumSelections,
        Boolean required,
        Boolean active
) {

    public static MenuCategoryResponse from(MenuCategory category) {
        return new MenuCategoryResponse(
                category.getId(),
                category.getCode(),
                category.getName(),
                category.getDisplayOrder(),
                category.getMinimumSelections(),
                category.getMaximumSelections(),
                category.getRequired(),
                category.getActive()
        );
    }
}