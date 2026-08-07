package dev.elias.restaurante.order.dto;

import dev.elias.restaurante.order.entity.FulfillmentType;
import dev.elias.restaurante.order.entity.Order;
import dev.elias.restaurante.order.entity.OrderChannel;
import dev.elias.restaurante.order.entity.OrderStatus;
import dev.elias.restaurante.order.entity.PaymentMethod;
import dev.elias.restaurante.order.entity.PaymentStatus;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

public record OrderResponse(

        Long id,
        Long orderNumber,
        Long dailyMenuId,

        String customerName,
        String customerPhone,

        OrderChannel channel,
        OrderStatus status,

        PaymentMethod paymentMethod,
        PaymentStatus paymentStatus,

        FulfillmentType fulfillmentType,

        String deliveryNeighborhood,
        String deliveryStreet,
        String deliveryNumber,
        String deliveryComplement,
        String deliveryReference,

        BigDecimal deliveryFee,
        BigDecimal itemsSubtotal,
        BigDecimal total,

        String notes,

        OffsetDateTime createdAt,
        OffsetDateTime updatedAt,
        OffsetDateTime confirmedAt,
        OffsetDateTime preparationStartedAt,
        OffsetDateTime readyAt,
        OffsetDateTime finishedAt,
        OffsetDateTime cancelledAt,

        List<OrderItemResponse> items
) {

    public static OrderResponse from(Order order) {
        List<OrderItemResponse> items =
                order.getItems()
                        .stream()
                        .map(OrderItemResponse::from)
                        .toList();

        BigDecimal itemsSubtotal =
                order.getItems()
                        .stream()
                        .map(item ->
                                item.getSubtotal() != null
                                        ? item.getSubtotal()
                                        : BigDecimal.ZERO
                        )
                        .reduce(
                                BigDecimal.ZERO,
                                BigDecimal::add
                        );

        return new OrderResponse(
                order.getId(),
                order.getDisplayNumber(),
                order.getDailyMenu().getId(),

                order.getCustomerName(),
                order.getCustomerPhone(),

                order.getChannel(),
                order.getStatus(),

                order.getPaymentMethod(),
                order.getPaymentStatus(),

                order.getFulfillmentType(),

                order.getDeliveryNeighborhood(),
                order.getDeliveryStreet(),
                order.getDeliveryNumber(),
                order.getDeliveryComplement(),
                order.getDeliveryReference(),

                valueOrZero(order.getDeliveryFee()),
                itemsSubtotal,
                valueOrZero(order.getTotal()),

                order.getNotes(),

                order.getCreatedAt(),
                order.getUpdatedAt(),
                order.getConfirmedAt(),
                order.getPreparationStartedAt(),
                order.getReadyAt(),
                order.getFinishedAt(),
                order.getCancelledAt(),

                items
        );
    }

    private static BigDecimal valueOrZero(
            BigDecimal value
    ) {
        return value != null
                ? value
                : BigDecimal.ZERO;
    }
}