package dev.elias.restaurante.catalog.service;

import dev.elias.restaurante.catalog.dto.MenuCategoryResponse;
import dev.elias.restaurante.catalog.repository.MenuCategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MenuCategoryService {

    private final MenuCategoryRepository repository;

    public MenuCategoryService(MenuCategoryRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<MenuCategoryResponse> findAll() {
        return repository
                .findAllByOrderByDisplayOrderAsc()
                .stream()
                .map(MenuCategoryResponse::from)
                .toList();
    }
}