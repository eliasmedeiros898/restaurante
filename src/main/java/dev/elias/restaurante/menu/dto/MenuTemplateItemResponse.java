package dev.elias.restaurante.menu.dto;

import dev.elias.restaurante.menu.entity.MenuTemplateItem;

public record MenuTemplateItemResponse(
        Long id,
        Integer displayOrder,
        Long menuItemId,
        String menuItemName,
        Long categoryId,
        String categoryCode,
        String categoryName,
        Boolean basicPlanEligible,
        Boolean active
) {

    public static MenuTemplateItemResponse from(
            MenuTemplateItem templateItem
    ) {
        var menuItem = templateItem.getMenuItem();
        var category = menuItem.getCategory();

        Boolean basicPlanEligible =
                "MEAT".equals(category.getCode())
                        ? menuItem.getBasicPlanEligible()
                        : null;

        return new MenuTemplateItemResponse(
                templateItem.getId(),
                templateItem.getDisplayOrder(),
                menuItem.getId(),
                menuItem.getName(),
                category.getId(),
                category.getCode(),
                category.getName(),
                basicPlanEligible,
                menuItem.getActive()
        );
    }
}