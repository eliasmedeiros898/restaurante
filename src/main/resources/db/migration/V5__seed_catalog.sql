-- ============================================================
-- PLANOS
-- ============================================================

INSERT INTO meal_plans (
    code,
    name,
    price,
    meat_quantity,
    basic_meats_only
)
VALUES
    ('ONE_MEAT', '1 carne', 14.00, 1, TRUE),
    ('TWO_MEATS', '2 carnes', 17.00, 2, FALSE),
    ('THREE_MEATS', '3 carnes', 20.00, 3, FALSE)
ON CONFLICT (code) DO NOTHING;


-- ============================================================
-- CATEGORIAS
-- ============================================================

INSERT INTO menu_categories (
    code,
    name,
    display_order,
    minimum_selections,
    maximum_selections,
    required
)
VALUES
    ('BEAN', 'Feijão', 10, 1, 1, TRUE),
    ('RICE', 'Arroz', 20, 1, 1, TRUE),
    ('PASTA', 'Massa', 30, 0, 1, FALSE),
    ('VEGETABLE', 'Verdura', 40, 0, 1, FALSE),
    ('SIDE_DISH', 'Acompanhamento', 50, 0, 2, FALSE),
    ('MEAT', 'Carne', 60, 1, 3, TRUE)
ON CONFLICT (code) DO NOTHING;


-- ============================================================
-- FEIJÕES
-- ============================================================

INSERT INTO menu_items (category_id, name)
SELECT id, 'Feijão na farofa'
FROM menu_categories
WHERE code = 'BEAN'
ON CONFLICT (category_id, name) DO NOTHING;

INSERT INTO menu_items (category_id, name)
SELECT id, 'Feijão no caldo'
FROM menu_categories
WHERE code = 'BEAN'
ON CONFLICT (category_id, name) DO NOTHING;

INSERT INTO menu_items (category_id, name)
SELECT id, 'Feijão preto'
FROM menu_categories
WHERE code = 'BEAN'
ON CONFLICT (category_id, name) DO NOTHING;


-- ============================================================
-- ARROZES
-- ============================================================

INSERT INTO menu_items (category_id, name)
SELECT id, 'Arroz refogado'
FROM menu_categories
WHERE code = 'RICE'
ON CONFLICT (category_id, name) DO NOTHING;

INSERT INTO menu_items (category_id, name)
SELECT id, 'Arroz de leite'
FROM menu_categories
WHERE code = 'RICE'
ON CONFLICT (category_id, name) DO NOTHING;

INSERT INTO menu_items (category_id, name)
SELECT id, 'Arroz na graxa'
FROM menu_categories
WHERE code = 'RICE'
ON CONFLICT (category_id, name) DO NOTHING;


-- ============================================================
-- MASSAS
-- ============================================================

INSERT INTO menu_items (category_id, name)
SELECT id, 'Macarrão'
FROM menu_categories
WHERE code = 'PASTA'
ON CONFLICT (category_id, name) DO NOTHING;


-- ============================================================
-- VERDURAS
-- ============================================================

INSERT INTO menu_items (category_id, name)
SELECT id, 'Verdura crua'
FROM menu_categories
WHERE code = 'VEGETABLE'
ON CONFLICT (category_id, name) DO NOTHING;

INSERT INTO menu_items (category_id, name)
SELECT id, 'Verdura cozida'
FROM menu_categories
WHERE code = 'VEGETABLE'
ON CONFLICT (category_id, name) DO NOTHING;

INSERT INTO menu_items (category_id, name)
SELECT id, 'Verdura doce'
FROM menu_categories
WHERE code = 'VEGETABLE'
ON CONFLICT (category_id, name) DO NOTHING;

INSERT INTO menu_items (category_id, name)
SELECT id, 'Verdura refogada'
FROM menu_categories
WHERE code = 'VEGETABLE'
ON CONFLICT (category_id, name) DO NOTHING;


-- ============================================================
-- OUTROS ACOMPANHAMENTOS
-- ============================================================

INSERT INTO menu_items (category_id, name)
SELECT id, 'Arrubacão'
FROM menu_categories
WHERE code = 'SIDE_DISH'
ON CONFLICT (category_id, name) DO NOTHING;

INSERT INTO menu_items (category_id, name)
SELECT id, 'Batata-doce'
FROM menu_categories
WHERE code = 'SIDE_DISH'
ON CONFLICT (category_id, name) DO NOTHING;


-- ============================================================
-- CARNES
-- ============================================================
-- Foi considerado inicialmente que as preparações de frango,
-- linguiça e fígado são elegíveis para o plano de R$ 14,00.
-- Essa configuração pode ser alterada pelo painel posteriormente.
-- ============================================================

INSERT INTO menu_items (
    category_id,
    name,
    basic_plan_eligible
)
SELECT id, 'Frango assado', TRUE
FROM menu_categories
WHERE code = 'MEAT'
ON CONFLICT (category_id, name) DO NOTHING;

INSERT INTO menu_items (
    category_id,
    name,
    basic_plan_eligible
)
SELECT id, 'Frango ao molho', FALSE
FROM menu_categories
WHERE code = 'MEAT'
ON CONFLICT (category_id, name) DO NOTHING;

INSERT INTO menu_items (
    category_id,
    name,
    basic_plan_eligible
)
SELECT id, 'Linguiça', TRUE
FROM menu_categories
WHERE code = 'MEAT'
ON CONFLICT (category_id, name) DO NOTHING;

INSERT INTO menu_items (
    category_id,
    name,
    basic_plan_eligible
)
SELECT id, 'Fígado', TRUE
FROM menu_categories
WHERE code = 'MEAT'
ON CONFLICT (category_id, name) DO NOTHING;

INSERT INTO menu_items (category_id, name)
SELECT id, 'Bife de carne'
FROM menu_categories
WHERE code = 'MEAT'
ON CONFLICT (category_id, name) DO NOTHING;

INSERT INTO menu_items (category_id, name)
SELECT id, 'Carne de sol'
FROM menu_categories
WHERE code = 'MEAT'
ON CONFLICT (category_id, name) DO NOTHING;

INSERT INTO menu_items (category_id, name)
SELECT id, 'Estrogonofe de frango'
FROM menu_categories
WHERE code = 'MEAT'
ON CONFLICT (category_id, name) DO NOTHING;

INSERT INTO menu_items (category_id, name)
SELECT id, 'Peixe frito'
FROM menu_categories
WHERE code = 'MEAT'
ON CONFLICT (category_id, name) DO NOTHING;

INSERT INTO menu_items (category_id, name)
SELECT id, 'Carne guisada'
FROM menu_categories
WHERE code = 'MEAT'
ON CONFLICT (category_id, name) DO NOTHING;

INSERT INTO menu_items (category_id, name)
SELECT id, 'Porco torrado'
FROM menu_categories
WHERE code = 'MEAT'
ON CONFLICT (category_id, name) DO NOTHING;

INSERT INTO menu_items (category_id, name)
SELECT id, 'Panelada'
FROM menu_categories
WHERE code = 'MEAT'
ON CONFLICT (category_id, name) DO NOTHING;