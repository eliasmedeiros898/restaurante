package dev.elias.restaurante.report.service;

import dev.elias.restaurante.order.entity.FulfillmentType;
import dev.elias.restaurante.order.entity.OrderStatus;
import dev.elias.restaurante.order.entity.PaymentStatus;

import dev.elias.restaurante.report.dto.*;
import dev.elias.restaurante.report.repository.ReportQueryRepository;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

import java.time.LocalDate;

import java.util.List;

@Service
public class ReportService {

    private static final String
            MEAT_CATEGORY_CODE =
            "MEAT";

    private static final int
            TOP_LIMIT = 5;

    private final
    ReportQueryRepository repository;

    public ReportService(
            ReportQueryRepository repository
    ) {
        this.repository =
                repository;
    }

    @Transactional(readOnly = true)
    public ReportSummaryResponse getSummary(
            LocalDate startDate,
            LocalDate endDate
    ) {
        validatePeriod(
                startDate,
                endDate
        );

        BigDecimal revenue =
                normalizeMoney(
                        repository
                                .sumRevenue(
                                        startDate,
                                        endDate,
                                        PaymentStatus.PAID
                                )
                );

        long orderCount =
                normalizeCount(
                        repository
                                .countValidOrders(
                                        startDate,
                                        endDate,
                                        OrderStatus.CANCELLED
                                )
                );

        long paidOrderCount =
                normalizeCount(
                        repository
                                .countPaidOrders(
                                        startDate,
                                        endDate,
                                        PaymentStatus.PAID
                                )
                );

        long cancelledOrderCount =
                normalizeCount(
                        repository
                                .countCancelledOrders(
                                        startDate,
                                        endDate,
                                        OrderStatus.CANCELLED
                                )
                );

        BigDecimal deliveryFees =
                normalizeMoney(
                        repository
                                .sumDeliveryFees(
                                        startDate,
                                        endDate,
                                        PaymentStatus.PAID,
                                        FulfillmentType.DELIVERY
                                )
                );

        BigDecimal averageTicket =
                calculateAverageTicket(
                        revenue,
                        paidOrderCount
                );

        List<DailyRevenueResponse>
                dailyRevenue =
                repository
                        .findDailyRevenue(
                                startDate,
                                endDate,
                                PaymentStatus.PAID
                        )
                        .stream()
                        .map(
                                this::mapDailyRevenue
                        )
                        .toList();

        List<ReportBreakdownResponse>
                channels =
                mapBreakdown(
                        repository
                                .findOrdersByChannel(
                                        startDate,
                                        endDate,
                                        OrderStatus.CANCELLED
                                )
                );

        List<ReportBreakdownResponse>
                fulfillmentTypes =
                mapBreakdown(
                        repository
                                .findOrdersByFulfillmentType(
                                        startDate,
                                        endDate,
                                        OrderStatus.CANCELLED
                                )
                );

        List<ReportBreakdownResponse>
                payments =
                mapBreakdown(
                        repository
                                .findOrdersByPaymentMethod(
                                        startDate,
                                        endDate,
                                        PaymentStatus.PAID
                                )
                );

        List<ReportBreakdownResponse>
                mealTypes =
                mapBreakdown(
                        repository
                                .findMealTypes(
                                        startDate,
                                        endDate,
                                        OrderStatus.CANCELLED
                                )
                );

        List<TopMealPlanResponse>
                topMealPlans =
                repository
                        .findTopMealPlans(
                                startDate,
                                endDate,
                                OrderStatus.CANCELLED,
                                PageRequest.of(
                                        0,
                                        TOP_LIMIT
                                )
                        )
                        .stream()
                        .map(
                                this::mapMealPlan
                        )
                        .toList();

        List<TopMenuItemResponse>
                topMeats =
                repository
                        .findTopMenuItemsByCategory(
                                startDate,
                                endDate,
                                OrderStatus.CANCELLED,
                                MEAT_CATEGORY_CODE,
                                PageRequest.of(
                                        0,
                                        TOP_LIMIT
                                )
                        )
                        .stream()
                        .map(
                                this::mapMenuItem
                        )
                        .toList();

        return new ReportSummaryResponse(
                startDate,
                endDate,
                revenue,
                orderCount,
                paidOrderCount,
                cancelledOrderCount,
                averageTicket,
                deliveryFees,
                dailyRevenue,
                channels,
                fulfillmentTypes,
                payments,
                mealTypes,
                topMealPlans,
                topMeats
        );
    }

    private DailyRevenueResponse
    mapDailyRevenue(
            Object[] row
    ) {
        return new DailyRevenueResponse(
                (LocalDate) row[0],

                normalizeMoney(
                        (BigDecimal)
                                row[1]
                ),

                toLong(
                        row[2]
                )
        );
    }

    private List<ReportBreakdownResponse>
    mapBreakdown(
            List<Object[]> rows
    ) {
        return rows
                .stream()
                .map(row ->
                        new ReportBreakdownResponse(
                                String.valueOf(
                                        row[0]
                                ),
                                toLong(
                                        row[1]
                                )
                        )
                )
                .toList();
    }

    private TopMealPlanResponse
    mapMealPlan(
            Object[] row
    ) {
        return new TopMealPlanResponse(
                toLong(
                        row[0]
                ),

                String.valueOf(
                        row[1]
                ),

                toLong(
                        row[2]
                )
        );
    }

    private TopMenuItemResponse
    mapMenuItem(
            Object[] row
    ) {
        return new TopMenuItemResponse(
                toLong(
                        row[0]
                ),

                String.valueOf(
                        row[1]
                ),

                toLong(
                        row[2]
                )
        );
    }

    private BigDecimal
    calculateAverageTicket(
            BigDecimal revenue,
            long paidOrderCount
    ) {
        if (paidOrderCount <= 0) {
            return BigDecimal.ZERO
                    .setScale(
                            2,
                            RoundingMode.HALF_UP
                    );
        }

        return revenue.divide(
                BigDecimal.valueOf(
                        paidOrderCount
                ),
                2,
                RoundingMode.HALF_UP
        );
    }

    private void validatePeriod(
            LocalDate startDate,
            LocalDate endDate
    ) {
        if (
                startDate == null ||
                        endDate == null
        ) {
            throw new IllegalArgumentException(
                    "Informe a data inicial e a data final."
            );
        }

        if (
                endDate.isBefore(
                        startDate
                )
        ) {
            throw new IllegalArgumentException(
                    "A data final não pode ser anterior à data inicial."
            );
        }

        /*
         * Evita consultas administrativas
         * exageradamente grandes.
         *
         * Um ano é mais que suficiente
         * para esta primeira versão.
         */
        if (
                startDate
                        .plusYears(1)
                        .isBefore(
                                endDate
                        )
        ) {
            throw new IllegalArgumentException(
                    "O período máximo para um relatório é de 1 ano."
            );
        }
    }

    private BigDecimal normalizeMoney(
            BigDecimal value
    ) {
        if (value == null) {
            return BigDecimal.ZERO
                    .setScale(
                            2,
                            RoundingMode.HALF_UP
                    );
        }

        return value.setScale(
                2,
                RoundingMode.HALF_UP
        );
    }

    private long normalizeCount(
            Long value
    ) {
        return value != null
                ? value
                : 0L;
    }

    private Long toLong(
            Object value
    ) {
        if (
                value instanceof Number
                        number
        ) {
            return number.longValue();
        }

        return 0L;
    }
}