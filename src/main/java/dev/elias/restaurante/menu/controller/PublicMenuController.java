package dev.elias.restaurante.menu.controller;

import dev.elias.restaurante.menu.dto.PublicMenuResponse;
import dev.elias.restaurante.menu.service.PublicMenuService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public/menu")
public class PublicMenuController {

    private final PublicMenuService service;

    public PublicMenuController(PublicMenuService service) {
        this.service = service;
    }

    @GetMapping("/today")
    public PublicMenuResponse findTodayMenu() {
        return service.findTodayMenu();
    }
}