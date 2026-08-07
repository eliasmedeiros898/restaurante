package dev.elias.restaurante.order.dto;

import dev.elias.restaurante.order.entity.OrderItemSelection;

public record OrderItemSelectionResponse(
        Long id,
        Long categoryId,
        String categoryCode,
        String categoryName,
        Long menuItemId,
        String menuItemName,
        Integer quantity
) {

    public static OrderItemSelectionResponse from(
            OrderItemSelection selection
    ) {
        return new OrderItemSelectionResponse(
                selection.getId(),
                selection.getCategory().getId(),
                selection.getCategory().getCode(),
                selection.getCategory().getName(),
                selection.getMenuItem().getId(),
                selection.getMenuItem().getName(),
                selection.getQuantity()
        );
    }
}