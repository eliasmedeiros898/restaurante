ALTER TABLE order_item_selections
    ADD COLUMN quantity INTEGER NOT NULL DEFAULT 1;

ALTER TABLE order_item_selections
    ADD CONSTRAINT ck_order_item_selections_quantity_positive
        CHECK (quantity > 0);