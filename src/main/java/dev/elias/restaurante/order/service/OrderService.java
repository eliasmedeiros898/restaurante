package dev.elias.restaurante.order.service;

import dev.elias.restaurante.catalog.entity.MealPlan;
import dev.elias.restaurante.catalog.entity.MenuCategory;
import dev.elias.restaurante.catalog.entity.MenuItem;
import dev.elias.restaurante.catalog.repository.MenuItemRepository;
import dev.elias.restaurante.catalog.service.MealPlanService;
import dev.elias.restaurante.menu.entity.DailyMenu;
import dev.elias.restaurante.menu.entity.DailyMenuItem;
import dev.elias.restaurante.menu.service.DailyMenuService;
import dev.elias.restaurante.order.dto.CreateOrderItemRequest;
import dev.elias.restaurante.order.dto.CreateOrderRequest;
import dev.elias.restaurante.order.dto.CreateOrderSelectionRequest;
import dev.elias.restaurante.order.dto.OrderResponse;
import dev.elias.restaurante.order.entity.*;
import dev.elias.restaurante.order.repository.OrderRepository;
import dev.elias.restaurante.shared.exception.BusinessRuleException;
import dev.elias.restaurante.shared.exception.InvalidStateTransitionException;
import dev.elias.restaurante.shared.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityManager;
import dev.elias.restaurante.delivery.entity.DeliveryZone;
import dev.elias.restaurante.delivery.service.DeliveryZoneService;
import dev.elias.restaurante.order.entity.FulfillmentType;
import dev.elias.restaurante.order.entity.OrderChannel;


import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;


@Service
public class OrderService {

    private static final String MEAT_CATEGORY_CODE = "MEAT";
    private static final String RICE_CATEGORY_CODE = "RICE";
    private static final String BEAN_CATEGORY_CODE = "BEAN";

    private static final String MARMITA_PLAN_PREFIX =
            "MARMITA_";

    private static final int MARMITA_MIN_MEATS = 5;
    private static final int MARMITA_MAX_MEATS = 10;
    private static final int MARMITA_MAX_RICE_OPTIONS = 2;
    private static final int MARMITA_MAX_BEAN_OPTIONS = 2;

    private final OrderRepository orderRepository;
    private final MenuItemRepository menuItemRepository;
    private final DailyMenuService dailyMenuService;
    private final MealPlanService mealPlanService;
    private final EntityManager entityManager;
    private final DeliveryZoneService deliveryZoneService;


    public OrderService(
            OrderRepository orderRepository,
            DailyMenuService dailyMenuService,
            MealPlanService mealPlanService,
            MenuItemRepository menuItemRepository,
            DeliveryZoneService deliveryZoneService,
            EntityManager entityManager
    ) {
        this.orderRepository = orderRepository;
        this.dailyMenuService = dailyMenuService;
        this.mealPlanService = mealPlanService;
        this.menuItemRepository = menuItemRepository;
        this.deliveryZoneService =
                deliveryZoneService;
        this.entityManager = entityManager;
    }

