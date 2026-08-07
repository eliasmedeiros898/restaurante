package dev.elias.restaurante.menu.entity;

import dev.elias.restaurante.catalog.entity.MenuItem;
import jakarta.persistence.*;

import java.time.OffsetDateTime;

@Entity
@Table(
        name = "daily_menu_items",
        schema = "public",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_daily_menu_items",
                        columnNames = {
                                "daily_menu_id",
                                "menu_item_id"
                        }
                )
        }
)
public class DailyMenuItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "daily_menu_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_daily_menu_items_menu"
            )
    )
    private DailyMenu dailyMenu;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "menu_item_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_daily_menu_items_item"
            )
    )
    private MenuItem menuItem;

    @Column(nullable = false)
    private Boolean available;

    @Column(name = "display_order", nullable = false)
    private Integer displayOrder;

    @Column(name = "unavailable_reason", length = 255)
    private String unavailableReason;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    protected DailyMenuItem() {
    }

    public DailyMenuItem(
            MenuItem menuItem,
            Boolean available,
            Integer displayOrder
    ) {
        this.menuItem = menuItem;
        this.available = available;
        this.displayOrder = displayOrder;
        this.createdAt = OffsetDateTime.now();
        this.updatedAt = OffsetDateTime.now();
    }

    public void changeAvailability(
            Boolean available,
            String unavailableReason
    ) {
        this.available = available;

        if (Boolean.TRUE.equals(available)) {
            this.unavailableReason = null;
        } else {
            this.unavailableReason = unavailableReason;
        }
    }

    public void setDailyMenu(DailyMenu dailyMenu) {
        this.dailyMenu = dailyMenu;
    }

    public Long getId() {
        return id;
    }

    public DailyMenu getDailyMenu() {
        return dailyMenu;
    }

    public MenuItem getMenuItem() {
        return menuItem;
    }

    public Boolean getAvailable() {
        return available;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public String getUnavailableReason() {
        return unavailableReason;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }
}