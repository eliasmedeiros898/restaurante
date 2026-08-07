package dev.elias.restaurante.catalog.service;

import dev.elias.restaurante.catalog.dto.MenuItemResponse;
import dev.elias.restaurante.catalog.repository.MenuItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MenuItemService {

    private final MenuItemRepository repository;

    public MenuItemService(MenuItemRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<MenuItemResponse> findAll(
            Long categoryId,
            Boolean activeOnly
    ) {
        if (categoryId != null) {
            return repository
                    .findByCategoryIdOrderByNameAsc(categoryId)
                    .stream()
                    .filter(item ->
                            !Boolean.TRUE.equals(activeOnly)
                                    || Boolean.TRUE.equals(item.getActive())
                    )
                    .map(MenuItemResponse::from)
                    .toList();
        }

        if (Boolean.TRUE.equals(activeOnly)) {
            return repository
                    .findByActiveTrueOrderByCategoryDisplayOrderAscNameAsc()
                    .stream()
                    .map(MenuItemResponse::from)
                    .toList();
        }

        return repository
                .findAllByOrderByCategoryDisplayOrderAscNameAsc()
                .stream()
                .map(MenuItemResponse::from)
                .toList();
    }
}