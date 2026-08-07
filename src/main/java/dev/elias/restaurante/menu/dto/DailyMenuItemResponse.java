package dev.elias.restaurante.menu.dto;

import dev.elias.restaurante.menu.entity.DailyMenuItem;

public record DailyMenuItemResponse(
        Long id,
        Long menuItemId,
        String menuItemName,
        Long categoryId,
        String categoryCode,
        String categoryName,
        Boolean basicPlanEligible,
        Boolean available,
        String unavailableReason,
        Integer displayOrder
) {

    public static DailyMenuItemResponse from(
            DailyMenuItem dailyMenuItem
    ) {
        var menuItem = dailyMenuItem.getMenuItem();
        var category = menuItem.getCategory();

        Boolean basicPlanEligible =
                "MEAT".equals(category.getCode())
                        ? menuItem.getBasicPlanEligible()
                        : null;

        return new DailyMenuItemResponse(
                dailyMenuItem.getId(),
                menuItem.getId(),
                menuItem.getName(),
                category.getId(),
                category.getCode(),
                category.getName(),
                basicPlanEligible,
                dailyMenuItem.getAvailable(),
                dailyMenuItem.getUnavailableReason(),
                dailyMenuItem.getDisplayOrder()
        );
    }
}