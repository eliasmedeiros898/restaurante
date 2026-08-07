package dev.elias.restaurante.menu.entity;

import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "menu_templates",
        schema = "public",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_menu_templates_day_of_week",
                        columnNames = "day_of_week"
                )
        }
)
public class MenuTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week", nullable = false, length = 20)
    private DayOfWeekName dayOfWeek;

    @Column(nullable = false)
    private Boolean active;

    @OneToMany(
            mappedBy = "menuTemplate",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @OrderBy("displayOrder ASC")
    private List<MenuTemplateItem> items = new ArrayList<>();

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    protected MenuTemplate() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public DayOfWeekName getDayOfWeek() {
        return dayOfWeek;
    }

    public Boolean getActive() {
        return active;
    }

    public List<MenuTemplateItem> getItems() {
        return items;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }
}