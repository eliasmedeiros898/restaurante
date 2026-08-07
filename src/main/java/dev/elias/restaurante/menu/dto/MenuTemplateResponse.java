package dev.elias.restaurante.menu.dto;

import dev.elias.restaurante.menu.entity.MenuTemplate;

import java.util.List;

public record MenuTemplateResponse(
        Long id,
        String name,
        String dayOfWeek,
        Boolean active,
        List<MenuTemplateItemResponse> items
) {

    public static MenuTemplateResponse from(
            MenuTemplate template
    ) {
        List<MenuTemplateItemResponse> items = template
                .getItems()
                .stream()
                .map(MenuTemplateItemResponse::from)
                .toList();

        return new MenuTemplateResponse(
                template.getId(),
                template.getName(),
                template.getDayOfWeek().name(),
                template.getActive(),
                items
        );
    }
}