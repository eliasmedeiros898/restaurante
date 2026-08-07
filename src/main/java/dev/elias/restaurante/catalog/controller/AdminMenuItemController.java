package dev.elias.restaurante.catalog.controller;

import dev.elias.restaurante.catalog.dto.MenuItemRequest;
import dev.elias.restaurante.catalog.dto.MenuItemResponse;
import dev.elias.restaurante.catalog.service.MenuItemService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(
        "/api/admin/catalog/menu-items"
)
public class AdminMenuItemController {

    private final MenuItemService service;

    public AdminMenuItemController(
            MenuItemService service
    ) {
        this.service = service;
    }

    @GetMapping
    public List<MenuItemResponse> findAll(
            @RequestParam(
                    required = false
            )
            Long categoryId,

            @RequestParam(
                    required = false,
                    defaultValue = "false"
            )
            Boolean activeOnly
    ) {
        return service.findAll(
                categoryId,
                activeOnly
        );
    }

    @GetMapping("/{id}")
    public MenuItemResponse findById(
            @PathVariable Long id
    ) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(
            HttpStatus.CREATED
    )
    public MenuItemResponse create(
            @Valid
            @RequestBody
            MenuItemRequest request
    ) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public MenuItemResponse update(
            @PathVariable Long id,
            @Valid
            @RequestBody
            MenuItemRequest request
    ) {
        return service.update(
                id,
                request
        );
    }

    @PatchMapping("/{id}/active")
    public MenuItemResponse
    updateActive(
            @PathVariable Long id,
            @RequestParam
            Boolean active
    ) {
        return service.updateActive(
                id,
                active
        );
    }
}