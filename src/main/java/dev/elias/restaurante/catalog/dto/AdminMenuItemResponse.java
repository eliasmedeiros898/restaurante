package dev.elias.restaurante.catalog.dto;

import dev.elias.restaurante.catalog.entity.MenuItem;

public record AdminMenuItemResponse(
        Long id,
        String name,
        String description,
        Long categoryId,
        String categoryCode,
        String categoryName,
        Boolean basicPlanEligible,
        Boolean active
) {

    public static AdminMenuItemResponse from(
            MenuItem item
    ) {
        return new AdminMenuItemResponse(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getCategory().getId(),
                item.getCategory().getCode(),
                item.getCategory().getName(),
                item.getBasicPlanEligible(),
                item.getActive()
        );
    }
}