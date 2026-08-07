package dev.elias.restaurante.menu.entity;

import dev.elias.restaurante.catalog.entity.MenuItem;
import jakarta.persistence.*;

import java.time.OffsetDateTime;

@Entity
@Table(
        name = "menu_template_items",
        schema = "public",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_menu_template_items",
                        columnNames = {
                                "menu_template_id",
                                "menu_item_id"
                        }
                )
        }
)
public class MenuTemplateItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "menu_template_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_menu_template_items_template"
            )
    )
    private MenuTemplate menuTemplate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "menu_item_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_menu_template_items_item"
            )
    )
    private MenuItem menuItem;

    @Column(name = "display_order", nullable = false)
    private Integer displayOrder;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    protected MenuTemplateItem() {
    }

    public Long getId() {
        return id;
    }

    public MenuTemplate getMenuTemplate() {
        return menuTemplate;
    }

    public MenuItem getMenuItem() {
        return menuItem;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }
}