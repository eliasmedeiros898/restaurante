ALTER TABLE public.order_items
    DROP CONSTRAINT IF EXISTS ck_order_items_meal_type;

ALTER TABLE public.order_items
    ADD CONSTRAINT ck_order_items_meal_type
        CHECK (
            meal_type IN (
                          'QUENTINHA',
                          'PF',
                          'MARMITA'
                )
            );