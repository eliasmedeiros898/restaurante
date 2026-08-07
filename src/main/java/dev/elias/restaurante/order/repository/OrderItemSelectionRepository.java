package dev.elias.restaurante.order.repository;

import dev.elias.restaurante.order.entity.OrderItemSelection;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemSelectionRepository
        extends JpaRepository<OrderItemSelection, Long> {

    @EntityGraph(attributePaths = {
            "menuItem",
            "category"
    })
    List<OrderItemSelection> findByOrderItemOrderId(Long orderId);
}