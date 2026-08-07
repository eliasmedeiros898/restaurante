CREATE TABLE delivery_zones (
                                id BIGSERIAL PRIMARY KEY,
                                neighborhood VARCHAR(120) NOT NULL,
                                normalized_neighborhood VARCHAR(120) NOT NULL,
                                delivery_fee NUMERIC(10, 2) NOT NULL,
                                display_order INTEGER NOT NULL DEFAULT 0,
                                active BOOLEAN NOT NULL DEFAULT TRUE,
                                created_at TIMESTAMP WITH TIME ZONE NOT NULL,
                                updated_at TIMESTAMP WITH TIME ZONE NOT NULL,

                                CONSTRAINT uk_delivery_zones_normalized_neighborhood
                                    UNIQUE (normalized_neighborhood),

                                CONSTRAINT ck_delivery_zones_fee_non_negative
                                    CHECK (delivery_fee >= 0)
);

CREATE INDEX idx_delivery_zones_active_order
    ON delivery_zones (active, display_order, neighborhood);