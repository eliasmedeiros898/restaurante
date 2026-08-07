-- ============================================================
-- CARDÁPIOS PADRÃO E CARDÁPIOS DIÁRIOS
-- ============================================================

CREATE TABLE menu_templates (
                                id BIGSERIAL PRIMARY KEY,
                                name VARCHAR(100) NOT NULL,
                                day_of_week VARCHAR(20) NOT NULL,
                                active BOOLEAN NOT NULL DEFAULT TRUE,
                                created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

                                CONSTRAINT uk_menu_templates_day_of_week
                                    UNIQUE (day_of_week),

                                CONSTRAINT ck_menu_templates_name_not_blank
                                    CHECK (BTRIM(name) <> ''),

                                CONSTRAINT ck_menu_templates_day_of_week
                                    CHECK (
                                        day_of_week IN (
                                                        'MONDAY',
                                                        'TUESDAY',
                                                        'WEDNESDAY',
                                                        'THURSDAY',
                                                        'FRIDAY',
                                                        'SATURDAY',
                                                        'SUNDAY'
                                            )
                                        )
);


CREATE TABLE menu_template_items (
                                     id BIGSERIAL PRIMARY KEY,
                                     menu_template_id BIGINT NOT NULL,
                                     menu_item_id BIGINT NOT NULL,
                                     display_order INTEGER NOT NULL DEFAULT 0,
                                     created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

                                     CONSTRAINT fk_menu_template_items_template
                                         FOREIGN KEY (menu_template_id)
                                             REFERENCES menu_templates (id)
                                             ON DELETE CASCADE,

                                     CONSTRAINT fk_menu_template_items_item
                                         FOREIGN KEY (menu_item_id)
                                             REFERENCES menu_items (id)
                                             ON DELETE RESTRICT,

                                     CONSTRAINT uk_menu_template_items
                                         UNIQUE (menu_template_id, menu_item_id)
);


CREATE TABLE daily_menus (
                             id BIGSERIAL PRIMARY KEY,
                             menu_date DATE NOT NULL,
                             day_of_week VARCHAR(20) NOT NULL,
                             open BOOLEAN NOT NULL DEFAULT TRUE,
                             opening_time TIME,
                             closing_time TIME,
                             source_template_id BIGINT,
                             notes VARCHAR(500),
                             created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                             updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

                             CONSTRAINT uk_daily_menus_date
                                 UNIQUE (menu_date),

                             CONSTRAINT fk_daily_menus_template
                                 FOREIGN KEY (source_template_id)
                                     REFERENCES menu_templates (id)
                                     ON DELETE SET NULL,

                             CONSTRAINT ck_daily_menus_day_of_week
                                 CHECK (
                                     day_of_week IN (
                                                     'MONDAY',
                                                     'TUESDAY',
                                                     'WEDNESDAY',
                                                     'THURSDAY',
                                                     'FRIDAY',
                                                     'SATURDAY',
                                                     'SUNDAY'
                                         )
                                     ),

                             CONSTRAINT ck_daily_menus_times
                                 CHECK (
                                     opening_time IS NULL
                                         OR closing_time IS NULL
                                         OR closing_time > opening_time
                                     )
);


CREATE TABLE daily_menu_items (
                                  id BIGSERIAL PRIMARY KEY,
                                  daily_menu_id BIGINT NOT NULL,
                                  menu_item_id BIGINT NOT NULL,
                                  available BOOLEAN NOT NULL DEFAULT TRUE,
                                  display_order INTEGER NOT NULL DEFAULT 0,
                                  unavailable_reason VARCHAR(255),
                                  created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                  updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

                                  CONSTRAINT fk_daily_menu_items_menu
                                      FOREIGN KEY (daily_menu_id)
                                          REFERENCES daily_menus (id)
                                          ON DELETE CASCADE,

                                  CONSTRAINT fk_daily_menu_items_item
                                      FOREIGN KEY (menu_item_id)
                                          REFERENCES menu_items (id)
                                          ON DELETE RESTRICT,

                                  CONSTRAINT uk_daily_menu_items
                                      UNIQUE (daily_menu_id, menu_item_id),

                                  CONSTRAINT ck_daily_menu_items_unavailability
                                      CHECK (
                                          available = TRUE
                                              OR unavailable_reason IS NULL
                                              OR BTRIM(unavailable_reason) <> ''
                                          )
);