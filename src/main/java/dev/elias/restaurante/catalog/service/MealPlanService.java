package dev.elias.restaurante.catalog.service;

import dev.elias.restaurante.catalog.dto.MealPlanRequest;
import dev.elias.restaurante.catalog.dto.MealPlanResponse;
import dev.elias.restaurante.catalog.entity.MealPlan;
import dev.elias.restaurante.catalog.repository.MealPlanRepository;
import dev.elias.restaurante.shared.exception.ResourceNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;

@Service
public class MealPlanService {

    private final MealPlanRepository repository;

    public MealPlanService(
            MealPlanRepository repository
    ) {
        this.repository = repository;
    }

    /*
    |--------------------------------------------------------------------------
    | CONSULTAS
    |--------------------------------------------------------------------------
    */

    @Transactional(readOnly = true)
    public List<MealPlanResponse> findAll(
            Boolean activeOnly
    ) {
        List<MealPlan> plans;

        if (Boolean.TRUE.equals(
                activeOnly
        )) {
            plans =
                    repository
                            .findByActiveTrueOrderByPriceAsc();
        } else {
            plans =
                    repository
                            .findAllByOrderByPriceAsc();
        }

        return plans.stream()
                .map(MealPlanResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public MealPlanResponse findById(
            Long id
    ) {
        return MealPlanResponse.from(
                findEntityById(id)
        );
    }

    @Transactional(readOnly = true)
    public MealPlanResponse findByCode(
            String code
    ) {
        String normalizedCode =
                normalizeCode(code);

        MealPlan plan =
                repository
                        .findByCodeIgnoreCase(
                                normalizedCode
                        )
                        .orElseThrow(
                                () ->
                                        new ResourceNotFoundException(
                                                "Plano de refeição não encontrado: "
                                                        + code
                                        )
                        );

        return MealPlanResponse.from(
                plan
        );
    }

    @Transactional(readOnly = true)
    public MealPlan findEntityById(
            Long id
    ) {
        return repository
                .findById(id)
                .orElseThrow(
                        () ->
                                new ResourceNotFoundException(
                                        "Plano de refeição não encontrado: "
                                                + id
                                )
                );
    }

    /*
    |--------------------------------------------------------------------------
    | ADMINISTRAÇÃO
    |--------------------------------------------------------------------------
    */

    @Transactional
    public MealPlanResponse create(
            MealPlanRequest request
    ) {
        String normalizedCode =
                normalizeCode(
                        request.code()
                );

        validateCodeForCreate(
                normalizedCode
        );

        validateBusinessRules(
                request.price(),
                request.meatQuantity()
        );

        MealPlan plan =
                new MealPlan(
                        normalizedCode,
                        normalizeName(
                                request.name()
                        ),
                        request.price(),
                        request.meatQuantity(),
                        request.basicMeatsOnly(),
                        request.active() == null
                                ? true
                                : request.active()
                );

        MealPlan saved =
                repository.save(plan);

        return MealPlanResponse.from(
                saved
        );
    }

    @Transactional
    public MealPlanResponse update(
            Long id,
            MealPlanRequest request
    ) {
        MealPlan plan =
                findEntityById(id);

        String normalizedCode =
                normalizeCode(
                        request.code()
                );

        validateCodeForUpdate(
                normalizedCode,
                id
        );

        validateBusinessRules(
                request.price(),
                request.meatQuantity()
        );

        plan.update(
                normalizedCode,
                normalizeName(
                        request.name()
                ),
                request.price(),
                request.meatQuantity(),
                request.basicMeatsOnly()
        );

        /*
         * Se "active" vier no PUT,
         * também atualizamos o status.
         *
         * Caso seja null, mantemos o
         * status atual.
         */
        if (request.active() != null) {
            plan.setActive(
                    request.active()
            );
        }

        MealPlan saved =
                repository.save(plan);

        return MealPlanResponse.from(
                saved
        );
    }

    @Transactional
    public MealPlanResponse updateActive(
            Long id,
            Boolean active
    ) {
        if (active == null) {
            throw new IllegalArgumentException(
                    "O status do plano é obrigatório."
            );
        }

        MealPlan plan =
                findEntityById(id);

        plan.setActive(active);

        MealPlan saved =
                repository.save(plan);

        return MealPlanResponse.from(
                saved
        );
    }

    /*
    |--------------------------------------------------------------------------
    | VALIDAÇÕES
    |--------------------------------------------------------------------------
    */

    private void validateCodeForCreate(
            String code
    ) {
        if (
                repository
                        .existsByCodeIgnoreCase(
                                code
                        )
        ) {
            throw new IllegalArgumentException(
                    "Já existe um plano com o código: "
                            + code
            );
        }
    }

    private void validateCodeForUpdate(
            String code,
            Long id
    ) {
        if (
                repository
                        .existsByCodeIgnoreCaseAndIdNot(
                                code,
                                id
                        )
        ) {
            throw new IllegalArgumentException(
                    "Já existe outro plano com o código: "
                            + code
            );
        }
    }

    private void validateBusinessRules(
            BigDecimal price,
            Integer meatQuantity
    ) {
        if (
                price == null ||
                        price.compareTo(
                                BigDecimal.ZERO
                        ) <= 0
        ) {
            throw new IllegalArgumentException(
                    "O preço do plano deve ser maior que zero."
            );
        }

        if (
                meatQuantity == null ||
                        meatQuantity < 1
        ) {
            throw new IllegalArgumentException(
                    "O plano deve permitir pelo menos uma carne."
            );
        }
    }

    /*
    |--------------------------------------------------------------------------
    | NORMALIZAÇÃO
    |--------------------------------------------------------------------------
    */

    private String normalizeCode(
            String code
    ) {
        if (code == null) {
            return "";
        }

        return code
                .trim()
                .toUpperCase(
                        Locale.ROOT
                )
                .replaceAll(
                        "\\s+",
                        "_"
                );
    }

    private String normalizeName(
            String name
    ) {
        if (name == null) {
            return "";
        }

        return name.trim();
    }
}