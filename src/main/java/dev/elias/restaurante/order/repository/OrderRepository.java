package dev.elias.restaurante.order.repository;

import dev.elias.restaurante.order.entity.Order;
import dev.elias.restaurante.order.entity.OrderChannel;
import dev.elias.restaurante.order.entity.OrderStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface OrderRepository
        extends JpaRepository<Order, Long> {

    @EntityGraph(attributePaths = {
            "dailyMenu",
            "items",
            "items.mealPlan"
    })
    Optional<Order> findOneById(
            Long id
    );

    @EntityGraph(attributePaths = {
            "dailyMenu",
            "items",
            "items.mealPlan"
    })
    Optional<Order> findByOrderNumber(
            Long orderNumber
    );

    /*
     * Todos os pedidos.
     */
    @EntityGraph(attributePaths = {
            "dailyMenu",
            "items",
            "items.mealPlan"
    })
    List<Order> findAllByOrderByCreatedAtDesc();

    /*
     * Somente pela data do cardápio.
     */
    @EntityGraph(attributePaths = {
            "dailyMenu",
            "items",
            "items.mealPlan"
    })
    List<Order>
    findByDailyMenuMenuDateOrderByCreatedAtDesc(
            LocalDate menuDate
    );

    /*
     * Data e status.
     */
    @EntityGraph(attributePaths = {
            "dailyMenu",
            "items",
            "items.mealPlan"
    })
    List<Order>
    findByDailyMenuMenuDateAndStatusOrderByCreatedAtDesc(
            LocalDate menuDate,
            OrderStatus status
    );

    /*
     * Data e canal.
     */
    @EntityGraph(attributePaths = {
            "dailyMenu",
            "items",
            "items.mealPlan"
    })
    List<Order>
    findByDailyMenuMenuDateAndChannelOrderByCreatedAtDesc(
            LocalDate menuDate,
            OrderChannel channel
    );

    /*
     * Data, status e canal.
     */
    @EntityGraph(attributePaths = {
            "dailyMenu",
            "items",
            "items.mealPlan"
    })
    List<Order>
    findByDailyMenuMenuDateAndStatusAndChannelOrderByCreatedAtDesc(
            LocalDate menuDate,
            OrderStatus status,
            OrderChannel channel
    );

    /*
     * Consultas sem data, mantidas para outros
     * usos administrativos.
     */
    @EntityGraph(attributePaths = {
            "dailyMenu",
            "items",
            "items.mealPlan"
    })
    List<Order> findByStatusOrderByCreatedAtDesc(
            OrderStatus status
    );

    @EntityGraph(attributePaths = {
            "dailyMenu",
            "items",
            "items.mealPlan"
    })
    List<Order> findByChannelOrderByCreatedAtDesc(
            OrderChannel channel
    );

    @EntityGraph(attributePaths = {
            "dailyMenu",
            "items",
            "items.mealPlan"
    })
    List<Order>
    findByStatusAndChannelOrderByCreatedAtDesc(
            OrderStatus status,
            OrderChannel channel
    );
}