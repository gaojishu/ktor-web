-- ============================================================
-- V1.0.0 初始化：公共函数与触发器
-- ============================================================

-- 创建 updated_at 自动更新函数
CREATE OR REPLACE FUNCTION public.set_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- 创建触发器工厂函数
CREATE OR REPLACE FUNCTION public.ensure_updated_at_trigger(
    p_table_name TEXT,
    p_trigger_name TEXT
)
RETURNS VOID
LANGUAGE plpgsql
AS $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_trigger t
        WHERE t.tgname = p_trigger_name
          AND t.tgrelid = format('public.%I', p_table_name)::regclass
    ) THEN
        EXECUTE format(
            'CREATE TRIGGER %I
             BEFORE UPDATE ON public.%I
             FOR EACH ROW
             EXECUTE FUNCTION public.set_updated_at()',
            p_trigger_name,
            p_table_name
        );
    END IF;
END;
$$;