    @Transactional
    public OrderResponse create(
            CreateOrderRequest request
    ) {
        DailyMenu dailyMenu =
                dailyMenuService.findEntityById(
                        request.dailyMenuId()
                );

        Map<Long, DailyMenuItem> dailyItemsByMenuItemId =
                dailyMenu.getItems()
                        .stream()
                        .collect(Collectors.toMap(
                                dailyMenuItem ->
                                        dailyMenuItem
                                                .getMenuItem()
                                                .getId(),
                                Function.identity()
                        ));

        Order order = new Order(
                dailyMenu,
                request.customerName(),
                request.customerPhone(),
                request.channel(),
                request.paymentMethod(),
                request.notes()
        );

        for (
                CreateOrderItemRequest itemRequest
                : request.items()
        ) {
            OrderItem orderItem =
                    createOrderItem(
                            itemRequest,
                            dailyItemsByMenuItemId
                    );

            order.addItem(orderItem);
        }

        if (
                request.fulfillmentType()
                        == FulfillmentType.DELIVERY
        ) {
            DeliveryZone zone =
                    deliveryZoneService
                            .findActiveEntity(
                                    request.deliveryZoneId()
                            );

            order.configureDelivery(
                    zone,
                    request.deliveryStreet(),
                    request.deliveryNumber(),
                    request.deliveryComplement(),
                    request.deliveryReference()
            );
        } else {
            order.configurePickup();
        }

        order.recalculateTotal();

        Order savedOrder =
                orderRepository.saveAndFlush(order);

        entityManager.refresh(savedOrder);

        return OrderResponse.from(savedOrder);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> findAll(
            LocalDate date,
            OrderStatus status,
            OrderChannel channel
    ) {
        List<Order> orders;

        if (
                date != null
                        && status != null
                        && channel != null
        ) {
            orders = orderRepository
                    .findByDailyMenuMenuDateAndStatusAndChannelOrderByCreatedAtDesc(
                            date,
                            status,
                            channel
                    );

        } else if (
                date != null
                        && status != null
        ) {
            orders = orderRepository
                    .findByDailyMenuMenuDateAndStatusOrderByCreatedAtDesc(
                            date,
                            status
                    );

        } else if (
                date != null
                        && channel != null
        ) {
            orders = orderRepository
                    .findByDailyMenuMenuDateAndChannelOrderByCreatedAtDesc(
                            date,
                            channel
                    );

        } else if (date != null) {
            orders = orderRepository
                    .findByDailyMenuMenuDateOrderByCreatedAtDesc(
                            date
                    );

        } else if (
                status != null
                        && channel != null
        ) {
            orders = orderRepository
                    .findByStatusAndChannelOrderByCreatedAtDesc(
                            status,
                            channel
                    );

        } else if (status != null) {
            orders = orderRepository
                    .findByStatusOrderByCreatedAtDesc(
                            status
                    );

        } else if (channel != null) {
            orders = orderRepository
                    .findByChannelOrderByCreatedAtDesc(
                            channel
                    );

        } else {
            orders = orderRepository
                    .findAllByOrderByCreatedAtDesc();
        }

        return orders
                .stream()
                .map(OrderResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public OrderResponse findById(Long id) {
        return OrderResponse.from(findEntityById(id));
    }

    @Transactional(readOnly = true)
    public OrderResponse findByOrderNumber(Long orderNumber) {
        Order order = orderRepository
                .findByOrderNumber(orderNumber)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Pedido não encontrado: "
                                        + orderNumber
                        )
                );

        orderRepository.saveAndFlush(order);

        return OrderResponse.from(
                findEntityById(order.getId())
        );
    }

    @Transactional(readOnly = true)
    public Order findEntityById(Long id) {
        return orderRepository
                .findOneById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Pedido não encontrado: " + id
                        )
                );
    }

    @Transactional
    public OrderResponse confirm(Long id) {
        Order order = findEntityById(id);

        executeTransition(order::confirm);

        orderRepository.saveAndFlush(order);

        return OrderResponse.from(
                findEntityById(order.getId())
        );
    }

    @Transactional
    public OrderResponse startPreparation(Long id) {
        Order order = findEntityById(id);

        executeTransition(order::startPreparation);

        orderRepository.saveAndFlush(order);

        return OrderResponse.from(
                findEntityById(order.getId())
        );
    }

    @Transactional
    public OrderResponse markReady(Long id) {
        Order order = findEntityById(id);

        executeTransition(order::markAsReady);

        orderRepository.saveAndFlush(order);

        return OrderResponse.from(
                findEntityById(order.getId())
        );
    }

    @Transactional
    public OrderResponse deliver(Long id) {
        Order order = findEntityById(id);

        executeTransition(order::deliver);

        orderRepository.saveAndFlush(order);

        return OrderResponse.from(
                findEntityById(order.getId())
        );
    }

    @Transactional
    public OrderResponse cancel(Long id) {
        Order order = findEntityById(id);

        executeTransition(order::cancel);

        orderRepository.saveAndFlush(order);

        return OrderResponse.from(
                findEntityById(order.getId())
        );
    }

    @Transactional
    public OrderResponse markAsPaid(Long id) {
        Order order = findEntityById(id);

        executeTransition(order::markAsPaid);

        orderRepository.saveAndFlush(order);

        return OrderResponse.from(
                findEntityById(order.getId())
        );
    }

    @Transactional
    public OrderResponse createPublicOrder(
            CreateOrderRequest request
    ) {
        if (
                request.channel()
                        != OrderChannel.ONLINE_WHATSAPP
        ) {
            throw new BusinessRuleException(
                    "Pedidos públicos devem utilizar o canal online"
            );
        }

        if (
                request.customerName() == null
                        || request.customerName().isBlank()
        ) {
            throw new BusinessRuleException(
                    "Informe o nome do cliente"
            );
        }

        if (
                request.customerPhone() == null
                        || request.customerPhone().isBlank()
        ) {
            throw new BusinessRuleException(
                    "Informe o telefone do cliente"
            );
        }

        return create(request);
    }

    @Transactional
    public OrderResponse createCounterOrder(
            CreateOrderRequest request
    ) {
        if (
                request.channel()
                        != OrderChannel.COUNTER
        ) {
            throw new BusinessRuleException(
                    "Pedidos do balcão devem utilizar o canal COUNTER"
            );
        }

        return create(request);
    }


    private void executeTransition(Runnable transition) {
        try {
            transition.run();
        } catch (IllegalStateException exception) {
            throw new InvalidStateTransitionException(
                    exception.getMessage()
            );
        }
    }

    private OrderItem createOrderItem(
            CreateOrderItemRequest request,
            Map<Long, DailyMenuItem> dailyItemsByMenuItemId
    ) {
        MealPlan mealPlan = mealPlanService.findEntityById(
                request.mealPlanId()
        );

        if (!Boolean.TRUE.equals(mealPlan.getActive())) {
            throw new BusinessRuleException(
                    "O plano de refeição está inativo: "
                            + mealPlan.getName()
            );
        }

        validateMealTypeAndPlan(
                request.mealType(),
                mealPlan
        );

        validateDuplicateSelections(request.selections());

        List<Long> selectedIds = request
                .selections()
                .stream()
                .map(CreateOrderSelectionRequest::menuItemId)
                .toList();

        List<MenuItem> selectedItems =
                menuItemRepository.findByIdIn(selectedIds);

        if (selectedItems.size() != selectedIds.size()) {
            throw new ResourceNotFoundException(
                    "Um ou mais itens selecionados não existem"
            );
        }

        Map<Long, MenuItem> menuItemsById = selectedItems
                .stream()
                .collect(Collectors.toMap(
                        MenuItem::getId,
                        Function.identity()
                ));

        validateSelections(
                request,
                mealPlan,
                menuItemsById,
                dailyItemsByMenuItemId
        );

        OrderItem orderItem = new OrderItem(
                mealPlan,
                request.mealType(),
                request.quantity(),
                normalize(request.itemNotes())
        );

        for (
                CreateOrderSelectionRequest selectionRequest
                : request.selections()
        ) {
            MenuItem menuItem = menuItemsById.get(
                    selectionRequest.menuItemId()
            );

            MenuCategory category = menuItem.getCategory();

            OrderItemSelection selection =
                    new OrderItemSelection(
                            category,
                            menuItem,
                            selectionRequest.quantity()
                    );

            orderItem.addSelection(selection);
        }

        return orderItem;
    }

    private void configureFulfillment(
            Order order,
            CreateOrderRequest request
    ) {
        if (
                request.fulfillmentType()
                        == FulfillmentType.DELIVERY
        ) {
            validateDeliveryRequest(request);

            DeliveryZone deliveryZone =
                    deliveryZoneService.findActiveEntity(
                            request.deliveryZoneId()
                    );

            order.configureDelivery(
                    deliveryZone,
                    request.deliveryStreet(),
                    request.deliveryNumber(),
                    request.deliveryComplement(),
                    request.deliveryReference()
            );

            return;
        }

        order.configurePickup();
    }

    private void validateDeliveryRequest(
            CreateOrderRequest request
    ) {
        if (request.deliveryZoneId() == null) {
            throw new BusinessRuleException(
                    "Selecione o bairro para entrega"
            );
        }

        if (
                request.deliveryStreet() == null
                        || request.deliveryStreet().isBlank()
        ) {
            throw new BusinessRuleException(
                    "Informe a rua para entrega"
            );
        }

        if (
                request.deliveryNumber() == null
                        || request.deliveryNumber().isBlank()
        ) {
            throw new BusinessRuleException(
                    "Informe o número do endereço"
            );
        }

        if (
                request.deliveryStreet().trim().length()
                        > 160
        ) {
            throw new BusinessRuleException(
                    "A rua informada é muito extensa"
            );
        }

        if (
                request.deliveryNumber().trim().length()
                        > 30
        ) {
            throw new BusinessRuleException(
                    "O número informado é muito extenso"
            );
        }
    }

    private void validateMealTypeAndPlan(
            MealType mealType,
            MealPlan mealPlan
    ) {
        boolean marmitaPlan =
                isMarmitaPlan(mealPlan);

        if (
                mealType == MealType.MARMITA
                        && !marmitaPlan
        ) {
            throw new BusinessRuleException(
                    "Selecione um plano de marmita válido"
            );
        }

        if (
                mealType != MealType.MARMITA
                        && marmitaPlan
        ) {
            throw new BusinessRuleException(
                    "Planos de marmita só podem ser usados "
                            + "com o tipo MARMITA"
            );
        }
    }

    private boolean isMarmitaPlan(
            MealPlan mealPlan
    ) {
        return mealPlan.getCode() != null
                && mealPlan
                .getCode()
                .startsWith(
                        MARMITA_PLAN_PREFIX
                );
    }

    private void validateSelections(
            CreateOrderItemRequest request,
            MealPlan mealPlan,
            Map<Long, MenuItem> selectedItemsById,
            Map<Long, DailyMenuItem> dailyItemsByMenuItemId
    ) {
        for (
                CreateOrderSelectionRequest selection
                : request.selections()
        ) {
            MenuItem menuItem = selectedItemsById.get(
                    selection.menuItemId()
            );

            validateItemCategory(selection, menuItem);
            validateItemActive(menuItem);
            validateDailyAvailability(
                    menuItem,
                    dailyItemsByMenuItemId
            );
        }

        Map<Long, Integer> selectionCountByCategory =
                request.selections()
                        .stream()
                        .collect(Collectors.groupingBy(
                                CreateOrderSelectionRequest::categoryId,
                                Collectors.summingInt(
                                        CreateOrderSelectionRequest::quantity
                                )
                        ));

        validateCategoryLimits(
                request.mealType(),
                dailyItemsByMenuItemId,
                selectionCountByCategory
        );

        List<CreateOrderSelectionRequest> selectedMeatRequests =
                request.selections()
                        .stream()
                        .filter(selection -> {
                            MenuItem menuItem =
                                    selectedItemsById.get(
                                            selection.menuItemId()
                                    );

                            return isMeat(menuItem);
                        })
                        .toList();

        validateMeatPlan(request.mealType(),mealPlan, selectedMeatRequests,selectedItemsById);
    }

    private void validateCategoryLimits(
            MealType mealType,
            Map<Long, DailyMenuItem> dailyItemsByMenuItemId,
            Map<Long, Integer> selectionCountByCategory
    ) {
        Map<Long, MenuCategory> categoriesInDailyMenu =
                dailyItemsByMenuItemId
                        .values()
                        .stream()
                        .filter(item ->
                                Boolean.TRUE.equals(
                                        item.getAvailable()
                                )
                        )
                        .map(item ->
                                item.getMenuItem().getCategory()
                        )
                        .collect(Collectors.toMap(
                                MenuCategory::getId,
                                Function.identity(),
                                (first, second) -> first
                        ));

        for (
                MenuCategory category
                : categoriesInDailyMenu.values()
        ) {
            if (MEAT_CATEGORY_CODE.equals(category.getCode())) {
                continue;
            }

            int minimumSelections =
                    category.getMinimumSelections();

            int maximumSelections =
                    resolveMaximumSelections(
                            mealType,
                            category
                    );

            int selectedQuantity =
                    selectionCountByCategory.getOrDefault(
                            category.getId(),
                            0
                    );

            if (
                    selectedQuantity
                            < minimumSelections
            ) {
                throw new BusinessRuleException(
                        "Selecione pelo menos "
                                + minimumSelections
                                + " opção(ões) de "
                                + category.getName()
                );
            }

            if (
                    selectedQuantity
                            > maximumSelections
            ) {
                throw new BusinessRuleException(
                        "Selecione no máximo "
                                + maximumSelections
                                + " opção(ões) de "
                                + category.getName()
                );
            }
        }
    }

    private void validateMeatPlan(
            MealType mealType,
            MealPlan mealPlan,
            List<CreateOrderSelectionRequest> selectedMeats,
            Map<Long, MenuItem> selectedItemsById
    ) {
        int totalMeatQuantity = selectedMeats
                .stream()
                .mapToInt(CreateOrderSelectionRequest::quantity)
                .sum();

        if (mealType == MealType.MARMITA) {
            validateMarmitaMeats(
                    mealPlan,
                    totalMeatQuantity
            );

            return;
        }

        if (totalMeatQuantity != mealPlan.getMeatQuantity()) {
            throw new BusinessRuleException(
                    "O plano "
                            + mealPlan.getName()
                            + " exige exatamente "
                            + mealPlan.getMeatQuantity()
                            + " porção(ões) de carne"
            );
        }

        if (Boolean.TRUE.equals(mealPlan.getBasicMeatsOnly())) {
            Optional<MenuItem> invalidMeat = selectedMeats
                    .stream()
                    .map(selection ->
                            selectedItemsById.get(
                                    selection.menuItemId()
                            )
                    )
                    .filter(meat ->
                            !Boolean.TRUE.equals(
                                    meat.getBasicPlanEligible()
                            )
                    )
                    .findFirst();

            if (invalidMeat.isPresent()) {
                throw new BusinessRuleException(
                        "A carne "
                                + invalidMeat.get().getName()
                                + " não está disponível no plano básico"
                );
            }
        }
    }

    private void validateItemCategory(
            CreateOrderSelectionRequest selection,
            MenuItem menuItem
    ) {
        Long actualCategoryId =
                menuItem.getCategory().getId();

        if (!actualCategoryId.equals(selection.categoryId())) {
            throw new BusinessRuleException(
                    "O item "
                            + menuItem.getName()
                            + " não pertence à categoria informada"
            );
        }
    }

    private int resolveMaximumSelections(
            MealType mealType,
            MenuCategory category
    ) {
        if (mealType != MealType.MARMITA) {
            return category.getMaximumSelections();
        }

        if (
                RICE_CATEGORY_CODE.equals(
                        category.getCode()
                )
        ) {
            return MARMITA_MAX_RICE_OPTIONS;
        }

        if (
                BEAN_CATEGORY_CODE.equals(
                        category.getCode()
                )
        ) {
            return MARMITA_MAX_BEAN_OPTIONS;
        }

        return category.getMaximumSelections();
    }

    private void validateItemActive(MenuItem menuItem) {
        if (!Boolean.TRUE.equals(menuItem.getActive())) {
            throw new BusinessRuleException(
                    "O item está inativo: "
                            + menuItem.getName()
            );
        }
    }

    private void validateMarmitaMeats(
            MealPlan mealPlan,
            int totalMeatQuantity
    ) {
        if (
                mealPlan.getMeatQuantity()
                        < MARMITA_MIN_MEATS
                        || mealPlan.getMeatQuantity()
                        > MARMITA_MAX_MEATS
        ) {
            throw new BusinessRuleException(
                    "O plano de marmita deve possuir "
                            + "entre 5 e 10 porções de carne"
            );
        }

        if (
                totalMeatQuantity
                        != mealPlan.getMeatQuantity()
        ) {
            throw new BusinessRuleException(
                    "A marmita "
                            + mealPlan.getName()
                            + " exige exatamente "
                            + mealPlan.getMeatQuantity()
                            + " porções de carne"
            );
        }
    }

    private void validateDailyAvailability(
            MenuItem menuItem,
            Map<Long, DailyMenuItem> dailyItemsByMenuItemId
    ) {
        DailyMenuItem dailyItem =
                dailyItemsByMenuItemId.get(menuItem.getId());

        if (dailyItem == null) {
            throw new BusinessRuleException(
                    "O item "
                            + menuItem.getName()
                            + " não pertence ao cardápio do dia"
            );
        }

        if (!Boolean.TRUE.equals(dailyItem.getAvailable())) {
            throw new BusinessRuleException(
                    "O item "
                            + menuItem.getName()
                            + " está indisponível"
            );
        }
    }

    private void validateDuplicateSelections(
            List<CreateOrderSelectionRequest> selections
    ) {
        Set<Long> uniqueIds = selections
                .stream()
                .map(CreateOrderSelectionRequest::menuItemId)
                .collect(Collectors.toSet());

        if (uniqueIds.size() != selections.size()) {
            throw new BusinessRuleException(
                    "Não é permitido selecionar o mesmo item "
                            + "mais de uma vez na mesma refeição"
            );
        }
    }

    private void validateDailyMenu(DailyMenu dailyMenu) {
        if (!Boolean.TRUE.equals(dailyMenu.getOpen())) {
            throw new BusinessRuleException(
                    "O cardápio está fechado"
            );
        }

        if (!dailyMenu.getMenuDate().equals(LocalDate.now())) {
            throw new BusinessRuleException(
                    "Só é possível realizar pedidos "
                            + "para o cardápio de hoje"
            );
        }

        LocalTime now = LocalTime.now();

        if (
                dailyMenu.getOpeningTime() != null
                        && now.isBefore(dailyMenu.getOpeningTime())
        ) {
            throw new BusinessRuleException(
                    "O restaurante ainda não está aberto"
            );
        }

        if (
                dailyMenu.getClosingTime() != null
                        && !now.isBefore(dailyMenu.getClosingTime())
        ) {
            throw new BusinessRuleException(
                    "O horário de pedidos foi encerrado"
            );
        }
    }

    private void validateCustomer(CreateOrderRequest request) {
        if (
                request.channel()
                        == OrderChannel.ONLINE_WHATSAPP
        ) {
            if (
                    request.customerName() == null
                            || request.customerName().isBlank()
            ) {
                throw new BusinessRuleException(
                        "O nome do cliente é obrigatório "
                                + "para pedidos online"
                );
            }

            if (
                    request.customerPhone() == null
                            || request.customerPhone().isBlank()
            ) {
                throw new BusinessRuleException(
                        "O telefone do cliente é obrigatório "
                                + "para pedidos online"
                );
            }
        }
    }

    private boolean isMeat(MenuItem menuItem) {
        return MEAT_CATEGORY_CODE.equals(
                menuItem.getCategory().getCode()
        );
    }

    private String normalize(String value) {
        if (value == null) {
            return null;
        }

        String normalized = value.trim();

        return normalized.isEmpty()
                ? null
                : normalized;
    }
}