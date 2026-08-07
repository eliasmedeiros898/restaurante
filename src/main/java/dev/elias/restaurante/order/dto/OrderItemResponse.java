package dev.elias.restaurante.order.dto;

import dev.elias.restaurante.order.entity.OrderItem;

import java.math.BigDecimal;
import java.util.List;

public record OrderItemResponse(
        Long id,
        Long mealPlanId,
        String mealPlanName,
        String mealType,
        Integer quantity,
        BigDecimal unitPrice,
        BigDecimal subtotal,
        String notes,
        List<OrderItemSelectionResponse> selections
) {

    public static OrderItemResponse from(OrderItem item) {
        return new OrderItemResponse(
                item.getId(),
                item.getMealPlan().getId(),
                item.getMealPlan().getName(),
                item.getMealType().name(),
                item.getQuantity(),
                item.getUnitPrice(),
                item.getSubtotal(),
                item.getNotes(),
                item.getSelections()
                        .stream()
                        .map(OrderItemSelectionResponse::from)
                        .toList()
        );
    }
}