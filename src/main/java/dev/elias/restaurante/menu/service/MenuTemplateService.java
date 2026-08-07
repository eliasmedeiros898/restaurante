package dev.elias.restaurante.menu.service;

import dev.elias.restaurante.menu.dto.MenuTemplateResponse;
import dev.elias.restaurante.menu.entity.DayOfWeekName;
import dev.elias.restaurante.menu.entity.MenuTemplate;
import dev.elias.restaurante.menu.repository.MenuTemplateRepository;
import dev.elias.restaurante.shared.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Service
public class MenuTemplateService {

    private final MenuTemplateRepository repository;

    public MenuTemplateService(
            MenuTemplateRepository repository
    ) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<MenuTemplateResponse> findAll() {
        return repository
                .findAllByOrderByDayOfWeekAsc()
                .stream()
                .map(MenuTemplateResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public MenuTemplateResponse findById(Long id) {
        return MenuTemplateResponse.from(
                findEntityById(id)
        );
    }

    @Transactional(readOnly = true)
    public MenuTemplateResponse findByDayOfWeek(
            String dayOfWeek
    ) {
        DayOfWeekName parsedDay = parseDayOfWeek(dayOfWeek);

        MenuTemplate template = repository
                .findByDayOfWeek(parsedDay)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Cardápio padrão não encontrado para: "
                                        + parsedDay
                        )
                );

        return MenuTemplateResponse.from(template);
    }

    @Transactional(readOnly = true)
    public MenuTemplate findEntityById(Long id) {
        return repository
                .findOneById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Cardápio padrão não encontrado: " + id
                        )
                );
    }

    private DayOfWeekName parseDayOfWeek(String value) {
        try {
            return DayOfWeekName.valueOf(
                    value.trim().toUpperCase(Locale.ROOT)
            );
        } catch (
                IllegalArgumentException
                | NullPointerException exception
        ) {
            throw new IllegalArgumentException(
                    "Dia da semana inválido. Valores permitidos: "
                            + Arrays.toString(
                            DayOfWeekName.values()
                    )
            );
        }
    }
}