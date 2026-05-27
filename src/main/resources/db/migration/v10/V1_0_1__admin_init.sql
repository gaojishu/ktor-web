-- ============================================================
-- V1.0.1 初始化：管理后台基础表
-- ============================================================

-- 创建 admin 表
CREATE TABLE IF NOT EXISTS "public"."admin" (
    "id" UUID PRIMARY KEY DEFAULT uuidv7(),              -- 主键 ID
    "created_at" TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP, -- 创建时间
    "updated_at" TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP, -- 更新时间
    "deleted_at" TIMESTAMP(6),                         -- 删除时间
    "username" VARCHAR(55) NOT NULL,                   -- 用户名
    "password" VARCHAR(255) NOT NULL,                  -- 密码
    "nickname" VARCHAR(55),                            -- 昵称
    "email" VARCHAR(55),                               -- 邮箱
    "mobile" VARCHAR(20),                              -- 手机号
    "permission_key" TEXT[],                             -- 权限键集合
    "status" INT4 NOT NULL DEFAULT 0          -- 禁用状态（0 正常，1 禁用）
);

CREATE UNIQUE INDEX IF NOT EXISTS "uk_admin_admin_username" ON "public"."admin" ("username");

-- 初始化默认管理员
INSERT INTO "public"."admin" ("id", "username", "password", "status")
VALUES ('019d5228-5235-768e-8f49-6189373b7191', 'admin123', '$argon2id$v=19$m=65536,t=3,p=4$m8dTWCwxVAU62yed888Z1c1WjJZkBRUcTONePXrahUu6kOmDHJM0QDXEepHA1vwTZf1xV4p51P5y4m8AkfL6oA$cF5OW3H3g1fDEhSspzewm2GU9MQUpMysgQj+KEjzcZY', 1);

-- 创建 permission 表
CREATE TABLE IF NOT EXISTS "public"."permission" (
    "id" UUID PRIMARY KEY DEFAULT uuidv7(),              -- 主键 ID
    "parent_id" UUID,                                    -- 父级权限 ID
    "name" VARCHAR(55) NOT NULL,                         -- 权限名称
    "code" VARCHAR(100),                                 -- 权限编码
    "key" VARCHAR(1000) NOT NULL DEFAULT '',           -- 权限路径键
    "type" INT4 NOT NULL DEFAULT 1,                      -- 权限类型（1 菜单，2 按钮）
    "level" INT4 NOT NULL,                               -- 权限层级
    "path" VARCHAR(100),                                 -- 路由路径
    "icon" VARCHAR(100),                                 -- 图标
    "sort" INT4 NOT NULL DEFAULT 1000,                   -- 排序
    "remark" VARCHAR(100),                               -- 备注
    "created_at" TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP, -- 创建时间
    "updated_at" TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP  -- 更新时间
);

CREATE INDEX IF NOT EXISTS "idx_admin_permission_parent_id" ON "public"."permission" ("parent_id");
CREATE UNIQUE INDEX IF NOT EXISTS "uk_admin_permission_key" ON "public"."permission" ("key");

-- 创建 admin_permission 表
CREATE TABLE IF NOT EXISTS "public"."admin_permission" (
    "admin_id" UUID NOT NULL,                            -- 管理员 ID
    "permission_id" UUID NOT NULL                        -- 权限 ID
);

-- 创建 role 表
CREATE TABLE IF NOT EXISTS "public"."role" (
    "id" UUID PRIMARY KEY DEFAULT uuidv7(),              -- 主键 ID
    "name" VARCHAR(55) NOT NULL DEFAULT '',              -- 角色名称
    "permission_key" TEXT[],                               -- 权限键集合
    "remark" VARCHAR(100),                               -- 备注
    "created_at" TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP, -- 创建时间
    "updated_at" TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP  -- 更新时间
);

