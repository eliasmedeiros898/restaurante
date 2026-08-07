package dev.elias.restaurante.menu.controller;

import dev.elias.restaurante.menu.dto.CreateDailyMenuRequest;
import dev.elias.restaurante.menu.dto.DailyMenuResponse;
import dev.elias.restaurante.menu.dto.UpdateDailyMenuItemRequest;
import dev.elias.restaurante.menu.service.DailyMenuService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/daily-menus")
public class DailyMenuController {

    private final DailyMenuService service;

    public DailyMenuController(DailyMenuService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DailyMenuResponse create(
            @Valid @RequestBody CreateDailyMenuRequest request
    ) {
        return service.create(request);
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

    @GetMapping("/date/{date}")
    public DailyMenuResponse findByDate(
            @PathVariable LocalDate date
    ) {
        return service.findByDate(date);
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

    @PatchMapping("/{dailyMenuId}/items/{itemId}/availability")
    public DailyMenuResponse updateItemAvailability(
            @PathVariable Long dailyMenuId,
            @PathVariable Long itemId,
            @Valid @RequestBody UpdateDailyMenuItemRequest request
    ) {
        return service.updateItemAvailability(
                dailyMenuId,
                itemId,
                request
        );
    }
}