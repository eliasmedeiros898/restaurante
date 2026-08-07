package dev.elias.restaurante.catalog.service;

import dev.elias.restaurante.catalog.dto.AdminMenuItemResponse;
import dev.elias.restaurante.catalog.repository.MenuItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdminMenuItemService {

    private final MenuItemRepository repository;

    public AdminMenuItemService(
            MenuItemRepository repository
    ) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<AdminMenuItemResponse>
    findAllActive() {
        return repository
                .findByActiveTrueOrderByCategoryDisplayOrderAscNameAsc()
                .stream()
                .map(AdminMenuItemResponse::from)
                .toList();
    }
}