-- 创建 files 表
CREATE TABLE IF NOT EXISTS "public"."files" (
    "id" UUID PRIMARY KEY DEFAULT uuidv7(),              -- 主键 ID
    "category_id" UUID,                                  -- 分类 ID
    "name" VARCHAR(100) NOT NULL DEFAULT '',             -- 文件名称
    "key" VARCHAR(100) NOT NULL DEFAULT '',              -- 存储键
    "hash" VARCHAR(100) NOT NULL DEFAULT '',             -- 文件哈希
    "type" VARCHAR(50) NOT NULL DEFAULT '',              -- 文件类型
    "platform" VARCHAR(10) NOT NULL DEFAULT '',          -- 存储平台
    "visibility" VARCHAR(10) NOT NULL DEFAULT '',         -- 可见性
    "mime_type" VARCHAR(100),                            -- MIME 类型
    "size" INT8 NOT NULL DEFAULT 0,                      -- 文件大小（字节）
    "remark" VARCHAR(100),                               -- 备注
    "created_at" TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP, -- 创建时间
    "updated_at" TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP  -- 更新时间
);

CREATE INDEX IF NOT EXISTS "idx_admin_files_key" ON "public"."files" ("key");
CREATE UNIQUE INDEX IF NOT EXISTS "idx_admin_files_hash" ON "public"."files" ("hash");

-- 创建 files_category 表
CREATE TABLE IF NOT EXISTS "public"."files_category" (
    "id" UUID PRIMARY KEY DEFAULT uuidv7(),              -- 主键 ID
    "name" VARCHAR(55) NOT NULL DEFAULT '',              -- 分类名称
    "remark" VARCHAR(100),                               -- 备注
    "created_at" TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP, -- 创建时间
    "updated_at" TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP  -- 更新时间
);

-- 创建 notice 表
CREATE TABLE IF NOT EXISTS "public"."notice" (
    "id" UUID PRIMARY KEY DEFAULT uuidv7(),              -- 主键 ID
    "admin_id" UUID,                                     -- 发布管理员 ID
    "title" VARCHAR(100) NOT NULL,                       -- 标题
    "content" VARCHAR(200) NOT NULL,                     -- 内容
    "attachments" JSON,                                  -- 附件列表
    "created_at" TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP, -- 创建时间
    "updated_at" TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP  -- 更新时间
);

CREATE INDEX IF NOT EXISTS "idx_admin_notice_admin_id" ON "public"."notice" ("admin_id");

-- 创建 op_log 表
CREATE TABLE IF NOT EXISTS "public"."op_log" (
    "id" UUID PRIMARY KEY DEFAULT uuidv7(),              -- 主键 ID
    "admin_id" UUID,                                     -- 操作管理员 ID
    "ip" VARCHAR(100) NOT NULL,                          -- 请求 IP
    "method" VARCHAR(22) NOT NULL,                       -- 请求方法
    "uri" VARCHAR(100),                                  -- 请求 URI
    "params" TEXT,                                       -- 请求体参数
    "query_params" TEXT,                                 -- 查询参数
    "duration" INT8 NOT NULL,                            -- 耗时（毫秒）
    "remark" VARCHAR(100),                               -- 备注
    "created_at" TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP, -- 创建时间
    "updated_at" TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP  -- 更新时间
);

CREATE INDEX IF NOT EXISTS "idx_admin_op_log_admin_id" ON "public"."op_log" ("admin_id");
CREATE INDEX IF NOT EXISTS "idx_admin_op_log_created_at" ON "public"."op_log" ("created_at");

-- 创建 updated_at 触发器
SELECT public.ensure_updated_at_trigger('admin', 'trg_admin_set_updated_at');
SELECT public.ensure_updated_at_trigger('permission', 'trg_permission_set_updated_at');
SELECT public.ensure_updated_at_trigger('role', 'trg_role_set_updated_at');
SELECT public.ensure_updated_at_trigger('files', 'trg_files_set_updated_at');
SELECT public.ensure_updated_at_trigger('files_category', 'trg_files_category_set_updated_at');
SELECT public.ensure_updated_at_trigger('notice', 'trg_notice_set_updated_at');
SELECT public.ensure_updated_at_trigger('op_log', 'trg_op_log_set_updated_at');
