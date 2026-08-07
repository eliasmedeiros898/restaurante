package dev.elias.restaurante.menu.controller;

import dev.elias.restaurante.menu.dto.AddDailyMenuItemRequest;
import dev.elias.restaurante.menu.dto.CreateDailyMenuRequest;
import dev.elias.restaurante.menu.dto.DailyMenuResponse;
import dev.elias.restaurante.menu.dto.UpdateDailyMenuItemRequest;
import dev.elias.restaurante.menu.dto.UpdateDailyMenuSettingsRequest;
import dev.elias.restaurante.menu.service.DailyMenuService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/daily-menus")
public class AdminDailyMenuController {

    private final DailyMenuService service;

    public AdminDailyMenuController(
            DailyMenuService service
    ) {
        this.service = service;
    }

    @GetMapping
    public List<DailyMenuResponse> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public DailyMenuResponse findById(
            @PathVariable Long id
    ) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DailyMenuResponse create(
            @Valid
            @RequestBody
            CreateDailyMenuRequest request
    ) {
        return service.create(request);
    }

    @PatchMapping("/{id}/open")
    public DailyMenuResponse openMenu(
            @PathVariable Long id
    ) {
        return service.openMenu(id);
    }

    @PatchMapping("/{id}/close")
    public DailyMenuResponse closeMenu(
            @PathVariable Long id
    ) {
        return service.closeMenu(id);
    }

    @PatchMapping("/{id}/settings")
    public DailyMenuResponse updateSettings(
            @PathVariable Long id,
            @Valid
            @RequestBody
            UpdateDailyMenuSettingsRequest request
    ) {
        return service.updateSettings(
                id,
                request
        );
    }

    @PatchMapping(
            "/{dailyMenuId}/items/{dailyMenuItemId}"
    )
    public DailyMenuResponse updateItemAvailability(
            @PathVariable Long dailyMenuId,
            @PathVariable Long dailyMenuItemId,
            @Valid
            @RequestBody
            UpdateDailyMenuItemRequest request
    ) {
        return service.updateItemAvailability(
                dailyMenuId,
                dailyMenuItemId,
                request
        );
    }

    @PostMapping("/{dailyMenuId}/items")
    @ResponseStatus(HttpStatus.CREATED)
    public DailyMenuResponse addItem(
            @PathVariable Long dailyMenuId,
            @Valid
            @RequestBody
            AddDailyMenuItemRequest request
    ) {
        return service.addItem(
                dailyMenuId,
                request
        );
    }

    @DeleteMapping(
            "/{dailyMenuId}/items/{dailyMenuItemId}"
    )
    public DailyMenuResponse removeItem(
            @PathVariable Long dailyMenuId,
            @PathVariable Long dailyMenuItemId
    ) {
        return service.removeItem(
                dailyMenuId,
                dailyMenuItemId
        );
    }
}