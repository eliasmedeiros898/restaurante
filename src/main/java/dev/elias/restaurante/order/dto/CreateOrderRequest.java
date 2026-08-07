package dev.elias.restaurante.order.dto;

import dev.elias.restaurante.order.entity.FulfillmentType;
import dev.elias.restaurante.order.entity.OrderChannel;
import dev.elias.restaurante.order.entity.PaymentMethod;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CreateOrderRequest(

        @NotNull(
                message = "O cardápio diário é obrigatório"
        )
        Long dailyMenuId,

        @Size(
                max = 120,
                message = "O nome deve possuir no máximo 120 caracteres"
        )
        String customerName,

        @Size(
                max = 20,
                message = "O telefone deve possuir no máximo 20 caracteres"
        )
        String customerPhone,

        @NotNull(
                message = "O canal do pedido é obrigatório"
        )
        OrderChannel channel,

        @NotNull(
                message = "A forma de pagamento é obrigatória"
        )
        PaymentMethod paymentMethod,

        @NotNull(
                message = "A forma de recebimento é obrigatória"
        )
        FulfillmentType fulfillmentType,

        Long deliveryZoneId,

        @Size(
                max = 160,
                message = "A rua deve possuir no máximo 160 caracteres"
        )
        String deliveryStreet,

        @Size(
                max = 30,
                message = "O número deve possuir no máximo 30 caracteres"
        )
        String deliveryNumber,

        @Size(
                max = 160,
                message = "O complemento deve possuir no máximo 160 caracteres"
        )
        String deliveryComplement,

        @Size(
                max = 255,
                message = "A referência deve possuir no máximo 255 caracteres"
        )
        String deliveryReference,

        @NotEmpty(
                message = "O pedido deve possuir pelo menos um item"
        )
        List<
                @Valid CreateOrderItemRequest
                > items,

        @Size(
                max = 1000,
                message = "A observação deve possuir no máximo 1000 caracteres"
        )
        String notes
) {
}