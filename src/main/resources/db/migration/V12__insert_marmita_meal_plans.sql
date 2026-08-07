INSERT INTO meal_plans (
    code,
    name,
    price,
    meat_quantity,
    basic_meats_only,
    active,
    created_at,
    updated_at
)
VALUES
    (
        'MARMITA_5',
        'Marmita com 5 carnes',
        35.00,
        5,
        FALSE,
        TRUE,
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP
    ),
    (
        'MARMITA_6',
        'Marmita com 6 carnes',
        40.00,
        6,
        FALSE,
        TRUE,
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP
    ),
    (
        'MARMITA_7',
        'Marmita com 7 carnes',
        45.00,
        7,
        FALSE,
        TRUE,
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP
    ),
    (
        'MARMITA_8',
        'Marmita com 8 carnes',
        50.00,
        8,
        FALSE,
        TRUE,
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP
    ),
    (
        'MARMITA_9',
        'Marmita com 9 carnes',
        55.00,
        9,
        FALSE,
        TRUE,
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP
    ),
    (
        'MARMITA_10',
        'Marmita com 10 carnes',
        60.00,
        10,
        FALSE,
        TRUE,
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP
    )
ON CONFLICT (code)
    DO UPDATE SET
                  name = EXCLUDED.name,
                  price = EXCLUDED.price,
                  meat_quantity = EXCLUDED.meat_quantity,
                  basic_meats_only = EXCLUDED.basic_meats_only,
                  active = EXCLUDED.active,
                  updated_at = CURRENT_TIMESTAMP;