package dev.elias.restaurante.menu.service;

import dev.elias.restaurante.catalog.entity.MealPlan;
import dev.elias.restaurante.catalog.entity.MenuCategory;
import dev.elias.restaurante.catalog.repository.MealPlanRepository;
import dev.elias.restaurante.menu.dto.*;
import dev.elias.restaurante.menu.entity.DailyMenu;
import dev.elias.restaurante.menu.entity.DailyMenuItem;
import dev.elias.restaurante.menu.repository.DailyMenuRepository;
import dev.elias.restaurante.shared.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class PublicMenuService {

    private final DailyMenuRepository dailyMenuRepository;
    private final MealPlanRepository mealPlanRepository;

    public PublicMenuService(
            DailyMenuRepository dailyMenuRepository,
            MealPlanRepository mealPlanRepository
    ) {
        this.dailyMenuRepository = dailyMenuRepository;
        this.mealPlanRepository = mealPlanRepository;
    }

    @Transactional(readOnly = true)
    public PublicMenuResponse findTodayMenu() {
        LocalDate today = LocalDate.now();

        DailyMenu dailyMenu = dailyMenuRepository
                .findByMenuDate(today)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Não existe cardápio cadastrado para hoje"
                        )
                );

        List<DailyMenuItem> availableItems = dailyMenu
                .getItems()
                .stream()
                .filter(item ->
                        Boolean.TRUE.equals(item.getAvailable())
                )
                .filter(item ->
                        Boolean.TRUE.equals(
                                item.getMenuItem().getActive()
                        )
                )
                .sorted(
                        Comparator.comparing(
                                        (DailyMenuItem item) ->
                                                item.getMenuItem()
                                                        .getCategory()
                                                        .getDisplayOrder()
                                )
                                .thenComparing(
                                        DailyMenuItem::getDisplayOrder
                                )
                                .thenComparing(
                                        item -> item.getMenuItem().getName()
                                )
                )
                .toList();

        List<PublicMenuCategoryResponse> categories =
                groupItemsByCategory(availableItems);

        List<PublicMealPlanResponse> mealPlans =
                mealPlanRepository
                        .findByActiveTrueOrderByPriceAsc()
                        .stream()
                        .map(PublicMealPlanResponse::from)
                        .toList();

        return new PublicMenuResponse(
                dailyMenu.getId(),
                dailyMenu.getMenuDate(),
                dailyMenu.getDayOfWeek().name(),
                dailyMenu.getOpen(),
                isCurrentlyAcceptingOrders(dailyMenu),
                dailyMenu.getOpeningTime(),
                dailyMenu.getClosingTime(),
                dailyMenu.getNotes(),
                categories,
                mealPlans
        );
    }

    private List<PublicMenuCategoryResponse> groupItemsByCategory(
            List<DailyMenuItem> availableItems
    ) {
        Map<Long, List<DailyMenuItem>> itemsByCategory =
                availableItems
                        .stream()
                        .collect(
                                java.util.stream.Collectors.groupingBy(
                                        item -> item
                                                .getMenuItem()
                                                .getCategory()
                                                .getId(),
                                        LinkedHashMap::new,
                                        java.util.stream.Collectors.toList()
                                )
                        );

        return itemsByCategory
                .values()
                .stream()
                .map(this::createCategoryResponse)
                .toList();
    }

    private PublicMenuCategoryResponse createCategoryResponse(
            List<DailyMenuItem> categoryItems
    ) {
        MenuCategory category = categoryItems
                .getFirst()
                .getMenuItem()
                .getCategory();

        List<PublicMenuItemResponse> items = categoryItems
                .stream()
                .map(PublicMenuItemResponse::from)
                .toList();

        return new PublicMenuCategoryResponse(
                category.getId(),
                category.getCode(),
                category.getName(),
                category.getDisplayOrder(),
                category.getMinimumSelections(),
                category.getMaximumSelections(),
                category.getRequired(),
                items
        );
    }

    private boolean isCurrentlyAcceptingOrders(
            DailyMenu dailyMenu
    ) {
        if (!Boolean.TRUE.equals(dailyMenu.getOpen())) {
            return false;
        }

        LocalTime now = LocalTime.now();

        if (
                dailyMenu.getOpeningTime() != null
                        && now.isBefore(dailyMenu.getOpeningTime())
        ) {
            return false;
        }

        if (
                dailyMenu.getClosingTime() != null
                        && !now.isBefore(dailyMenu.getClosingTime())
        ) {
            return false;
        }

        return true;
    }
}