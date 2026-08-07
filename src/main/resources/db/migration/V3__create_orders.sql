-- ============================================================
-- PEDIDOS, ITENS E SELEÇÕES
-- ============================================================

CREATE TABLE orders (
                        id BIGSERIAL PRIMARY KEY,
                        order_number BIGINT GENERATED ALWAYS AS IDENTITY,
                        daily_menu_id BIGINT,
                        created_by_user_id BIGINT,

                        customer_name VARCHAR(120),
                        customer_phone VARCHAR(30),

                        channel VARCHAR(30) NOT NULL,
                        status VARCHAR(40) NOT NULL DEFAULT 'AWAITING_CONFIRMATION',

                        payment_method VARCHAR(30) NOT NULL DEFAULT 'NOT_INFORMED',
                        payment_status VARCHAR(30) NOT NULL DEFAULT 'PENDING',

                        total NUMERIC(10,2) NOT NULL DEFAULT 0,
                        notes VARCHAR(1000),

                        created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        confirmed_at TIMESTAMP WITH TIME ZONE,
                        preparation_started_at TIMESTAMP WITH TIME ZONE,
                        ready_at TIMESTAMP WITH TIME ZONE,
                        finished_at TIMESTAMP WITH TIME ZONE,
                        cancelled_at TIMESTAMP WITH TIME ZONE,

                        CONSTRAINT uk_orders_order_number
                            UNIQUE (order_number),

                        CONSTRAINT fk_orders_daily_menu
                            FOREIGN KEY (daily_menu_id)
                                REFERENCES daily_menus (id)
                                ON DELETE SET NULL,

                        CONSTRAINT fk_orders_created_by_user
                            FOREIGN KEY (created_by_user_id)
                                REFERENCES users (id)
                                ON DELETE SET NULL,

                        CONSTRAINT ck_orders_channel
                            CHECK (channel IN ('COUNTER', 'ONLINE_WHATSAPP')),

                        CONSTRAINT ck_orders_status
                            CHECK (
                                status IN (
                                           'AWAITING_CONFIRMATION',
                                           'CONFIRMED',
                                           'PREPARING',
                                           'READY',
                                           'DELIVERED',
                                           'CANCELLED'
                                    )
                                ),

                        CONSTRAINT ck_orders_payment_method
                            CHECK (
                                payment_method IN (
                                                   'CASH',
                                                   'PIX',
                                                   'CREDIT_CARD',
                                                   'DEBIT_CARD',
                                                   'NOT_INFORMED'
                                    )
                                ),

                        CONSTRAINT ck_orders_payment_status
                            CHECK (
                                payment_status IN (
                                                   'PENDING',
                                                   'PAID',
                                                   'CANCELLED'
                                    )
                                ),

                        CONSTRAINT ck_orders_total
                            CHECK (total >= 0),

                        CONSTRAINT ck_orders_customer_name
                            CHECK (
                                customer_name IS NULL
                                    OR BTRIM(customer_name) <> ''
                                ),

                        CONSTRAINT ck_orders_customer_phone
                            CHECK (
                                customer_phone IS NULL
                                    OR BTRIM(customer_phone) <> ''
                                )
);


CREATE TABLE order_items (
                             id BIGSERIAL PRIMARY KEY,
                             order_id BIGINT NOT NULL,
                             meal_plan_id BIGINT NOT NULL,

                             meal_type VARCHAR(30) NOT NULL,
                             quantity INTEGER NOT NULL DEFAULT 1,

                             unit_price NUMERIC(10,2) NOT NULL,
                             subtotal NUMERIC(10,2) NOT NULL,

                             notes VARCHAR(500),
                             created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                             updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

                             CONSTRAINT fk_order_items_order
                                 FOREIGN KEY (order_id)
                                     REFERENCES orders (id)
                                     ON DELETE CASCADE,

                             CONSTRAINT fk_order_items_meal_plan
                                 FOREIGN KEY (meal_plan_id)
                                     REFERENCES meal_plans (id)
                                     ON DELETE RESTRICT,

                             CONSTRAINT ck_order_items_meal_type
                                 CHECK (meal_type IN ('QUENTINHA', 'PF')),

                             CONSTRAINT ck_order_items_quantity
                                 CHECK (quantity >= 1),

                             CONSTRAINT ck_order_items_unit_price
                                 CHECK (unit_price >= 0),

                             CONSTRAINT ck_order_items_subtotal
                                 CHECK (subtotal >= 0),

                             CONSTRAINT ck_order_items_subtotal_calculation
                                 CHECK (subtotal = unit_price * quantity)
);


CREATE TABLE order_item_selections (
                                       id BIGSERIAL PRIMARY KEY,
                                       order_item_id BIGINT NOT NULL,
                                       category_id BIGINT NOT NULL,
                                       menu_item_id BIGINT NOT NULL,
                                       created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

                                       CONSTRAINT fk_order_item_selections_order_item
                                           FOREIGN KEY (order_item_id)
                                               REFERENCES order_items (id)
                                               ON DELETE CASCADE,

                                       CONSTRAINT fk_order_item_selections_category
                                           FOREIGN KEY (category_id)
                                               REFERENCES menu_categories (id)
                                               ON DELETE RESTRICT,

    -- Garante que o item escolhido pertence à categoria informada.
                                       CONSTRAINT fk_order_item_selections_item_category
                                           FOREIGN KEY (menu_item_id, category_id)
                                               REFERENCES menu_items (id, category_id)
                                               ON DELETE RESTRICT,

    -- Impede selecionar o mesmo item duas vezes na mesma refeição.
                                       CONSTRAINT uk_order_item_selections
                                           UNIQUE (order_item_id, menu_item_id)
);