package dev.elias.restaurante.order.entity;

import dev.elias.restaurante.delivery.entity.DeliveryZone;
import dev.elias.restaurante.menu.entity.DailyMenu;
import jakarta.persistence.*;
import org.hibernate.annotations.Generated;
import org.hibernate.generator.EventType;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Entity
@Table(
        name = "orders",
        indexes = {
                @Index(
                        name = "idx_orders_status",
                        columnList = "status"
                ),
                @Index(
                        name = "idx_orders_channel",
                        columnList = "channel"
                ),
                @Index(
                        name = "idx_orders_created_at",
                        columnList = "created_at"
                ),
                @Index(
                        name = "idx_orders_daily_menu_id",
                        columnList = "daily_menu_id"
                ),
                @Index(
                        name = "idx_orders_fulfillment_type",
                        columnList = "fulfillment_type"
                )
        }
)
public class Order {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;

    /**
     * Número público do pedido.
     *
     * Esse campo é gerado pelo PostgreSQL e não deve ser
     * preenchido manualmente pela aplicação.
     */
    @Generated(event = EventType.INSERT)
    @Column(
            name = "order_number",
            nullable = false,
            insertable = false,
            updatable = false,
            unique = true
    )
    private Long orderNumber;

    @ManyToOne(
            fetch = FetchType.LAZY,
            optional = false
    )
    @JoinColumn(
            name = "daily_menu_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_orders_daily_menu"
            )
    )
    private DailyMenu dailyMenu;

    @Column(
            name = "customer_name",
            length = 120
    )
    private String customerName;

    @Column(
            name = "customer_phone",
            length = 20
    )
    private String customerPhone;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "channel",
            nullable = false,
            length = 30
    )
    private OrderChannel channel;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "status",
            nullable = false,
            length = 40
    )
    private OrderStatus status;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "payment_method",
            nullable = false,
            length = 30
    )
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "payment_status",
            nullable = false,
            length = 30
    )
    private PaymentStatus paymentStatus;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "fulfillment_type",
            nullable = false,
            length = 20
    )
    private FulfillmentType fulfillmentType;

    /**
     * Zona atual relacionada ao pedido.
     *
     * Mesmo mantendo essa relação, também salvamos bairro e
     * taxa diretamente no pedido como snapshot.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "delivery_zone_id",
            foreignKey = @ForeignKey(
                    name = "fk_orders_delivery_zone"
            )
    )
    private DeliveryZone deliveryZone;

    @Column(
            name = "delivery_neighborhood",
            length = 120
    )
    private String deliveryNeighborhood;

    @Column(
            name = "delivery_street",
            length = 160
    )
    private String deliveryStreet;

    @Column(
            name = "delivery_number",
            length = 30
    )
    private String deliveryNumber;

    @Column(
            name = "delivery_complement",
            length = 160
    )
    private String deliveryComplement;

    @Column(
            name = "delivery_reference",
            length = 255
    )
    private String deliveryReference;

    @Column(
            name = "delivery_fee",
            nullable = false,
            precision = 10,
            scale = 2
    )
    private BigDecimal deliveryFee;

    /**
     * Total final:
     *
     * soma dos itens + taxa de entrega.
     */
    @Column(
            name = "total",
            nullable = false,
            precision = 10,
            scale = 2
    )
    private BigDecimal total;

    @Column(
            name = "notes",
            length = 1000
    )
    private String notes;

    @OneToMany(
            mappedBy = "order",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @OrderBy("id ASC")
    private List<OrderItem> items =
            new ArrayList<>();

    @Column(
            name = "created_at",
            nullable = false,
            updatable = false
    )
    private OffsetDateTime createdAt;

    @Column(
            name = "updated_at",
            nullable = false
    )
    private OffsetDateTime updatedAt;

    @Column(name = "confirmed_at")
    private OffsetDateTime confirmedAt;

    @Column(name = "preparation_started_at")
    private OffsetDateTime preparationStartedAt;

    @Column(name = "ready_at")
    private OffsetDateTime readyAt;

    @Column(name = "finished_at")
    private OffsetDateTime finishedAt;

    @Column(name = "cancelled_at")
    private OffsetDateTime cancelledAt;

    protected Order() {
    }

    public Order(
            DailyMenu dailyMenu,
            String customerName,
            String customerPhone,
            OrderChannel channel,
            PaymentMethod paymentMethod,
            String notes
    ) {
        this.dailyMenu = Objects.requireNonNull(
                dailyMenu,
                "O cardápio diário é obrigatório"
        );

        this.channel = Objects.requireNonNull(
                channel,
                "O canal do pedido é obrigatório"
        );

        this.paymentMethod =
                paymentMethod != null
                        ? paymentMethod
                        : PaymentMethod.NOT_INFORMED;

        this.customerName =
                normalizeOptional(customerName);

        this.customerPhone =
                normalizeOptional(customerPhone);

        this.notes =
                normalizeOptional(notes);

        this.status = initialStatusFor(channel);
        this.paymentStatus = PaymentStatus.PENDING;

        this.fulfillmentType =
                FulfillmentType.PICKUP;

        this.deliveryFee = BigDecimal.ZERO;
        this.total = BigDecimal.ZERO;

        OffsetDateTime now =
                OffsetDateTime.now();

        this.createdAt = now;
        this.updatedAt = now;
    }

    private OrderStatus initialStatusFor(
            OrderChannel channel
    ) {
        if (
                channel
                        == OrderChannel.ONLINE_WHATSAPP
        ) {
            return OrderStatus.AWAITING_CONFIRMATION;
        }

        return OrderStatus.CONFIRMED;
    }

    public void addItem(OrderItem item) {
        Objects.requireNonNull(
                item,
                "O item do pedido não pode ser nulo"
        );

        item.setOrder(this);
        this.items.add(item);

        touch();
    }

    public void removeItem(OrderItem item) {
        if (item == null) {
            return;
        }

        boolean removed =
                this.items.remove(item);

        if (removed) {
            item.removeOrder();
            recalculateTotal();
            touch();
        }
    }

    /**
     * Configura retirada no restaurante.
     */
    public void configurePickup() {
        this.fulfillmentType =
                FulfillmentType.PICKUP;

        this.deliveryZone = null;
        this.deliveryNeighborhood = null;
        this.deliveryStreet = null;
        this.deliveryNumber = null;
        this.deliveryComplement = null;
        this.deliveryReference = null;
        this.deliveryFee = BigDecimal.ZERO;

        recalculateTotal();
        touch();
    }

    /**
     * Configura entrega no endereço do cliente.
     *
     * Bairro e taxa são copiados da DeliveryZone para o pedido,
     * preservando os dados originais mesmo que a configuração
     * administrativa seja alterada futuramente.
     */
    public void configureDelivery(
            DeliveryZone deliveryZone,
            String street,
            String number,
            String complement,
            String reference
    ) {
        Objects.requireNonNull(
                deliveryZone,
                "A zona de entrega é obrigatória"
        );

        requireText(
                street,
                "A rua é obrigatória para entrega"
        );

        requireText(
                number,
                "O número é obrigatório para entrega"
        );

        if (
                !Boolean.TRUE.equals(
                        deliveryZone.getActive()
                )
        ) {
            throw new IllegalStateException(
                    "A zona selecionada não está ativa para entrega"
            );
        }

        this.fulfillmentType =
                FulfillmentType.DELIVERY;

        this.deliveryZone =
                deliveryZone;

        this.deliveryNeighborhood =
                deliveryZone
                        .getNeighborhood()
                        .trim();

        this.deliveryStreet =
                street.trim();

        this.deliveryNumber =
                number.trim();

        this.deliveryComplement =
                normalizeOptional(complement);

        this.deliveryReference =
                normalizeOptional(reference);

        this.deliveryFee =
                normalizeMoney(
                        deliveryZone.getDeliveryFee()
                );

        recalculateTotal();
        touch();
    }

    /**
     * Soma subtotal dos itens e taxa de entrega.
     */
    public void recalculateTotal() {
        BigDecimal itemsTotal =
                this.items
                        .stream()
                        .map(OrderItem::getSubtotal)
                        .filter(subtotal -> subtotal != null)
                        .reduce(
                                BigDecimal.ZERO,
                                BigDecimal::add
                        );

        BigDecimal currentDeliveryFee =
                this.deliveryFee != null
                        ? this.deliveryFee
                        : BigDecimal.ZERO;

        this.total =
                itemsTotal.add(currentDeliveryFee);

        touch();
    }

    public void confirm() {
        requireStatus(
                OrderStatus.AWAITING_CONFIRMATION,
                "Somente pedidos aguardando confirmação podem ser confirmados"
        );

        this.status = OrderStatus.CONFIRMED;
        this.confirmedAt = OffsetDateTime.now();

        touch();
    }

    public void startPreparation() {
        requireStatus(
                OrderStatus.CONFIRMED,
                "Somente pedidos confirmados podem entrar em preparação"
        );

        this.status = OrderStatus.PREPARING;
        this.preparationStartedAt =
                OffsetDateTime.now();

        touch();
    }

    public void markAsReady() {
        requireStatus(
                OrderStatus.PREPARING,
                "Somente pedidos em preparação podem ser marcados como prontos"
        );

        this.status = OrderStatus.READY;
        this.readyAt = OffsetDateTime.now();

        touch();
    }

    public void deliver() {
        requireStatus(
                OrderStatus.READY,
                "Somente pedidos prontos podem ser finalizados"
        );

        this.status = OrderStatus.DELIVERED;
        this.finishedAt = OffsetDateTime.now();

        touch();
    }

    public void cancel() {
        if (
                this.status == OrderStatus.DELIVERED
        ) {
            throw new IllegalStateException(
                    "Um pedido entregue não pode ser cancelado"
            );
        }

        if (
                this.status == OrderStatus.CANCELLED
        ) {
            throw new IllegalStateException(
                    "O pedido já está cancelado"
            );
        }

        this.status = OrderStatus.CANCELLED;
        this.cancelledAt = OffsetDateTime.now();

        if (
                this.paymentStatus
                        == PaymentStatus.PENDING
        ) {
            this.paymentStatus =
                    PaymentStatus.CANCELLED;
        }

        touch();
    }

    public void markAsPaid() {
        if (
                this.status == OrderStatus.CANCELLED
        ) {
            throw new IllegalStateException(
                    "Não é possível pagar um pedido cancelado"
            );
        }

        if (
                this.paymentStatus
                        == PaymentStatus.PAID
        ) {
            throw new IllegalStateException(
                    "O pedido já está pago"
            );
        }

        this.paymentStatus = PaymentStatus.PAID;

        touch();
    }

    public void markPaymentAsPending() {
        if (
                this.status == OrderStatus.CANCELLED
        ) {
            throw new IllegalStateException(
                    "Não é possível alterar o pagamento de um pedido cancelado"
            );
        }

        this.paymentStatus =
                PaymentStatus.PENDING;

        touch();
    }

    public void updatePaymentMethod(
            PaymentMethod paymentMethod
    ) {
        if (
                this.status == OrderStatus.CANCELLED
        ) {
            throw new IllegalStateException(
                    "Não é possível alterar um pedido cancelado"
            );
        }

        this.paymentMethod =
                paymentMethod != null
                        ? paymentMethod
                        : PaymentMethod.NOT_INFORMED;

        touch();
    }

    public void updateCustomer(
            String customerName,
            String customerPhone
    ) {
        this.customerName =
                normalizeOptional(customerName);

        this.customerPhone =
                normalizeOptional(customerPhone);

        touch();
    }

    public void updateNotes(String notes) {
        this.notes =
                normalizeOptional(notes);

        touch();
    }

    private void requireStatus(
            OrderStatus expected,
            String message
    ) {
        if (this.status != expected) {
            throw new IllegalStateException(message);
        }
    }

    private static void requireText(
            String value,
            String message
    ) {
        if (
                value == null
                        || value.isBlank()
        ) {
            throw new IllegalArgumentException(
                    message
            );
        }
    }

    private static String normalizeOptional(
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

    private static BigDecimal normalizeMoney(
            BigDecimal value
    ) {
        return value != null
                ? value
                : BigDecimal.ZERO;
    }

    private void touch() {
        this.updatedAt =
                OffsetDateTime.now();
    }

    @PrePersist
    private void prePersist() {
        OffsetDateTime now =
                OffsetDateTime.now();

        if (this.createdAt == null) {
            this.createdAt = now;
        }

        if (this.updatedAt == null) {
            this.updatedAt = now;
        }

        if (this.status == null) {
            this.status =
                    initialStatusFor(
                            this.channel
                    );
        }

        if (this.paymentStatus == null) {
            this.paymentStatus =
                    PaymentStatus.PENDING;
        }

        if (this.paymentMethod == null) {
            this.paymentMethod =
                    PaymentMethod.NOT_INFORMED;
        }

        if (this.fulfillmentType == null) {
            this.fulfillmentType =
                    FulfillmentType.PICKUP;
        }

        if (this.deliveryFee == null) {
            this.deliveryFee =
                    BigDecimal.ZERO;
        }

        recalculateTotal();
    }

    @PreUpdate
    private void preUpdate() {
        this.updatedAt =
                OffsetDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Long getOrderNumber() {
        return orderNumber;
    }

    /**
     * Pode ser usado como proteção caso o número público
     * ainda não tenha sido recarregado após o INSERT.
     */
    public Long getDisplayNumber() {
        return orderNumber != null
                ? orderNumber
                : id;
    }

    public DailyMenu getDailyMenu() {
        return dailyMenu;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public OrderChannel getChannel() {
        return channel;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public FulfillmentType getFulfillmentType() {
        return fulfillmentType;
    }

    public DeliveryZone getDeliveryZone() {
        return deliveryZone;
    }

    public String getDeliveryNeighborhood() {
        return deliveryNeighborhood;
    }

    public String getDeliveryStreet() {
        return deliveryStreet;
    }

    public String getDeliveryNumber() {
        return deliveryNumber;
    }

    public String getDeliveryComplement() {
        return deliveryComplement;
    }

    public String getDeliveryReference() {
        return deliveryReference;
    }

    public BigDecimal getDeliveryFee() {
        return deliveryFee;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public String getNotes() {
        return notes;
    }

    public List<OrderItem> getItems() {
        return Collections.unmodifiableList(
                items
        );
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public OffsetDateTime getConfirmedAt() {
        return confirmedAt;
    }

    public OffsetDateTime getPreparationStartedAt() {
        return preparationStartedAt;
    }

    public OffsetDateTime getReadyAt() {
        return readyAt;
    }

    public OffsetDateTime getFinishedAt() {
        return finishedAt;
    }

    public OffsetDateTime getCancelledAt() {
        return cancelledAt;
    }
}