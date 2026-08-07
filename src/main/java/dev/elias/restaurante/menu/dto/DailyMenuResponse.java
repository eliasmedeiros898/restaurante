package dev.elias.restaurante.menu.dto;

import dev.elias.restaurante.menu.entity.DailyMenu;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record DailyMenuResponse(
        Long id,
        LocalDate menuDate,
        String dayOfWeek,
        Boolean open,
        LocalTime openingTime,
        LocalTime closingTime,
        Long sourceTemplateId,
        String sourceTemplateName,
        String notes,
        List<DailyMenuItemResponse> items
) {

    public static DailyMenuResponse from(DailyMenu menu) {
        Long templateId = menu.getSourceTemplate() != null
                ? menu.getSourceTemplate().getId()
                : null;

        String templateName = menu.getSourceTemplate() != null
                ? menu.getSourceTemplate().getName()
                : null;

        List<DailyMenuItemResponse> items = menu
                .getItems()
                .stream()
                .map(DailyMenuItemResponse::from)
                .toList();

        return new DailyMenuResponse(
                menu.getId(),
                menu.getMenuDate(),
                menu.getDayOfWeek().name(),
                menu.getOpen(),
                menu.getOpeningTime(),
                menu.getClosingTime(),
                templateId,
                templateName,
                menu.getNotes(),
                items
        );
    }
}