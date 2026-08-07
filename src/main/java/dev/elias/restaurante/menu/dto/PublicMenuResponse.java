package dev.elias.restaurante.menu.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record PublicMenuResponse(
        Long menuId,
        LocalDate menuDate,
        String dayOfWeek,
        Boolean open,
        Boolean currentlyAcceptingOrders,
        LocalTime openingTime,
        LocalTime closingTime,
        String notes,
        List<PublicMenuCategoryResponse> categories,
        List<PublicMealPlanResponse> mealPlans
) {
}