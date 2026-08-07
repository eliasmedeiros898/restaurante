-- ============================================================
-- CARDÁPIOS PADRÃO POR DIA DA SEMANA
-- ============================================================

INSERT INTO menu_templates (name, day_of_week)
VALUES
    ('Cardápio de segunda-feira', 'MONDAY'),
    ('Cardápio de terça-feira', 'TUESDAY'),
    ('Cardápio de quarta-feira', 'WEDNESDAY'),
    ('Cardápio de quinta-feira', 'THURSDAY'),
    ('Cardápio de sexta-feira', 'FRIDAY'),
    ('Cardápio de sábado', 'SATURDAY')
ON CONFLICT (day_of_week) DO NOTHING;


-- ============================================================
-- SEGUNDA-FEIRA
-- ============================================================

INSERT INTO menu_template_items (
    menu_template_id,
    menu_item_id,
    display_order
)
SELECT
    template.id,
    item.id,
    ROW_NUMBER() OVER (ORDER BY category.display_order, item.name)
FROM menu_templates template
         JOIN menu_items item
              ON item.name IN (
                               'Feijão na farofa',
                               'Feijão no caldo',
                               'Arroz refogado',
                               'Arroz de leite',
                               'Macarrão',
                               'Verdura crua',
                               'Verdura cozida',
                               'Frango assado',
                               'Frango ao molho',
                               'Bife de carne',
                               'Linguiça',
                               'Fígado'
                  )
         JOIN menu_categories category
              ON category.id = item.category_id
WHERE template.day_of_week = 'MONDAY'
ON CONFLICT (menu_template_id, menu_item_id) DO NOTHING;


-- ============================================================
-- TERÇA-FEIRA
-- ============================================================

INSERT INTO menu_template_items (
    menu_template_id,
    menu_item_id,
    display_order
)
SELECT
    template.id,
    item.id,
    ROW_NUMBER() OVER (ORDER BY category.display_order, item.name)
FROM menu_templates template
         JOIN menu_items item
              ON item.name IN (
                               'Feijão na farofa',
                               'Feijão no caldo',
                               'Arroz refogado',
                               'Arroz de leite',
                               'Macarrão',
                               'Verdura crua',
                               'Verdura cozida',
                               'Frango assado',
                               'Frango ao molho',
                               'Carne de sol',
                               'Linguiça',
                               'Fígado'
                  )
         JOIN menu_categories category
              ON category.id = item.category_id
WHERE template.day_of_week = 'TUESDAY'
ON CONFLICT (menu_template_id, menu_item_id) DO NOTHING;


-- ============================================================
-- QUARTA-FEIRA
-- ============================================================

INSERT INTO menu_template_items (
    menu_template_id,
    menu_item_id,
    display_order
)
SELECT
    template.id,
    item.id,
    ROW_NUMBER() OVER (ORDER BY category.display_order, item.name)
FROM menu_templates template
         JOIN menu_items item
              ON item.name IN (
                               'Feijão na farofa',
                               'Feijão no caldo',
                               'Arroz refogado',
                               'Arroz na graxa',
                               'Macarrão',
                               'Verdura crua',
                               'Verdura cozida',
                               'Frango assado',
                               'Estrogonofe de frango',
                               'Peixe frito',
                               'Linguiça',
                               'Fígado'
                  )
         JOIN menu_categories category
              ON category.id = item.category_id
WHERE template.day_of_week = 'WEDNESDAY'
ON CONFLICT (menu_template_id, menu_item_id) DO NOTHING;


-- ============================================================
-- QUINTA-FEIRA
-- ============================================================

INSERT INTO menu_template_items (
    menu_template_id,
    menu_item_id,
    display_order
)
SELECT
    template.id,
    item.id,
    ROW_NUMBER() OVER (ORDER BY category.display_order, item.name)
FROM menu_templates template
         JOIN menu_items item
              ON item.name IN (
                               'Feijão na farofa',
                               'Feijão preto',
                               'Arroz refogado',
                               'Arroz de leite',
                               'Macarrão',
                               'Verdura crua',
                               'Verdura doce',
                               'Frango assado',
                               'Frango ao molho',
                               'Carne guisada',
                               'Porco torrado',
                               'Panelada',
                               'Linguiça',
                               'Fígado'
                  )
         JOIN menu_categories category
              ON category.id = item.category_id
WHERE template.day_of_week = 'THURSDAY'
ON CONFLICT (menu_template_id, menu_item_id) DO NOTHING;


-- ============================================================
-- SEXTA-FEIRA
-- ============================================================

INSERT INTO menu_template_items (
    menu_template_id,
    menu_item_id,
    display_order
)
SELECT
    template.id,
    item.id,
    ROW_NUMBER() OVER (ORDER BY category.display_order, item.name)
FROM menu_templates template
         JOIN menu_items item
              ON item.name IN (
                               'Feijão na farofa',
                               'Feijão no caldo',
                               'Arroz refogado',
                               'Arroz de leite',
                               'Macarrão',
                               'Verdura crua',
                               'Verdura refogada',
                               'Frango assado',
                               'Estrogonofe de frango',
                               'Peixe frito',
                               'Linguiça',
                               'Fígado'
                  )
         JOIN menu_categories category
              ON category.id = item.category_id
WHERE template.day_of_week = 'FRIDAY'
ON CONFLICT (menu_template_id, menu_item_id) DO NOTHING;


-- ============================================================
-- SÁBADO
-- ============================================================

INSERT INTO menu_template_items (
    menu_template_id,
    menu_item_id,
    display_order
)
SELECT
    template.id,
    item.id,
    ROW_NUMBER() OVER (ORDER BY category.display_order, item.name)
FROM menu_templates template
         JOIN menu_items item
              ON item.name IN (
                               'Arrubacão',
                               'Feijão na farofa',
                               'Arroz refogado',
                               'Macarrão',
                               'Verdura crua',
                               'Batata-doce',
                               'Carne guisada',
                               'Linguiça',
                               'Frango assado',
                               'Fígado',
                               'Frango ao molho'
                  )
         JOIN menu_categories category
              ON category.id = item.category_id
WHERE template.day_of_week = 'SATURDAY'
ON CONFLICT (menu_template_id, menu_item_id) DO NOTHING;