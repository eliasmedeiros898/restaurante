package dev.elias.restaurante.catalog.controller;

import dev.elias.restaurante.catalog.dto.MenuItemResponse;
import dev.elias.restaurante.catalog.service.MenuItemService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menu-items")
public class MenuItemController {

    private final MenuItemService service;

    public MenuItemController(MenuItemService service) {
        this.service = service;
    }

    @GetMapping
    public List<MenuItemResponse> findAll(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(defaultValue = "false") Boolean activeOnly
    ) {
        return service.findAll(categoryId, activeOnly);
    }
}