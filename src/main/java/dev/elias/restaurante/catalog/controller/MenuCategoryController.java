package dev.elias.restaurante.catalog.controller;

import dev.elias.restaurante.catalog.dto.MenuCategoryResponse;
import dev.elias.restaurante.catalog.service.MenuCategoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/menu-categories")
public class MenuCategoryController {

    private final MenuCategoryService service;

    public MenuCategoryController(MenuCategoryService service) {
        this.service = service;
    }

    @GetMapping
    public List<MenuCategoryResponse> findAll() {
        return service.findAll();
    }
}