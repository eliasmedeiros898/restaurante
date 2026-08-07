package dev.elias.restaurante.catalog.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(
        name = "meal_plans",
        schema = "public",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_meal_plans_code",
                        columnNames = "code"
                )
        }
)
public class MealPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            nullable = false,
            unique = true,
            length = 50
    )
    private String code;

    @Column(
            nullable = false,
            length = 100
    )
    private String name;

    @Column(
            nullable = false,
            precision = 10,
            scale = 2
    )
    private BigDecimal price;

    @Column(
            name = "meat_quantity",
            nullable = false
    )
    private Integer meatQuantity;

    @Column(
            name = "basic_meats_only",
            nullable = false
    )
    private Boolean basicMeatsOnly;

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

    protected MealPlan() {
    }

    public MealPlan(
            String code,
            String name,
            BigDecimal price,
            Integer meatQuantity,
            Boolean basicMeatsOnly,
            Boolean active
    ) {
        this.code = code;
        this.name = name;
        this.price = price;
        this.meatQuantity = meatQuantity;
        this.basicMeatsOnly = basicMeatsOnly;
        this.active = active;
    }

    @PrePersist
    private void prePersist() {
        OffsetDateTime now =
                OffsetDateTime.now();

        if (createdAt == null) {
            createdAt = now;
        }

        updatedAt = now;

        if (active == null) {
            active = true;
        }

        if (basicMeatsOnly == null) {
            basicMeatsOnly = false;
        }
    }

    @PreUpdate
    private void preUpdate() {
        updatedAt =
                OffsetDateTime.now();
    }

    public void update(
            String code,
            String name,
            BigDecimal price,
            Integer meatQuantity,
            Boolean basicMeatsOnly
    ) {
        this.code = code;
        this.name = name;
        this.price = price;
        this.meatQuantity = meatQuantity;
        this.basicMeatsOnly =
                basicMeatsOnly;
    }

    public void setActive(
            Boolean active
    ) {
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Integer getMeatQuantity() {
        return meatQuantity;
    }

    public Boolean getBasicMeatsOnly() {
        return basicMeatsOnly;
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