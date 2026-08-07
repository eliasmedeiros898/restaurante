package dev.elias.restaurante.report.repository;

import dev.elias.restaurante.order.entity.*;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface ReportQueryRepository
        extends Repository<Order, Long> {

    /*
    |--------------------------------------------------------------------------
    | FATURAMENTO
    |--------------------------------------------------------------------------
    */

    @Query("""
            select coalesce(sum(o.total), 0)
            from Order o
            where o.dailyMenu.menuDate between :startDate and :endDate
              and o.paymentStatus = :paymentStatus
            """)
    BigDecimal sumRevenue(
            @Param("startDate")
            LocalDate startDate,

            @Param("endDate")
            LocalDate endDate,

            @Param("paymentStatus")
            PaymentStatus paymentStatus
    );

    /*
    |--------------------------------------------------------------------------
    | QUANTIDADE DE PEDIDOS
    |--------------------------------------------------------------------------
    */

    @Query("""
            select count(o)
            from Order o
            where o.dailyMenu.menuDate between :startDate and :endDate
              and o.status <> :cancelledStatus
            """)
    Long countValidOrders(
            @Param("startDate")
            LocalDate startDate,

            @Param("endDate")
            LocalDate endDate,

            @Param("cancelledStatus")
            OrderStatus cancelledStatus
    );

    @Query("""
            select count(o)
            from Order o
            where o.dailyMenu.menuDate between :startDate and :endDate
              and o.paymentStatus = :paymentStatus
            """)
    Long countPaidOrders(
            @Param("startDate")
            LocalDate startDate,

            @Param("endDate")
            LocalDate endDate,

            @Param("paymentStatus")
            PaymentStatus paymentStatus
    );

    @Query("""
            select count(o)
            from Order o
            where o.dailyMenu.menuDate between :startDate and :endDate
              and o.status = :cancelledStatus
            """)
    Long countCancelledOrders(
            @Param("startDate")
            LocalDate startDate,

            @Param("endDate")
            LocalDate endDate,

            @Param("cancelledStatus")
            OrderStatus cancelledStatus
    );

    /*
    |--------------------------------------------------------------------------
    | TAXAS DE ENTREGA
    |--------------------------------------------------------------------------
    */

    @Query("""
            select coalesce(sum(o.deliveryFee), 0)
            from Order o
            where o.dailyMenu.menuDate between :startDate and :endDate
              and o.paymentStatus = :paymentStatus
              and o.fulfillmentType = :fulfillmentType
            """)
    BigDecimal sumDeliveryFees(
            @Param("startDate")
            LocalDate startDate,

            @Param("endDate")
            LocalDate endDate,

            @Param("paymentStatus")
            PaymentStatus paymentStatus,

            @Param("fulfillmentType")
            FulfillmentType fulfillmentType
    );

    /*
    |--------------------------------------------------------------------------
    | FATURAMENTO POR DIA
    |--------------------------------------------------------------------------
    */

    @Query("""
            select
                o.dailyMenu.menuDate,
                coalesce(sum(o.total), 0),
                count(o)
            from Order o
            where o.dailyMenu.menuDate between :startDate and :endDate
              and o.paymentStatus = :paymentStatus
            group by o.dailyMenu.menuDate
            order by o.dailyMenu.menuDate asc
            """)
    List<Object[]> findDailyRevenue(
            @Param("startDate")
            LocalDate startDate,

            @Param("endDate")
            LocalDate endDate,

            @Param("paymentStatus")
            PaymentStatus paymentStatus
    );

    /*
    |--------------------------------------------------------------------------
    | CANAL
    |--------------------------------------------------------------------------
    */

    @Query("""
            select
                o.channel,
                count(o)
            from Order o
            where o.dailyMenu.menuDate between :startDate and :endDate
              and o.status <> :cancelledStatus
            group by o.channel
            order by count(o) desc
            """)
    List<Object[]> findOrdersByChannel(
            @Param("startDate")
            LocalDate startDate,

            @Param("endDate")
            LocalDate endDate,

            @Param("cancelledStatus")
            OrderStatus cancelledStatus
    );

    /*
    |--------------------------------------------------------------------------
    | ENTREGA / RETIRADA
    |--------------------------------------------------------------------------
    */

    @Query("""
            select
                o.fulfillmentType,
                count(o)
            from Order o
            where o.dailyMenu.menuDate between :startDate and :endDate
              and o.status <> :cancelledStatus
            group by o.fulfillmentType
            order by count(o) desc
            """)
    List<Object[]> findOrdersByFulfillmentType(
            @Param("startDate")
            LocalDate startDate,

            @Param("endDate")
            LocalDate endDate,

            @Param("cancelledStatus")
            OrderStatus cancelledStatus
    );

    /*
    |--------------------------------------------------------------------------
    | PAGAMENTOS
    |--------------------------------------------------------------------------
    */

    @Query("""
            select
                o.paymentMethod,
                count(o)
            from Order o
            where o.dailyMenu.menuDate between :startDate and :endDate
              and o.paymentStatus = :paymentStatus
            group by o.paymentMethod
            order by count(o) desc
            """)
    List<Object[]> findOrdersByPaymentMethod(
            @Param("startDate")
            LocalDate startDate,

            @Param("endDate")
            LocalDate endDate,

            @Param("paymentStatus")
            PaymentStatus paymentStatus
    );

    /*
    |--------------------------------------------------------------------------
    | TIPO DE REFEIÇÃO
    |--------------------------------------------------------------------------
    */

    @Query("""
            select
                item.mealType,
                sum(item.quantity)
            from Order o
            join o.items item
            where o.dailyMenu.menuDate between :startDate and :endDate
              and o.status <> :cancelledStatus
            group by item.mealType
            order by sum(item.quantity) desc
            """)
    List<Object[]> findMealTypes(
            @Param("startDate")
            LocalDate startDate,

            @Param("endDate")
            LocalDate endDate,

            @Param("cancelledStatus")
            OrderStatus cancelledStatus
    );

    /*
    |--------------------------------------------------------------------------
    | PLANOS MAIS VENDIDOS
    |--------------------------------------------------------------------------
    */

    @Query("""
            select
                item.mealPlan.id,
                item.mealPlan.name,
                sum(item.quantity)
            from Order o
            join o.items item
            where o.dailyMenu.menuDate between :startDate and :endDate
              and o.status <> :cancelledStatus
            group by
                item.mealPlan.id,
                item.mealPlan.name
            order by sum(item.quantity) desc
            """)
    List<Object[]> findTopMealPlans(
            @Param("startDate")
            LocalDate startDate,

            @Param("endDate")
            LocalDate endDate,

            @Param("cancelledStatus")
            OrderStatus cancelledStatus,

            Pageable pageable
    );

    /*
    |--------------------------------------------------------------------------
    | CARNES MAIS ESCOLHIDAS
    |--------------------------------------------------------------------------
    */

    @Query("""
            select
                selection.menuItem.id,
                selection.menuItem.name,
                sum(selection.quantity * item.quantity)
            from Order o
            join o.items item
            join item.selections selection
            where o.dailyMenu.menuDate between :startDate and :endDate
              and o.status <> :cancelledStatus
              and selection.category.code = :categoryCode
            group by
                selection.menuItem.id,
                selection.menuItem.name
            order by
                sum(selection.quantity * item.quantity) desc
            """)
    List<Object[]> findTopMenuItemsByCategory(
            @Param("startDate")
            LocalDate startDate,

            @Param("endDate")
            LocalDate endDate,

            @Param("cancelledStatus")
            OrderStatus cancelledStatus,

            @Param("categoryCode")
            String categoryCode,

            Pageable pageable
    );
}