CREATE TABLE "admin".config (
    key VARCHAR(100) UNIQUE NOT NULL, -- 配置唯一标识
    value JSON NOT NULL,            -- 存储具体参数
    remark VARCHAR(100)                  -- 备注
);

INSERT INTO "admin"."config" ("key", "value", "remark") VALUES ('agent.ba_zi', '{"systemPrompt":""}', NULL);
INSERT INTO "admin"."config" ("key", "value", "remark") VALUES ('agent.ming_pan', '{"systemPrompt":""}', NULL);
