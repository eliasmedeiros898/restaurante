package dev.elias.restaurante.menu.repository;

import dev.elias.restaurante.menu.entity.MenuTemplateItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuTemplateItemRepository
        extends JpaRepository<MenuTemplateItem, Long> {

    List<MenuTemplateItem>
    findByMenuTemplateIdOrderByDisplayOrderAsc(Long menuTemplateId);
}