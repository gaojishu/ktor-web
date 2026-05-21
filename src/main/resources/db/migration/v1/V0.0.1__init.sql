-- SCHEMA
CREATE SCHEMA IF NOT EXISTS "admin";

-- 创建函数
CREATE OR REPLACE FUNCTION admin.set_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- trigger 工厂
CREATE OR REPLACE FUNCTION admin.ensure_updated_at_trigger(
    p_table_name text,
    p_trigger_name text
)
RETURNS void
LANGUAGE plpgsql
AS $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_trigger t
        WHERE t.tgname = p_trigger_name
          AND t.tgrelid = format('admin.%I', p_table_name)::regclass
    ) THEN
        EXECUTE format(
            'CREATE TRIGGER %I
             BEFORE UPDATE ON admin.%I
             FOR EACH ROW
             EXECUTE FUNCTION admin.set_updated_at()',
            p_trigger_name,
            p_table_name
        );
END IF;
END;
$$;