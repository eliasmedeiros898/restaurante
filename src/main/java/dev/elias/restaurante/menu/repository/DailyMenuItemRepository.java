package dev.elias.restaurante.menu.repository;

import dev.elias.restaurante.menu.entity.DailyMenuItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DailyMenuItemRepository
        extends JpaRepository<DailyMenuItem, Long> {

    Optional<DailyMenuItem> findByIdAndDailyMenuId(
            Long itemId,
            Long dailyMenuId
    );
}