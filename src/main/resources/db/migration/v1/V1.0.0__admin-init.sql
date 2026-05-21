

CREATE TABLE IF NOT EXISTS "admin"."admin" ("id" UUID PRIMARY KEY DEFAULT uuidv7(),
"created_at" TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
"updated_at" TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
"deleted_at" TIMESTAMP(6),
"username" VARCHAR(55) NOT NULL,
"password" VARCHAR(100) NOT NULL,
"nickname" VARCHAR(55),
"email" VARCHAR(55),
"mobile" VARCHAR(20),
"permission_key" JSON,
"disabled_status" INT2 NOT NULL DEFAULT 0);

CREATE UNIQUE INDEX IF NOT EXISTS "uk_admin_admin_username" ON "admin"."admin" ("username");

INSERT INTO "admin"."admin" ("id",
"username",
"password")
VALUES ('019d5228-5235-768e-8f49-6189373b7191','admin123', '$2a$10$uMJY137kuoJWqa.yEBpU6OE5Yh1lYNtnJPOlewFpnE81rd7C3qlsm');

CREATE TABLE IF NOT EXISTS "admin"."permission" ("id" UUID PRIMARY KEY DEFAULT uuidv7(),
"parent_id" UUID,
"name" VARCHAR(55) NOT NULL,
"code" VARCHAR(100),
"key" VARCHAR(1000) NOT NULL DEFAULT '',
"type" INT2 NOT NULL DEFAULT 1,
"level" INT4 NOT NULL,
"path" VARCHAR(100),
"icon" VARCHAR(100),
"sort" INT4 NOT NULL DEFAULT 1000,
"remark" VARCHAR(100),
"created_at" TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
"updated_at" TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP);

CREATE INDEX IF NOT EXISTS "idx_admin_permission_parent_id" ON "admin"."permission" ("parent_id");

CREATE UNIQUE INDEX IF NOT EXISTS "uk_admin_permission_key" ON "admin"."permission" ("key");


CREATE TABLE IF NOT EXISTS "admin"."admin_permission" ("admin_id" UUID NOT NULL,
"permission_id" UUID NOT NULL);

CREATE TABLE IF NOT EXISTS "admin"."role" ("id" UUID PRIMARY KEY DEFAULT uuidv7(),
"name" VARCHAR(55) NOT NULL DEFAULT '',
"permission_key" JSON,
"remark" VARCHAR(100),
"created_at" TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
"updated_at" TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP);

CREATE TABLE IF NOT EXISTS "admin"."files" ("id" UUID PRIMARY KEY DEFAULT uuidv7(),
"category_id" UUID,
"name" VARCHAR(100) NOT NULL DEFAULT '',
"key" VARCHAR(100) NOT NULL DEFAULT '',
"hash" VARCHAR(100) NOT NULL DEFAULT '',
"type" VARCHAR(50) NOT NULL DEFAULT '',
"platform" VARCHAR(10) NOT NULL DEFAULT '',
"visibility" VARCHAR(10) NOT NULL DEFAULT '',
"mime_type" VARCHAR(100),
"size" INT8 NOT NULL DEFAULT 0,
"remark" VARCHAR(100),
"created_at" TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
"updated_at" TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP);

CREATE INDEX IF NOT EXISTS "idx_admin_files_key" ON "admin"."files" ("key");

CREATE UNIQUE INDEX IF NOT EXISTS "idx_admin_files_hash" ON "admin"."files" ("hash");

CREATE TABLE IF NOT EXISTS "admin"."files_category" ("id" UUID PRIMARY KEY DEFAULT uuidv7(),
"name" VARCHAR(55) NOT NULL DEFAULT '',
"remark" VARCHAR(100),
"created_at" TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
"updated_at" TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP);

CREATE TABLE IF NOT EXISTS "admin"."notice" ("id" UUID PRIMARY KEY DEFAULT uuidv7(),
"admin_id" UUID,
"title" VARCHAR(100) NOT NULL,
"content" VARCHAR(200) NOT NULL,
"attachments" JSON,
"created_at" TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
"updated_at" TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP);

CREATE INDEX IF NOT EXISTS "idx_admin_notice_admin_id" ON "admin"."notice" ("admin_id");

CREATE TABLE IF NOT EXISTS "admin"."op_log" ("id" UUID PRIMARY KEY DEFAULT uuidv7(),
"admin_id" UUID,
"ip" VARCHAR(100) NOT NULL,
"method" VARCHAR(22) NOT NULL,
"uri" VARCHAR(100),
"params" TEXT, "query_params" TEXT, "duration" INT8 NOT NULL,
"remark" VARCHAR(100),
"created_at" TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
"updated_at" TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP);

CREATE INDEX IF NOT EXISTS "idx_admin_op_log_admin_id" ON "admin"."op_log" ("admin_id");

CREATE INDEX IF NOT EXISTS "idx_admin_op_log_created_at" ON "admin"."op_log" ("created_at");



SELECT admin.ensure_updated_at_trigger('admin', 'trg_admin_set_updated_at');
SELECT admin.ensure_updated_at_trigger('permission', 'trg_permission_set_updated_at');
SELECT admin.ensure_updated_at_trigger('role', 'trg_role_set_updated_at');
SELECT admin.ensure_updated_at_trigger('files', 'trg_files_set_updated_at');
SELECT admin.ensure_updated_at_trigger('files_category', 'trg_files_category_set_updated_at');
SELECT admin.ensure_updated_at_trigger('notice', 'trg_notice_set_updated_at');
SELECT admin.ensure_updated_at_trigger('op_log', 'trg_op_log_set_updated_at');
