package dev.elias.restaurante.report.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record ReportSummaryResponse(

        LocalDate startDate,
        LocalDate endDate,

        BigDecimal revenue,

        Long orderCount,
        Long paidOrderCount,
        Long cancelledOrderCount,

        BigDecimal averageTicket,
        BigDecimal deliveryFees,

        List<DailyRevenueResponse> dailyRevenue,

        List<ReportBreakdownResponse> channels,

        List<ReportBreakdownResponse> fulfillmentTypes,

        List<ReportBreakdownResponse> payments,

        List<ReportBreakdownResponse> mealTypes,

        List<TopMealPlanResponse> topMealPlans,

        List<TopMenuItemResponse> topMeats
) {
}