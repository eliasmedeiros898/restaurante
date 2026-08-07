package dev.elias.restaurante.order.entity;

import dev.elias.restaurante.catalog.entity.MenuCategory;
import dev.elias.restaurante.catalog.entity.MenuItem;
import jakarta.persistence.*;

import java.time.OffsetDateTime;

@Entity
@Table(
        name = "order_item_selections",
        schema = "public",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_order_item_selections",
                        columnNames = {
                                "order_item_id",
                                "menu_item_id"
                        }
                )
        }
)
public class OrderItemSelection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "order_item_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_order_item_selections_order_item"
            )
    )
    private OrderItem orderItem;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "category_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_order_item_selections_category"
            )
    )
    private MenuCategory category;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "menu_item_id",
            nullable = false
    )
    private MenuItem menuItem;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    protected OrderItemSelection() {
    }

    private Integer quantity;

    public OrderItemSelection(
            MenuCategory category,
            MenuItem menuItem,
            Integer quantity
    ) {
        this.category = category;
        this.menuItem = menuItem;
        this.quantity = quantity;
        this.createdAt = OffsetDateTime.now();
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setOrderItem(OrderItem orderItem) {
        this.orderItem = orderItem;
    }

    public Long getId() {
        return id;
    }

    public OrderItem getOrderItem() {
        return orderItem;
    }

    public MenuCategory getCategory() {
        return category;
    }

    public MenuItem getMenuItem() {
        return menuItem;
    }
}