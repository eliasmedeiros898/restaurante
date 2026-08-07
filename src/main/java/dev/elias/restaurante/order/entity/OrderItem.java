package dev.elias.restaurante.order.entity;

import dev.elias.restaurante.catalog.entity.MealPlan;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "order_items",
        schema = "public"
)
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "order_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_order_items_order"
            )
    )
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "meal_plan_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_order_items_meal_plan"
            )
    )
    private MealPlan mealPlan;

    @Enumerated(EnumType.STRING)
    @Column(name = "meal_type", nullable = false, length = 30)
    private MealType mealType;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "unit_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;

    @Column(length = 500)
    private String notes;

    @OneToMany(
            mappedBy = "orderItem",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<OrderItemSelection> selections = new ArrayList<>();

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    protected OrderItem() {
    }

    public OrderItem(
            MealPlan mealPlan,
            MealType mealType,
            Integer quantity,
            String notes
    ) {
        this.mealPlan = mealPlan;
        this.mealType = mealType;
        this.quantity = quantity;
        this.unitPrice = mealPlan.getPrice();
        this.subtotal = mealPlan.getPrice()
                .multiply(BigDecimal.valueOf(quantity));
        this.notes = notes;
        this.createdAt = OffsetDateTime.now();
        this.updatedAt = OffsetDateTime.now();
    }

    public void addSelection(OrderItemSelection selection) {
        selections.add(selection);
        selection.setOrderItem(this);
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Long getId() {
        return id;
    }

    public Order getOrder() {
        return order;
    }

    public MealPlan getMealPlan() {
        return mealPlan;
    }

    public MealType getMealType() {
        return mealType;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public String getNotes() {
        return notes;
    }

    public List<OrderItemSelection> getSelections() {
        return selections;
    }

    public void assignOrder(Order order) {
        this.order = order;
    }

    public void removeOrder() {
        this.order = null;
    }
}