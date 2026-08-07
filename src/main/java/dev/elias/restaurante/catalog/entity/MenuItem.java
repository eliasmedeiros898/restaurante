package dev.elias.restaurante.catalog.entity;

import jakarta.persistence.*;

import java.time.OffsetDateTime;

@Entity
@Table(
        name = "menu_items",
        schema = "public",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_menu_items_category_name",
                        columnNames = {
                                "category_id",
                                "name"
                        }
                )
        }
)
public class MenuItem {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;

    @ManyToOne(
            fetch = FetchType.LAZY,
            optional = false
    )
    @JoinColumn(
            name = "category_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_menu_items_category"
            )
    )
    private MenuCategory category;

    @Column(
            nullable = false,
            length = 120
    )
    private String name;

    @Column(length = 500)
    private String description;

    @Column(
            name = "basic_plan_eligible",
            nullable = false
    )
    private Boolean basicPlanEligible;

    @Column(nullable = false)
    private Boolean active;

    @Column(
            name = "created_at",
            nullable = false
    )
    private OffsetDateTime createdAt;

    @Column(
            name = "updated_at",
            nullable = false
    )
    private OffsetDateTime updatedAt;

    protected MenuItem() {
    }

    public MenuItem(
            MenuCategory category,
            String name,
            String description,
            Boolean basicPlanEligible,
            Boolean active
    ) {
        this.category = category;
        this.name = name;
        this.description = description;
        this.basicPlanEligible =
                basicPlanEligible;
        this.active = active;
    }

    @PrePersist
    private void prePersist() {
        OffsetDateTime now =
                OffsetDateTime.now();

        this.createdAt = now;
        this.updatedAt = now;

        if (this.active == null) {
            this.active = true;
        }

        if (this.basicPlanEligible == null) {
            this.basicPlanEligible = false;
        }
    }

    @PreUpdate
    private void preUpdate() {
        this.updatedAt =
                OffsetDateTime.now();
    }

    public void update(
            MenuCategory category,
            String name,
            String description,
            Boolean basicPlanEligible
    ) {
        this.category = category;
        this.name = name;
        this.description = description;
        this.basicPlanEligible =
                basicPlanEligible;
    }

    public void setActive(
            Boolean active
    ) {
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public MenuCategory getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Boolean getBasicPlanEligible() {
        return basicPlanEligible;
    }

    public Boolean getActive() {
        return active;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }
}