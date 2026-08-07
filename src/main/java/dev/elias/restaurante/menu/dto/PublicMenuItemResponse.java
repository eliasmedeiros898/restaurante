package dev.elias.restaurante.menu.dto;

import dev.elias.restaurante.menu.entity.DailyMenuItem;

public record PublicMenuItemResponse(
        Long id,
        String name,
        Boolean basicPlanEligible
) {

    public static PublicMenuItemResponse from(
            DailyMenuItem dailyMenuItem
    ) {
        var menuItem = dailyMenuItem.getMenuItem();
        var category = menuItem.getCategory();

        Boolean basicPlanEligible =
                "MEAT".equals(category.getCode())
                        ? menuItem.getBasicPlanEligible()
                        : null;

        return new PublicMenuItemResponse(
                menuItem.getId(),
                menuItem.getName(),
                basicPlanEligible
        );
    }
}