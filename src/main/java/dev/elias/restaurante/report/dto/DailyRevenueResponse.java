package dev.elias.restaurante.report.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DailyRevenueResponse(
        LocalDate date,
        BigDecimal revenue,
        Long orders
) {
}