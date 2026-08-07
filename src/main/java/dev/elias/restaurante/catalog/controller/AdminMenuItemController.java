package dev.elias.restaurante.catalog.controller;

import dev.elias.restaurante.catalog.dto.AdminMenuItemResponse;
import dev.elias.restaurante.catalog.service.AdminMenuItemService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/catalog/menu-items")
public class AdminMenuItemController {

    private final AdminMenuItemService service;

    public AdminMenuItemController(
            AdminMenuItemService service
    ) {
        this.service = service;
    }

    @GetMapping
    public List<AdminMenuItemResponse>
    findAllActive() {
        return service.findAllActive();
    }
}