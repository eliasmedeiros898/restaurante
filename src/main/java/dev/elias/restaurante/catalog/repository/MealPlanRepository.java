package dev.elias.restaurante.catalog.repository;

import dev.elias.restaurante.catalog.entity.MealPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MealPlanRepository
        extends JpaRepository<MealPlan, Long> {

    Optional<MealPlan> findByCode(
            String code
    );

    Optional<MealPlan> findByCodeIgnoreCase(
            String code
    );

    boolean existsByCodeIgnoreCase(
            String code
    );

    boolean existsByCodeIgnoreCaseAndIdNot(
            String code,
            Long id
    );

    List<MealPlan>
    findByActiveTrueOrderByPriceAsc();

    List<MealPlan>
    findAllByOrderByPriceAsc();
}