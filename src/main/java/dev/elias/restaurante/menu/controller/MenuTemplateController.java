package dev.elias.restaurante.menu.controller;

import dev.elias.restaurante.menu.dto.MenuTemplateResponse;
import dev.elias.restaurante.menu.service.MenuTemplateService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menu-templates")
public class MenuTemplateController {

    private final MenuTemplateService service;

    public MenuTemplateController(
            MenuTemplateService service
    ) {
        this.service = service;
    }

    @GetMapping
    public List<MenuTemplateResponse> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public MenuTemplateResponse findById(
            @PathVariable Long id
    ) {
        return service.findById(id);
    }

    @GetMapping("/day/{dayOfWeek}")
    public MenuTemplateResponse findByDayOfWeek(
            @PathVariable String dayOfWeek
    ) {
        return service.findByDayOfWeek(dayOfWeek);
    }
}