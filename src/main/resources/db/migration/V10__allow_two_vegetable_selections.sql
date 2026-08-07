UPDATE menu_categories
SET
    maximum_selections = 2,
    updated_at = CURRENT_TIMESTAMP
WHERE code = 'VEGETABLE';