-- ============================================================
-- USUÁRIOS, CATEGORIAS, ITENS E PLANOS DE REFEIÇÃO
-- ============================================================

CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       name VARCHAR(120) NOT NULL,
                       email VARCHAR(180) NOT NULL,
                       password_hash VARCHAR(255) NOT NULL,
                       role VARCHAR(30) NOT NULL,
                       active BOOLEAN NOT NULL DEFAULT TRUE,
                       created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

                       CONSTRAINT ck_users_name_not_blank
                           CHECK (BTRIM(name) <> ''),

                       CONSTRAINT ck_users_email_not_blank
                           CHECK (BTRIM(email) <> ''),

                       CONSTRAINT ck_users_role
                           CHECK (role IN ('ADMIN', 'ATENDENTE', 'COZINHA'))
);

CREATE UNIQUE INDEX uk_users_email_lower
    ON users (LOWER(email));


CREATE TABLE menu_categories (
                                 id BIGSERIAL PRIMARY KEY,
                                 code VARCHAR(50) NOT NULL,
                                 name VARCHAR(100) NOT NULL,
                                 display_order INTEGER NOT NULL DEFAULT 0,
                                 minimum_selections INTEGER NOT NULL DEFAULT 0,
                                 maximum_selections INTEGER NOT NULL DEFAULT 1,
                                 required BOOLEAN NOT NULL DEFAULT FALSE,
                                 active BOOLEAN NOT NULL DEFAULT TRUE,
                                 created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                 updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

                                 CONSTRAINT uk_menu_categories_code UNIQUE (code),

                                 CONSTRAINT ck_menu_categories_code_not_blank
                                     CHECK (BTRIM(code) <> ''),

                                 CONSTRAINT ck_menu_categories_name_not_blank
                                     CHECK (BTRIM(name) <> ''),

                                 CONSTRAINT ck_menu_categories_minimum
                                     CHECK (minimum_selections >= 0),

                                 CONSTRAINT ck_menu_categories_maximum
                                     CHECK (maximum_selections >= 1),

                                 CONSTRAINT ck_menu_categories_selection_range
                                     CHECK (minimum_selections <= maximum_selections)
);


CREATE TABLE menu_items (
                            id BIGSERIAL PRIMARY KEY,
                            category_id BIGINT NOT NULL,
                            name VARCHAR(120) NOT NULL,
                            description VARCHAR(500),
                            basic_plan_eligible BOOLEAN NOT NULL DEFAULT FALSE,
                            active BOOLEAN NOT NULL DEFAULT TRUE,
                            created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

                            CONSTRAINT fk_menu_items_category
                                FOREIGN KEY (category_id)
                                    REFERENCES menu_categories (id)
                                    ON DELETE RESTRICT,

                            CONSTRAINT uk_menu_items_category_name
                                UNIQUE (category_id, name),

    -- Permite uma chave estrangeira composta nas seleções do pedido.
                            CONSTRAINT uk_menu_items_id_category
                                UNIQUE (id, category_id),

                            CONSTRAINT ck_menu_items_name_not_blank
                                CHECK (BTRIM(name) <> '')
);


CREATE TABLE meal_plans (
                            id BIGSERIAL PRIMARY KEY,
                            code VARCHAR(50) NOT NULL,
                            name VARCHAR(100) NOT NULL,
                            price NUMERIC(10,2) NOT NULL,
                            meat_quantity INTEGER NOT NULL,
                            basic_meats_only BOOLEAN NOT NULL DEFAULT FALSE,
                            active BOOLEAN NOT NULL DEFAULT TRUE,
                            created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

                            CONSTRAINT uk_meal_plans_code UNIQUE (code),

                            CONSTRAINT ck_meal_plans_code_not_blank
                                CHECK (BTRIM(code) <> ''),

                            CONSTRAINT ck_meal_plans_name_not_blank
                                CHECK (BTRIM(name) <> ''),

                            CONSTRAINT ck_meal_plans_price
                                CHECK (price >= 0),

                            CONSTRAINT ck_meal_plans_meat_quantity
                                CHECK (meat_quantity >= 1)
);