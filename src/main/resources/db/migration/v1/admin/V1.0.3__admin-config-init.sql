-- ============================================================
-- V1.0.3 初始化：应用配置表
-- ============================================================

-- 创建 config 表
CREATE TABLE "public"."config" (
    "key" VARCHAR(100) UNIQUE NOT NULL,  -- 配置唯一标识
    "value" JSON NOT NULL,               -- 存储具体参数
    "remark" VARCHAR(100)                -- 备注
);

-- 初始化默认配置
INSERT INTO "public"."config" ("key", "value", "remark")
VALUES ('agent.ba_zi', '{"systemPrompt":""}', NULL);

INSERT INTO "public"."config" ("key", "value", "remark")
VALUES ('agent.ming_pan', '{"systemPrompt":""}', NULL);
