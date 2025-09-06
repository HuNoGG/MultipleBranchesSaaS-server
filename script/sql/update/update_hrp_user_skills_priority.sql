-- 为 hrp_user_skills 表添加技能优先级字段
ALTER TABLE hrp_user_skills ADD COLUMN priority BIGINT(20) NULL DEFAULT NULL COMMENT '技能优先级 (数字越大, 优先级越高)' AFTER tenant_id;
