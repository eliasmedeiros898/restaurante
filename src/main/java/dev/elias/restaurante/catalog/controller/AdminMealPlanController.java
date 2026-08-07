package dev.elias.restaurante.catalog.controller;

import dev.elias.restaurante.catalog.dto.MealPlanRequest;
import dev.elias.restaurante.catalog.dto.MealPlanResponse;
import dev.elias.restaurante.catalog.service.MealPlanService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(
        "/api/admin/meal-plans"
)
public class AdminMealPlanController {

    private final MealPlanService service;

    public AdminMealPlanController(
            MealPlanService service
    ) {
        this.service = service;
    }

    /*
    |--------------------------------------------------------------------------
    | LISTAR
    |--------------------------------------------------------------------------
    */

    @GetMapping
    public List<MealPlanResponse> findAll() {
        return service.findAll(false);
    }

    /*
    |--------------------------------------------------------------------------
    | BUSCAR
    |--------------------------------------------------------------------------
    */

    @GetMapping("/{id}")
    public MealPlanResponse findById(
            @PathVariable Long id
    ) {
        return service.findById(id);
    }

    /*
    |--------------------------------------------------------------------------
    | CRIAR
    |--------------------------------------------------------------------------
    */

    @PostMapping
    @ResponseStatus(
            HttpStatus.CREATED
    )
    public MealPlanResponse create(
            @Valid
            @RequestBody
            MealPlanRequest request
    ) {
        return service.create(
                request
        );
    }

    /*
    |--------------------------------------------------------------------------
    | EDITAR
    |--------------------------------------------------------------------------
    */

    @PutMapping("/{id}")
    public MealPlanResponse update(
            @PathVariable Long id,

            @Valid
            @RequestBody
            MealPlanRequest request
    ) {
        return service.update(
                id,
                request
        );
    }

    /*
    |--------------------------------------------------------------------------
    | ATIVAR / DESATIVAR
    |--------------------------------------------------------------------------
    */

    @PatchMapping(
            "/{id}/active"
    )
    public MealPlanResponse updateActive(
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