package dev.elias.restaurante.menu.repository;

import dev.elias.restaurante.menu.entity.DayOfWeekName;
import dev.elias.restaurante.menu.entity.MenuTemplate;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MenuTemplateRepository
        extends JpaRepository<MenuTemplate, Long> {

    @EntityGraph(attributePaths = {
            "items",
            "items.menuItem",
            "items.menuItem.category"
    })
    List<MenuTemplate> findAllByOrderByDayOfWeekAsc();

    @EntityGraph(attributePaths = {
            "items",
            "items.menuItem",
            "items.menuItem.category"
    })
    Optional<MenuTemplate> findByDayOfWeek(
            DayOfWeekName dayOfWeek
    );

    @EntityGraph(attributePaths = {
            "items",
            "items.menuItem",
            "items.menuItem.category"
    })
    Optional<MenuTemplate> findOneById(Long id);

    @EntityGraph(attributePaths = {
            "items",
            "items.menuItem",
            "items.menuItem.category"
    })
    Optional<MenuTemplate> findByDayOfWeekAndActiveTrue(
            DayOfWeekName dayOfWeek
    );
}