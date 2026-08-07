package dev.elias.restaurante.menu.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "daily_menus",
        schema = "public",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_daily_menus_date",
                        columnNames = "menu_date"
                )
        }
)
public class DailyMenu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "menu_date", nullable = false)
    private LocalDate menuDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week", nullable = false, length = 20)
    private DayOfWeekName dayOfWeek;

    @Column(nullable = false)
    private Boolean open;

    @Column(name = "opening_time")
    private LocalTime openingTime;

    @Column(name = "closing_time")
    private LocalTime closingTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "source_template_id",
            foreignKey = @ForeignKey(
                    name = "fk_daily_menus_template"
            )
    )
    private MenuTemplate sourceTemplate;

    @Column(length = 500)
    private String notes;

    @OneToMany(
            mappedBy = "dailyMenu",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @OrderBy("displayOrder ASC")
    private List<DailyMenuItem> items = new ArrayList<>();

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    protected DailyMenu() {
    }

    public DailyMenu(
            LocalDate menuDate,
            DayOfWeekName dayOfWeek,
            Boolean open,
            LocalTime openingTime,
            LocalTime closingTime,
            MenuTemplate sourceTemplate,
            String notes
    ) {
        this.menuDate = menuDate;
        this.dayOfWeek = dayOfWeek;
        this.open = open;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
        this.sourceTemplate = sourceTemplate;
        this.notes = notes;
        this.createdAt = OffsetDateTime.now();
        this.updatedAt = OffsetDateTime.now();
    }

    public void addItem(DailyMenuItem item) {
        items.add(item);
        item.setDailyMenu(this);
    }

    public void removeItem(
            DailyMenuItem item
    ) {
        if (item == null) {
            return;
        }

        items.remove(item);
        item.setDailyMenu(null);
    }

    public Long getId() {
        return id;
    }

    public LocalDate getMenuDate() {
        return menuDate;
    }

    public DayOfWeekName getDayOfWeek() {
        return dayOfWeek;
    }

    public Boolean getOpen() {
        return open;
    }

    public LocalTime getOpeningTime() {
        return openingTime;
    }

    public LocalTime getClosingTime() {
        return closingTime;
    }

    public MenuTemplate getSourceTemplate() {
        return sourceTemplate;
    }

    public String getNotes() {
        return notes;
    }

    public List<DailyMenuItem> getItems() {
        return items;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setOpen(Boolean open) {
        this.open = open;
    }

    public void setOpeningTime(LocalTime openingTime) {
        this.openingTime = openingTime;
    }

    public void setClosingTime(LocalTime closingTime) {
        this.closingTime = closingTime;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}