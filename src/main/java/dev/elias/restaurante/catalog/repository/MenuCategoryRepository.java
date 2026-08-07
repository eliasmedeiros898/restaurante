package dev.elias.restaurante.catalog.repository;

import dev.elias.restaurante.catalog.entity.MenuCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MenuCategoryRepository
        extends JpaRepository<MenuCategory, Long> {

    Optional<MenuCategory> findByCode(String code);

    List<MenuCategory> findAllByOrderByDisplayOrderAsc();
}