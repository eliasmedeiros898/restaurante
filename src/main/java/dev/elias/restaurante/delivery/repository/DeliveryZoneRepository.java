package dev.elias.restaurante.delivery.repository;

import dev.elias.restaurante.delivery.entity.DeliveryZone;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeliveryZoneRepository
        extends JpaRepository<DeliveryZone, Long> {

    List<DeliveryZone>
    findByActiveTrueOrderByDisplayOrderAscNeighborhoodAsc();

    List<DeliveryZone>
    findAllByOrderByDisplayOrderAscNeighborhoodAsc();
}