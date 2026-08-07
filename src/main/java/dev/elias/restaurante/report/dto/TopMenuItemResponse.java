package dev.elias.restaurante.report.dto;

public record TopMenuItemResponse(
        Long menuItemId,
        String name,
        Long quantity
) {
}