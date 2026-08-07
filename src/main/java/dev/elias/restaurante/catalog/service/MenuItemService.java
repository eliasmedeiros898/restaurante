package dev.elias.restaurante.catalog.service;

import dev.elias.restaurante.catalog.dto.MenuItemRequest;
import dev.elias.restaurante.catalog.dto.MenuItemResponse;
import dev.elias.restaurante.catalog.entity.MenuCategory;
import dev.elias.restaurante.catalog.entity.MenuItem;
import dev.elias.restaurante.catalog.repository.MenuCategoryRepository;
import dev.elias.restaurante.catalog.repository.MenuItemRepository;
import dev.elias.restaurante.shared.exception.BusinessRuleException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MenuItemService {

    private final MenuItemRepository repository;

    private final MenuCategoryRepository
            categoryRepository;

    public MenuItemService(
            MenuItemRepository repository,
            MenuCategoryRepository
                    categoryRepository
    ) {
        this.repository = repository;
        this.categoryRepository =
                categoryRepository;
    }

    @Transactional(readOnly = true)
    public List<MenuItemResponse> findAll(
            Long categoryId,
            Boolean activeOnly
    ) {
        if (categoryId != null) {
            return repository
                    .findByCategoryIdOrderByNameAsc(
                            categoryId
                    )
                    .stream()
                    .filter(item ->
                            !Boolean.TRUE.equals(
                                    activeOnly
                            )
                                    ||
                                    Boolean.TRUE.equals(
                                            item.getActive()
                                    )
                    )
                    .map(
                            MenuItemResponse::from
                    )
                    .toList();
        }

        if (Boolean.TRUE.equals(activeOnly)) {
            return repository
                    .findByActiveTrueOrderByCategoryDisplayOrderAscNameAsc()
                    .stream()
                    .map(
                            MenuItemResponse::from
                    )
                    .toList();
        }

        return repository
                .findAllByOrderByCategoryDisplayOrderAscNameAsc()
                .stream()
                .map(
                        MenuItemResponse::from
                )
                .toList();
    }

    @Transactional(readOnly = true)
    public MenuItemResponse findById(
            Long id
    ) {
        return MenuItemResponse.from(
                findEntity(id)
        );
    }

    @Transactional
    public MenuItemResponse create(
            MenuItemRequest request
    ) {
        MenuCategory category =
                findCategory(
                        request.categoryId()
                );

        String name =
                normalizeName(
                        request.name()
                );

        validateDuplicate(
                category.getId(),
                name,
                null
        );

        Boolean basicPlanEligible =
                resolveBasicPlanEligible(
                        category,
                        request.basicPlanEligible()
                );

        MenuItem item =
                new MenuItem(
                        category,
                        name,
                        normalizeDescription(
                                request.description()
                        ),
                        basicPlanEligible,
                        request.active() == null
                                ? true
                                : request.active()
                );

        MenuItem saved =
                repository.save(item);

        return MenuItemResponse.from(
                saved
        );
    }

    @Transactional
    public MenuItemResponse update(
            Long id,
            MenuItemRequest request
    ) {
        MenuItem item =
                findEntity(id);

        MenuCategory category =
                findCategory(
                        request.categoryId()
                );

        String name =
                normalizeName(
                        request.name()
                );

        validateDuplicate(
                category.getId(),
                name,
                id
        );

        Boolean basicPlanEligible =
                resolveBasicPlanEligible(
                        category,
                        request.basicPlanEligible()
                );

        item.update(
                category,
                name,
                normalizeDescription(
                        request.description()
                ),
                basicPlanEligible
        );

        if (request.active() != null) {
            item.setActive(
                    request.active()
            );
        }

        return MenuItemResponse.from(
                item
        );
    }

    @Transactional
    public MenuItemResponse updateActive(
            Long id,
            Boolean active
    ) {
        if (active == null) {
            throw new BusinessRuleException(
                    "O status do item é obrigatório"
            );
        }

        MenuItem item =
                findEntity(id);

        item.setActive(active);

        return MenuItemResponse.from(
                item
        );
    }

    private MenuItem findEntity(
            Long id
    ) {
        return repository
                .findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Item não encontrado"
                        )
                );
    }

    private MenuCategory findCategory(
            Long categoryId
    ) {
        return categoryRepository
                .findById(categoryId)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Categoria não encontrada"
                        )
                );
    }

    private void validateDuplicate(
            Long categoryId,
            String name,
            Long currentId
    ) {
        boolean exists;

        if (currentId == null) {
            exists =
                    repository
                            .existsByCategoryIdAndNameIgnoreCase(
                                    categoryId,
                                    name
                            );
        } else {
            exists =
                    repository
                            .existsByCategoryIdAndNameIgnoreCaseAndIdNot(
                                    categoryId,
                                    name,
                                    currentId
                            );
        }

        if (exists) {
            throw new BusinessRuleException(
                    "Já existe um item com esse nome nesta categoria"
            );
        }
    }

    private Boolean
    resolveBasicPlanEligible(
            MenuCategory category,
            Boolean requestedValue
    ) {
        if (!"MEAT".equalsIgnoreCase(
                category.getCode()
        )) {
            return false;
        }

        return Boolean.TRUE.equals(
                requestedValue
        );
    }

    private String normalizeName(
            String value
    ) {
        return value.trim();
    }

    private String normalizeDescription(
            String value
    ) {
        if (value == null) {
            return null;
        }

        String normalized =
                value.trim();

        return normalized.isBlank()
                ? null
                : normalized;
    }
}