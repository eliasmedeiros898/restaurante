package dev.elias.restaurante.catalog.repository;

import dev.elias.restaurante.catalog.entity.MenuItem;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MenuItemRepository
        extends JpaRepository<MenuItem, Long> {

    @EntityGraph(
            attributePaths = "category"
    )
    List<MenuItem>
    findAllByOrderByCategoryDisplayOrderAscNameAsc();

    @EntityGraph(
            attributePaths = "category"
    )
    List<MenuItem>
    findByCategoryIdOrderByNameAsc(
            Long categoryId
    );

    @EntityGraph(
            attributePaths = "category"
    )
    List<MenuItem>
    findByActiveTrueOrderByCategoryDisplayOrderAscNameAsc();

    @EntityGraph(
            attributePaths = "category"
    )
    List<MenuItem>
    findByIdIn(
            List<Long> ids
    );

    Optional<MenuItem>
    findByCategoryIdAndNameIgnoreCase(
            Long categoryId,
            String name
    );

    boolean
    existsByCategoryIdAndNameIgnoreCase(
            Long categoryId,
            String name
    );

    boolean
    existsByCategoryIdAndNameIgnoreCaseAndIdNot(
            Long categoryId,
            String name,
            Long id
    );
}