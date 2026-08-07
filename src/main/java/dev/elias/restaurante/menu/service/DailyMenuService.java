package dev.elias.restaurante.menu.service;

import dev.elias.restaurante.catalog.entity.MenuItem;
import dev.elias.restaurante.menu.dto.AddDailyMenuItemRequest;
import dev.elias.restaurante.menu.dto.CreateDailyMenuRequest;
import dev.elias.restaurante.menu.dto.DailyMenuResponse;
import dev.elias.restaurante.menu.dto.UpdateDailyMenuItemRequest;
import dev.elias.restaurante.menu.entity.*;
import dev.elias.restaurante.menu.repository.DailyMenuItemRepository;
import dev.elias.restaurante.menu.repository.DailyMenuRepository;
import dev.elias.restaurante.menu.repository.MenuTemplateRepository;
import dev.elias.restaurante.shared.exception.BusinessConflictException;
import dev.elias.restaurante.shared.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import dev.elias.restaurante.catalog.repository.MenuItemRepository;
import dev.elias.restaurante.menu.dto.UpdateDailyMenuSettingsRequest;

import java.time.ZoneId;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class DailyMenuService {

    private final DailyMenuRepository dailyMenuRepository;
    private final DailyMenuItemRepository dailyMenuItemRepository;
    private final MenuTemplateRepository menuTemplateRepository;
    private final MenuItemRepository menuItemRepository;
    private static final ZoneId RESTAURANT_ZONE =
            ZoneId.of("America/Sao_Paulo");

    public DailyMenuService(
            DailyMenuRepository dailyMenuRepository,
            DailyMenuItemRepository dailyMenuItemRepository,
            MenuTemplateRepository menuTemplateRepository,
            MenuItemRepository menuItemRepository
    ) {
        this.dailyMenuRepository = dailyMenuRepository;
        this.dailyMenuItemRepository = dailyMenuItemRepository;
        this.menuTemplateRepository = menuTemplateRepository;
        this.menuItemRepository = menuItemRepository;
    }

    @Transactional
    public DailyMenuResponse create(
            CreateDailyMenuRequest request
    ) {
        validateOpeningHours(request);

        if (dailyMenuRepository.existsByMenuDate(
                request.menuDate()
        )) {
            throw new BusinessConflictException(
                    "Já existe um cardápio para a data "
                            + request.menuDate()
            );
        }

        DayOfWeekName dayOfWeek = convertDayOfWeek(
                request.menuDate().getDayOfWeek()
        );

        MenuTemplate template = menuTemplateRepository
                .findByDayOfWeekAndActiveTrue(dayOfWeek)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Não existe cardápio padrão ativo para "
                                        + dayOfWeek
                        )
                );

        DailyMenu dailyMenu = new DailyMenu(
                request.menuDate(),
                dayOfWeek,
                true,
                request.openingTime(),
                request.closingTime(),
                template,
                request.notes()
        );

        template.getItems().forEach(templateItem -> {
            DailyMenuItem dailyItem = new DailyMenuItem(
                    templateItem.getMenuItem(),
                    true,
                    templateItem.getDisplayOrder()
            );

            dailyMenu.addItem(dailyItem);
        });

        DailyMenu savedMenu = dailyMenuRepository.save(dailyMenu);

        return DailyMenuResponse.from(savedMenu);
    }



    @Transactional(readOnly = true)
    public List<DailyMenuResponse> findAll() {
        LocalDate today =
                LocalDate.now(
                        RESTAURANT_ZONE
                );

        return dailyMenuRepository
                .findByMenuDateGreaterThanEqualOrderByMenuDateAsc(
                        today
                )
                .stream()
                .map(DailyMenuResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public DailyMenuResponse findById(Long id) {
        return DailyMenuResponse.from(
                findEntityById(id)
        );
    }

    @Transactional(readOnly = true)
    public DailyMenuResponse findByDate(LocalDate date) {
        DailyMenu menu = dailyMenuRepository
                .findByMenuDate(date)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Cardápio não encontrado para a data "
                                        + date
                        )
                );

        return DailyMenuResponse.from(menu);
    }

    @Transactional
    public DailyMenuResponse updateItemAvailability(
            Long dailyMenuId,
            Long dailyMenuItemId,
            UpdateDailyMenuItemRequest request
    ) {
        DailyMenuItem item = dailyMenuItemRepository
                .findByIdAndDailyMenuId(
                        dailyMenuItemId,
                        dailyMenuId
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Item do cardápio diário não encontrado: "
                                        + dailyMenuItemId
                        )
                );

        if (
                Boolean.FALSE.equals(request.available())
                        && (
                        request.unavailableReason() == null
                                || request.unavailableReason().isBlank()
                )
        ) {
            throw new IllegalArgumentException(
                    "Informe o motivo da indisponibilidade"
            );
        }

        item.changeAvailability(
                request.available(),
                request.unavailableReason()
        );

        dailyMenuItemRepository.save(item);

        return DailyMenuResponse.from(
                findEntityById(dailyMenuId)
        );
    }

    @Transactional
    public DailyMenuResponse closeMenu(Long id) {
        DailyMenu menu = findEntityById(id);
        menu.setOpen(false);

        return DailyMenuResponse.from(
                dailyMenuRepository.save(menu)
        );
    }

    @Transactional
    public DailyMenuResponse openMenu(Long id) {
        DailyMenu menu = findEntityById(id);
        menu.setOpen(true);

        return DailyMenuResponse.from(
                dailyMenuRepository.save(menu)
        );
    }

    @Transactional(readOnly = true)
    public DailyMenu findEntityById(Long id) {
        return dailyMenuRepository
                .findOneById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Cardápio diário não encontrado: " + id
                        )
                );
    }

    private void validateOpeningHours(
            LocalTime openingTime,
            LocalTime closingTime
    ) {
        if (
                openingTime == null
                        && closingTime == null
        ) {
            return;
        }

        if (
                openingTime == null
                        || closingTime == null
        ) {
            throw new IllegalArgumentException(
                    "Informe os horários de abertura e fechamento"
            );
        }

        if (
                !closingTime.isAfter(
                        openingTime
                )
        ) {
            throw new IllegalArgumentException(
                    "O horário de fechamento deve ser posterior "
                            + "ao horário de abertura"
            );
        }
    }

    private String normalizeOptionalText(
            String value
    ) {
        if (
                value == null
                        || value.isBlank()
        ) {
            return null;
        }

        return value.trim();
    }

    private DayOfWeekName convertDayOfWeek(
            DayOfWeek dayOfWeek
    ) {
        return DayOfWeekName.valueOf(dayOfWeek.name());
    }

    private void validateOpeningHours(
            CreateDailyMenuRequest request
    ) {
        validateOpeningHours(
                request.openingTime(),
                request.closingTime()
        );
    }

    @Transactional
    public DailyMenuResponse addItem(
            Long dailyMenuId,
            AddDailyMenuItemRequest request
    ) {
        DailyMenu dailyMenu =
                findEntityById(dailyMenuId);

        MenuItem menuItem = menuItemRepository
                .findById(request.menuItemId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Item do catálogo não encontrado: "
                                        + request.menuItemId()
                        )
                );

        if (!Boolean.TRUE.equals(menuItem.getActive())) {
            throw new BusinessConflictException(
                    "Não é possível adicionar um item inativo ao cardápio"
            );
        }


        boolean alreadyExists = dailyMenu
                .getItems()
                .stream()
                .anyMatch(item ->
                        item.getMenuItem()
                                .getId()
                                .equals(menuItem.getId())
                );

        if (alreadyExists) {
            throw new BusinessConflictException(
                    "Este item já está presente no cardápio"
            );
        }

        DailyMenuItem dailyMenuItem =
                new DailyMenuItem(
                        menuItem,
                        true,
                        request.displayOrder()
                );

        dailyMenu.addItem(dailyMenuItem);

        DailyMenu saved =
                dailyMenuRepository.save(dailyMenu);

        return DailyMenuResponse.from(saved);
    }

    @Transactional
    public DailyMenuResponse removeItem(
            Long dailyMenuId,
            Long dailyMenuItemId
    ) {
        DailyMenu dailyMenu =
                findEntityById(dailyMenuId);

        DailyMenuItem item = dailyMenuItemRepository
                .findByIdAndDailyMenuId(
                        dailyMenuItemId,
                        dailyMenuId
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Item do cardápio não encontrado: "
                                        + dailyMenuItemId
                        )
                );

        dailyMenu.removeItem(item);

        DailyMenu saved =
                dailyMenuRepository.save(dailyMenu);

        return DailyMenuResponse.from(saved);
    }

    @Transactional
    public DailyMenuResponse updateSettings(
            Long dailyMenuId,
            UpdateDailyMenuSettingsRequest request
    ) {
        validateOpeningHours(
                request.openingTime(),
                request.closingTime()
        );

        DailyMenu dailyMenu =
                findEntityById(dailyMenuId);

        dailyMenu.setOpeningTime(
                request.openingTime()
        );

        dailyMenu.setClosingTime(
                request.closingTime()
        );

        dailyMenu.setNotes(
                normalizeOptionalText(
                        request.notes()
                )
        );

        DailyMenu saved =
                dailyMenuRepository.save(
                        dailyMenu
                );

        return DailyMenuResponse.from(saved);
    }
}