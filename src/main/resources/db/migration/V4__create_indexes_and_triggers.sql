-- ============================================================
-- ÍNDICES E ATUALIZAÇÃO AUTOMÁTICA DE updated_at
-- ============================================================

CREATE INDEX idx_menu_items_category
    ON menu_items (category_id);

CREATE INDEX idx_menu_items_active
    ON menu_items (active);

CREATE INDEX idx_menu_template_items_template
    ON menu_template_items (menu_template_id);

CREATE INDEX idx_daily_menus_date_open
    ON daily_menus (menu_date, open);

CREATE INDEX idx_daily_menu_items_menu_available
    ON daily_menu_items (daily_menu_id, available);

CREATE INDEX idx_daily_menu_items_item
    ON daily_menu_items (menu_item_id);

CREATE INDEX idx_orders_status_created_at
    ON orders (status, created_at DESC);

CREATE INDEX idx_orders_channel_created_at
    ON orders (channel, created_at DESC);

CREATE INDEX idx_orders_customer_phone
    ON orders (customer_phone);

CREATE INDEX idx_orders_daily_menu
    ON orders (daily_menu_id);

CREATE INDEX idx_orders_created_by
    ON orders (created_by_user_id);

CREATE INDEX idx_order_items_order
    ON order_items (order_id);

CREATE INDEX idx_order_items_meal_plan
    ON order_items (meal_plan_id);

CREATE INDEX idx_order_item_selections_order_item
    ON order_item_selections (order_item_id);

CREATE INDEX idx_order_item_selections_menu_item
    ON order_item_selections (menu_item_id);


CREATE OR REPLACE FUNCTION update_updated_at_column()
    RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;


CREATE TRIGGER trg_users_updated_at
    BEFORE UPDATE ON users
    FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column();


CREATE TRIGGER trg_menu_categories_updated_at
    BEFORE UPDATE ON menu_categories
    FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column();


CREATE TRIGGER trg_menu_items_updated_at
    BEFORE UPDATE ON menu_items
    FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column();


CREATE TRIGGER trg_meal_plans_updated_at
    BEFORE UPDATE ON meal_plans
    FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column();


CREATE TRIGGER trg_menu_templates_updated_at
    BEFORE UPDATE ON menu_templates
    FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column();


CREATE TRIGGER trg_daily_menus_updated_at
    BEFORE UPDATE ON daily_menus
    FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column();


CREATE TRIGGER trg_daily_menu_items_updated_at
    BEFORE UPDATE ON daily_menu_items
    FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column();


CREATE TRIGGER trg_orders_updated_at
    BEFORE UPDATE ON orders
    FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column();


CREATE TRIGGER trg_order_items_updated_at
    BEFORE UPDATE ON order_items
    FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column();