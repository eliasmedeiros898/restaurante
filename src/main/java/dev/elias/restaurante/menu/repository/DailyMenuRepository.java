package dev.elias.restaurante.menu.repository;

import dev.elias.restaurante.menu.entity.DailyMenu;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DailyMenuRepository
        extends JpaRepository<DailyMenu, Long> {

    boolean existsByMenuDate(LocalDate menuDate);

    @EntityGraph(attributePaths = {
            "sourceTemplate",
            "items",
            "items.menuItem",
            "items.menuItem.category"
    })
    Optional<DailyMenu> findOneById(Long id);

    @EntityGraph(attributePaths = {
            "sourceTemplate",
            "items",
            "items.menuItem",
            "items.menuItem.category"
    })
    Optional<DailyMenu> findByMenuDate(LocalDate menuDate);

    @EntityGraph(attributePaths = {
            "sourceTemplate",
            "items",
            "items.menuItem",
            "items.menuItem.category"
    })
    List<DailyMenu> findAllByOrderByMenuDateDesc();

    @EntityGraph(attributePaths = {
            "sourceTemplate",
            "items",
            "items.menuItem",
            "items.menuItem.category"
    })
    List<DailyMenu>
    findByMenuDateGreaterThanEqualOrderByMenuDateAsc(
            LocalDate startDate
    );
}

