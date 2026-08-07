package dev.elias.restaurante.catalog.repository;

import dev.elias.restaurante.catalog.entity.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import java.util.List;
import java.util.Optional;

public interface MenuItemRepository
        extends JpaRepository<MenuItem, Long> {

    List<MenuItem> findAllByOrderByCategoryDisplayOrderAscNameAsc();

    List<MenuItem> findByCategoryIdOrderByNameAsc(Long categoryId);


    @EntityGraph(attributePaths = "category")
    List<MenuItem> findByActiveTrueOrderByCategoryDisplayOrderAscNameAsc();

    List<MenuItem> findByIdIn(List<Long> ids);

    Optional<MenuItem> findByCategoryIdAndNameIgnoreCase(
            Long categoryId,
            String name
    );
}