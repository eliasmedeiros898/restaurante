ALTER TABLE orders
    ADD COLUMN fulfillment_type VARCHAR(20)
        NOT NULL DEFAULT 'PICKUP',

    ADD COLUMN delivery_zone_id BIGINT,

    ADD COLUMN delivery_neighborhood VARCHAR(120),

    ADD COLUMN delivery_street VARCHAR(160),

    ADD COLUMN delivery_number VARCHAR(30),

    ADD COLUMN delivery_complement VARCHAR(160),

    ADD COLUMN delivery_reference VARCHAR(255),

    ADD COLUMN delivery_fee NUMERIC(10, 2)
        NOT NULL DEFAULT 0;

ALTER TABLE orders
    ADD CONSTRAINT fk_orders_delivery_zone
        FOREIGN KEY (delivery_zone_id)
            REFERENCES delivery_zones (id);

ALTER TABLE orders
    ADD CONSTRAINT ck_orders_fulfillment_type
        CHECK (
            fulfillment_type IN (
                                 'PICKUP',
                                 'DELIVERY'
                )
            );

ALTER TABLE orders
    ADD CONSTRAINT ck_orders_delivery_fee_non_negative
        CHECK (delivery_fee >= 0);