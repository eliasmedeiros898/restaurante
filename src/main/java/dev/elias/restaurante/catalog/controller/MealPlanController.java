package dev.elias.restaurante.catalog.controller;

import dev.elias.restaurante.catalog.dto.MealPlanResponse;
import dev.elias.restaurante.catalog.service.MealPlanService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/meal-plans")
public class MealPlanController {

    private final MealPlanService service;

    public MealPlanController(MealPlanService service) {
        this.service = service;
    }

    @GetMapping
    public List<MealPlanResponse> findAll(
            @RequestParam(defaultValue = "true") Boolean activeOnly
    ) {
        return service.findAll(activeOnly);
    }

    @GetMapping("/{id}")
    public MealPlanResponse findById(
            @PathVariable Long id
    ) {
        return service.findById(id);
    }

    @GetMapping("/code/{code}")
    public MealPlanResponse findByCode(
            @PathVariable String code
    ) {
        return service.findByCode(code);
    }
}