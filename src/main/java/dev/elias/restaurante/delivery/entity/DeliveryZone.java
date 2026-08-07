package dev.elias.restaurante.delivery.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.text.Normalizer;
import java.time.OffsetDateTime;
import java.util.Locale;

@Entity
@Table(name = "delivery_zones")
public class DeliveryZone {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;

    @Column(
            nullable = false,
            length = 120
    )
    private String neighborhood;

    @Column(
            name = "normalized_neighborhood",
            nullable = false,
            unique = true,
            length = 120
    )
    private String normalizedNeighborhood;

    @Column(
            name = "delivery_fee",
            nullable = false,
            precision = 10,
            scale = 2
    )
    private BigDecimal deliveryFee;

    @Column(
            name = "display_order",
            nullable = false
    )
    private Integer displayOrder;

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

    protected DeliveryZone() {
    }

    public DeliveryZone(
            String neighborhood,
            BigDecimal deliveryFee,
            Integer displayOrder
    ) {
        update(
                neighborhood,
                deliveryFee,
                displayOrder,
                true
        );

        this.createdAt = OffsetDateTime.now();
        this.updatedAt = OffsetDateTime.now();
    }

    public void update(
            String neighborhood,
            BigDecimal deliveryFee,
            Integer displayOrder,
            Boolean active
    ) {
        this.neighborhood =
                neighborhood.trim();

        this.normalizedNeighborhood =
                normalize(neighborhood);

        this.deliveryFee = deliveryFee;
        this.displayOrder = displayOrder;
        this.active = active;
        this.updatedAt = OffsetDateTime.now();
    }

    private static String normalize(
            String value
    ) {
        String withoutAccents =
                Normalizer
                        .normalize(
                                value.trim(),
                                Normalizer.Form.NFD
                        )
                        .replaceAll(
                                "\\p{M}",
                                ""
                        );

        return withoutAccents
                .toLowerCase(Locale.ROOT)
                .replaceAll("\\s+", " ");
    }

    public Long getId() {
        return id;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public BigDecimal getDeliveryFee() {
        return deliveryFee;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public Boolean getActive() {
        return active;
    }
}