package dev.elias.restaurante.menu.dto;

import java.util.List;

public record PublicMenuCategoryResponse(
        Long id,
        String code,
        String name,
        Integer displayOrder,
        Integer minimumSelections,
        Integer maximumSelections,
        Boolean required,
        List<PublicMenuItemResponse> items
) {
}