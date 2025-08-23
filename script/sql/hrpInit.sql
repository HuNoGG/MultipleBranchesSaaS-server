drop table if exists hrp_attendance_exceptions;

drop table if exists hrp_attendance_records;

drop table if exists hrp_cross_store_cost_allocation;

drop table if exists hrp_inventory_transfers;

drop table if exists hrp_leave_applications;

drop table if exists hrp_leave_requests;

drop table if exists hrp_payroll_records;

drop table if exists hrp_payroll_settings;

drop table if exists hrp_schedule_modifications;

drop table if exists hrp_schedule_requirements;

drop table if exists hrp_schedules;

drop table if exists hrp_shift_breaks;

drop table if exists hrp_shifts;

drop table if exists hrp_store_events;

drop table if exists hrp_store_support_relations;

drop table if exists hrp_user_availability;

drop table if exists hrp_user_profile;

drop table if exists hrp_user_skills;

drop table if exists hrp_skills;

drop table if exists hrp_user_store_access;

drop table if exists hrp_stores;

CREATE TABLE `hrp_stores`
(
    `id`             BIGINT       NOT NULL AUTO_INCREMENT COMMENT '分店唯一ID',
    `tenant_id`      VARCHAR(20)  DEFAULT '000000' COMMENT '租户编号',
    `name`           VARCHAR(100) NOT NULL COMMENT '分店名称',
    `address`        VARCHAR(255) DEFAULT NULL COMMENT '分店地址',
    `cross_day_rule` VARCHAR(20)  DEFAULT 'by_shift_start' COMMENT '跨日工时归属规则(字典: by_shift_start, by_calendar_day)',
    `create_dept`    BIGINT       DEFAULT NULL COMMENT '创建部门',
    `create_by`      BIGINT       DEFAULT NULL COMMENT '创建者',
    `create_time`    DATETIME     DEFAULT NULL COMMENT '创建时间',
    `update_by`      BIGINT       DEFAULT NULL COMMENT '更新者',
    `update_time`    DATETIME     DEFAULT NULL COMMENT '更新时间',
    `status`         CHAR(1)      DEFAULT '0' COMMENT '状态(0正常1停用)',
    `remark`         VARCHAR(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='分店表';

CREATE TABLE `hrp_store_events`
(
    `id`          BIGINT NOT NULL AUTO_INCREMENT COMMENT '事件ID',
    `store_id`    BIGINT       DEFAULT NULL COMMENT '分店 ID (关联分店表)',
    `event_date`  DATE   NOT NULL COMMENT '事件日期',
    `event_type`  VARCHAR(20)  DEFAULT NULL COMMENT '事件类型(字典: 全天放假, 上午放假, 下午放假)',
    `description` VARCHAR(255) DEFAULT NULL COMMENT '事件描述(如:法定节假日放假、设备检修)',
    `create_by`   BIGINT       DEFAULT NULL COMMENT '创建者(关联系统用户表 sys_user.user_id)',
    `create_time` DATETIME     DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `fk_events_store_id` (`store_id`),
    CONSTRAINT `fk_events_store_id` FOREIGN KEY (`store_id`) REFERENCES `hrp_stores` (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='分店特殊事件表';

CREATE TABLE `hrp_store_support_relations`
(
    `requesting_store_id` BIGINT NOT NULL COMMENT '请求支援的分店ID',
    `supporting_store_id` BIGINT NOT NULL COMMENT '可提供支援的分店 ID',
    PRIMARY KEY (`requesting_store_id`, `supporting_store_id`),
    KEY `fk_support_supporting_id` (`supporting_store_id`),
    CONSTRAINT `fk_support_requesting_id` FOREIGN KEY (`requesting_store_id`) REFERENCES `hrp_stores` (`id`),
    CONSTRAINT `fk_support_supporting_id` FOREIGN KEY (`supporting_store_id`) REFERENCES `hrp_stores` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='分店支援关系表';



CREATE TABLE `hrp_user_profile`
(
    `user_id`        BIGINT NOT NULL COMMENT '员工ID(关联系统用户表)',
    `tenant_id`      VARCHAR(20)  DEFAULT '000000' COMMENT '租户编号',
    `employee_type`  VARCHAR(10)  DEFAULT NULL COMMENT '员工分类(字典: 干部, 全职, 计时)',
    `main_store_id`  BIGINT       DEFAULT NULL COMMENT '主要归属分店 ID',
    `priority_score` INT          DEFAULT '0' COMMENT '分配工作优先分数',
    `create_dept`    BIGINT       DEFAULT NULL COMMENT '创建部门',
    `create_by`      BIGINT       DEFAULT NULL COMMENT '创建者',
    `create_time`    DATETIME     DEFAULT NULL COMMENT '创建时间',
    `update_by`      BIGINT       DEFAULT NULL COMMENT '更新者',
    `update_time`    DATETIME     DEFAULT NULL COMMENT '更新时间',
    `status`         CHAR(1)      DEFAULT '0' COMMENT '状态(0在职1离职)',
    `remark`         VARCHAR(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`user_id`),
    KEY `fk_profile_store_id` (`main_store_id`),
    CONSTRAINT `fk_profile_store_id` FOREIGN KEY (`main_store_id`) REFERENCES `hrp_stores` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='员工档案扩展表';

CREATE TABLE `hrp_user_store_access`
(
    `user_id`  BIGINT NOT NULL COMMENT '员工 ID',
    `store_id` BIGINT NOT NULL COMMENT '授权支援的分店 ID',
    PRIMARY KEY (`user_id`, `store_id`),
    KEY `fk_access_store_id` (`store_id`),
    CONSTRAINT `fk_access_store_id` FOREIGN KEY (`store_id`) REFERENCES `hrp_stores` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='员工跨店权限表';

CREATE TABLE `hrp_user_availability`
(
    `id`          BIGINT NOT NULL AUTO_INCREMENT COMMENT '唯一ID',
    `user_id`     BIGINT DEFAULT NULL COMMENT '员工 ID',
    `day_of_week` INT    DEFAULT NULL COMMENT '星期几 (1=周一, 2=周二, ..., 7=周日)',
    `start_time`  TIME   DEFAULT NULL COMMENT '可上班的开始时间',
    `end_time`    TIME   DEFAULT NULL COMMENT '可上班的结束时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='员工可上班时段表';

CREATE TABLE `hrp_skills`
(
    `id`          BIGINT NOT NULL AUTO_INCREMENT COMMENT '技能唯一ID',
    `name`        VARCHAR(50)  DEFAULT NULL COMMENT '技能名称(如: 出锅,带位)',
    `create_dept` BIGINT       DEFAULT NULL COMMENT '创建部门',
    `create_by`   BIGINT       DEFAULT NULL COMMENT '创建者',
    `create_time` DATETIME     DEFAULT NULL COMMENT '创建时间',
    `update_by`   BIGINT       DEFAULT NULL COMMENT '更新者',
    `update_time` DATETIME     DEFAULT NULL COMMENT '更新时间',
    `status`      CHAR(1)      DEFAULT '0' COMMENT '状态(0正常1停用)',
    `remark`      VARCHAR(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_skill_name` (`name`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='技能岗位表';

CREATE TABLE `hrp_user_skills`
(
    `user_id`  BIGINT NOT NULL COMMENT '员工 ID',
    `skill_id` BIGINT NOT NULL COMMENT '技能 ID',
    PRIMARY KEY (`user_id`, `skill_id`),
    KEY `fk_userskills_skill_id` (`skill_id`),
    CONSTRAINT `fk_userskills_skill_id` FOREIGN KEY (`skill_id`) REFERENCES `hrp_skills` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='员工技能关联表';

CREATE TABLE `hrp_shifts`
(
    `id`           BIGINT      NOT NULL AUTO_INCREMENT COMMENT '班别唯一ID',
    `store_id`     BIGINT       DEFAULT NULL COMMENT '所属分店 ID',
    `name`         VARCHAR(50) NOT NULL COMMENT '班别名称(如:早班)',
    `code`         VARCHAR(10)  DEFAULT NULL COMMENT '班别代码(如:早)',
    `start_time`   TIME        NOT NULL COMMENT '开始时间',
    `end_time`     TIME        NOT NULL COMMENT '结束时间',
    `is_cross_day` BOOLEAN      DEFAULT FALSE COMMENT '是否跨日(TRUE =是,FALSE =否)',
    `color_code`   VARCHAR(7)   DEFAULT NULL COMMENT '班表显示颜色(如:#FF5733)',
    `create_dept`  BIGINT       DEFAULT NULL COMMENT '创建部门',
    `create_by`    BIGINT       DEFAULT NULL COMMENT '创建者',
    `create_time`  DATETIME     DEFAULT NULL COMMENT '创建时间',
    `update_by`    BIGINT       DEFAULT NULL COMMENT '更新者',
    `update_time`  DATETIME     DEFAULT NULL COMMENT '更新时间',
    `status`       CHAR(1)      DEFAULT '0' COMMENT '状态(0正常1停用)',
    `remark`       VARCHAR(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`id`),
    KEY `fk_shifts_store_id` (`store_id`),
    CONSTRAINT `fk_shifts_store_id` FOREIGN KEY (`store_id`) REFERENCES `hrp_stores` (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='班别设定表';

CREATE TABLE `hrp_shift_breaks`
(
    `id`               BIGINT NOT NULL AUTO_INCREMENT COMMENT '唯一ID',
    `shift_id`         BIGINT DEFAULT NULL COMMENT '班别 ID',
    `break_start_time` TIME   NOT NULL COMMENT '休息开始时间',
    `break_end_time`   TIME   NOT NULL COMMENT '休息结束时间',
    PRIMARY KEY (`id`),
    KEY `fk_breaks_shift_id` (`shift_id`),
    CONSTRAINT `fk_breaks_shift_id` FOREIGN KEY (`shift_id`) REFERENCES `hrp_shifts` (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='班别休息时段表';



CREATE TABLE `hrp_schedule_requirements`
(
    `id`             BIGINT NOT NULL AUTO_INCREMENT COMMENT '唯一ID',
    `store_id`       BIGINT       DEFAULT NULL COMMENT '分店 ID',
    `day_type`       VARCHAR(20)  DEFAULT NULL COMMENT '日期类型(字典: 平日, 假日, 特殊节日)',
    `shift_id`       BIGINT       DEFAULT NULL COMMENT '班别 ID',
    `skill_id`       BIGINT       DEFAULT NULL COMMENT '岗位(技能) ID',
    `required_count` INT    NOT NULL COMMENT '需求人数',
    `create_dept`    BIGINT       DEFAULT NULL COMMENT '创建部门',
    `create_by`      BIGINT       DEFAULT NULL COMMENT '创建者',
    `create_time`    DATETIME     DEFAULT NULL COMMENT '创建时间',
    `update_by`      BIGINT       DEFAULT NULL COMMENT '更新者',
    `update_time`    DATETIME     DEFAULT NULL COMMENT '更新时间',
    `status`         CHAR(1)      DEFAULT '0' COMMENT '状态(0正常1停用)',
    `remark`         VARCHAR(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`id`),
    KEY `fk_reqs_store_id` (`store_id`),
    KEY `fk_reqs_shift_id` (`shift_id`),
    KEY `fk_reqs_skill_id` (`skill_id`),
    CONSTRAINT `fk_reqs_shift_id` FOREIGN KEY (`shift_id`) REFERENCES `hrp_shifts` (`id`),
    CONSTRAINT `fk_reqs_skill_id` FOREIGN KEY (`skill_id`) REFERENCES `hrp_skills` (`id`),
    CONSTRAINT `fk_reqs_store_id` FOREIGN KEY (`store_id`) REFERENCES `hrp_stores` (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='每日人力需求表';

CREATE TABLE `hrp_schedules`
(
    `id`            BIGINT NOT NULL AUTO_INCREMENT COMMENT '排班记录唯一ID',
    `user_id`       BIGINT DEFAULT NULL COMMENT '员工 ID',
    `store_id`      BIGINT DEFAULT NULL COMMENT '上班分店 ID',
    `shift_id`      BIGINT DEFAULT NULL COMMENT '班别 ID',
    `skill_id`      BIGINT DEFAULT NULL COMMENT '担任岗位 ID',
    `schedule_date` DATE   NOT NULL COMMENT '排班日期',
    `version`       INT    DEFAULT NULL COMMENT '版本号,用于管理与回溯',
    PRIMARY KEY (`id`),
    KEY `fk_schedules_store_id` (`store_id`),
    KEY `fk_schedules_shift_id` (`shift_id`),
    KEY `fk_schedules_skill_id` (`skill_id`),
    CONSTRAINT `fk_schedules_shift_id` FOREIGN KEY (`shift_id`) REFERENCES `hrp_shifts` (`id`),
    CONSTRAINT `fk_schedules_skill_id` FOREIGN KEY (`skill_id`) REFERENCES `hrp_skills` (`id`),
    CONSTRAINT `fk_schedules_store_id` FOREIGN KEY (`store_id`) REFERENCES `hrp_stores` (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='排班表';

CREATE TABLE `hrp_schedule_modifications`
(
    `id`               BIGINT NOT NULL AUTO_INCREMENT COMMENT '记录 ID',
    `schedule_id`      BIGINT       DEFAULT NULL COMMENT '关联的排班记录ID(原排班表主键)',
    `original_user_id` BIGINT       DEFAULT NULL COMMENT '原员工ID(修改前的排班员工)',
    `new_user_id`      BIGINT       DEFAULT NULL COMMENT '新员工ID(修改后的排班员工,change_type为\'新增临时\')',
    `change_type`      VARCHAR(20)  DEFAULT NULL COMMENT '修改类型(字典: 调班, 替班, 新增临时)',
    `changed_by`       BIGINT       DEFAULT NULL COMMENT '修改人ID(操作修改的用户)',
    `change_time`      DATETIME     DEFAULT NULL COMMENT '修改时间',
    `remark`           VARCHAR(500) DEFAULT NULL COMMENT '备注(如:调班原因、替班说明等)',
    PRIMARY KEY (`id`),
    KEY `fk_mods_schedule_id` (`schedule_id`),
    CONSTRAINT `fk_mods_schedule_id` FOREIGN KEY (`schedule_id`) REFERENCES `hrp_schedules` (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='排班修改记录表';

CREATE TABLE `hrp_leave_applications`
(
    `id`              BIGINT   NOT NULL AUTO_INCREMENT COMMENT '申请 ID',
    `user_id`         BIGINT        DEFAULT NULL COMMENT '申请人ID(关联系统用户表)',
    `leave_type`      VARCHAR(50)   DEFAULT NULL COMMENT '请假类型(如:事假,病假,年假,婚假)',
    `start_time`      DATETIME NOT NULL COMMENT '请假开始时间(精确到时分)',
    `end_time`        DATETIME NOT NULL COMMENT '请假结束时间(精确到时分)',
    `leave_days`      DECIMAL(5, 1) DEFAULT '0.0' COMMENT '请假天数(自动计算,支持0.5天粒度)',
    `reason`          VARCHAR(500)  DEFAULT NULL COMMENT '请假事由',
    `attachment_url`  VARCHAR(255)  DEFAULT NULL COMMENT '附件链接(如病假证明、休假凭证)',
    `approval_status` VARCHAR(10)   DEFAULT NULL COMMENT '审批状态(字典: 待审批, 已批准, 已驳回)',
    `approved_by`     BIGINT        DEFAULT NULL COMMENT '审批人ID(关联系统用户表,审批后生效)',
    `create_dept`     BIGINT        DEFAULT NULL COMMENT '创建部门(申请人所属部门)',
    `create_by`       BIGINT        DEFAULT NULL COMMENT '创建者(通常为申请人本人)',
    `create_time`     DATETIME      DEFAULT NULL COMMENT '申请创建时间',
    `update_by`       BIGINT        DEFAULT NULL COMMENT '更新者(如审批人或申请人修改时)',
    `update_time`     DATETIME      DEFAULT NULL COMMENT '记录更新时间',
    `status`          CHAR(1)       DEFAULT '0' COMMENT '数据状态(0正常1停用/作废)',
    `remark`          VARCHAR(500)  DEFAULT NULL COMMENT '备注(如审批意见、特殊说明)',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='正式请假申请表';

CREATE TABLE `hrp_leave_requests`
(
    `id`              BIGINT NOT NULL AUTO_INCREMENT COMMENT '唯一ID',
    `user_id`         BIGINT       DEFAULT NULL COMMENT '员工 ID',
    `leave_date`      DATE   NOT NULL COMMENT '期望休假日期',
    `approval_status` VARCHAR(10)  DEFAULT NULL COMMENT '状态(字典: 已提交, 已锁定, 已取消)',
    `create_dept`     BIGINT       DEFAULT NULL COMMENT '创建部门',
    `create_by`       BIGINT       DEFAULT NULL COMMENT '创建者',
    `create_time`     DATETIME     DEFAULT NULL COMMENT '创建时间',
    `update_by`       BIGINT       DEFAULT NULL COMMENT '更新者',
    `update_time`     DATETIME     DEFAULT NULL COMMENT '更新时间',
    `status`          CHAR(1)      DEFAULT '0' COMMENT '状态(0正常1停用)',
    `remark`          VARCHAR(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='期望休假/排休记录表';

CREATE TABLE `hrp_attendance_records`
(
    `id`             BIGINT    NOT NULL AUTO_INCREMENT COMMENT '记录唯一ID',
    `user_id`        BIGINT         DEFAULT NULL COMMENT '员工 ID',
    `schedule_id`    BIGINT         DEFAULT NULL COMMENT '关联的排班记录ID',
    `clock_in_time`  TIMESTAMP NULL DEFAULT NULL COMMENT '上班打卡时间',
    `clock_out_time` TIMESTAMP NULL DEFAULT NULL COMMENT '下班打卡时间',
    `record_date`    DATE           DEFAULT NULL COMMENT '记录日期',
    PRIMARY KEY (`id`),
    KEY `fk_records_schedule_id` (`schedule_id`),
    CONSTRAINT `fk_records_schedule_id` FOREIGN KEY (`schedule_id`) REFERENCES `hrp_schedules` (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='打卡记录表';



CREATE TABLE `hrp_attendance_exceptions`
(
    `id`                  BIGINT    NOT NULL AUTO_INCREMENT COMMENT '异常记录 ID',
    `user_id`             BIGINT         DEFAULT NULL COMMENT '员工 ID',
    `exception_date`      DATE      NOT NULL COMMENT '异常日期',
    `exception_type`      VARCHAR(20)    DEFAULT NULL COMMENT '异常类型(字典: 忘记打卡, 迟到未请假, 早退未请假, 缺勤)',
    `original_clock_in`   TIMESTAMP NULL DEFAULT NULL COMMENT '原始打卡(上班)',
    `original_clock_out`  TIMESTAMP NULL DEFAULT NULL COMMENT '原始打卡(下班)',
    `corrected_clock_in`  TIMESTAMP NULL DEFAULT NULL COMMENT '修正后打卡(上班)',
    `corrected_clock_out` TIMESTAMP NULL DEFAULT NULL COMMENT '修正后打卡(下班)',
    `approval_status`     VARCHAR(10)    DEFAULT NULL COMMENT '状态(字典: 待处理, 已修正)',
    `approved_by`         BIGINT         DEFAULT NULL COMMENT '审核人 ID',
    `create_dept`         BIGINT         DEFAULT NULL COMMENT '创建部门',
    `create_by`           BIGINT         DEFAULT NULL COMMENT '创建者',
    `create_time`         DATETIME       DEFAULT NULL COMMENT '创建时间',
    `update_by`           BIGINT         DEFAULT NULL COMMENT '更新者',
    `update_time`         DATETIME       DEFAULT NULL COMMENT '更新时间',
    `status`              CHAR(1)        DEFAULT '0' COMMENT '状态(0正常1停用)',
    `remark`              VARCHAR(500)   DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='考勤异常表';

CREATE TABLE `hrp_inventory_transfers`
(
    `id`                    BIGINT    NOT NULL AUTO_INCREMENT COMMENT '调货单 ID',
    `request_store_id`      BIGINT         DEFAULT NULL COMMENT '申请方分店ID (A分店)',
    `provider_store_id`     BIGINT         DEFAULT NULL COMMENT '提供方分店ID(B分店)',
    `request_user_id`       BIGINT         DEFAULT NULL COMMENT '申请人ID',
    `transfer_details`      JSON COMMENT '调货内容(品项,数量)示例:[{\"item\":\"商品A\",\"qty\":10}]',
    `approval_status`       VARCHAR(10)    DEFAULT NULL COMMENT '状态(字典: 待审核, 待确认, 已完成)',
    `request_confirmed_at`  TIMESTAMP NULL DEFAULT NULL COMMENT '申请方确认时间',
    `provider_confirmed_at` TIMESTAMP NULL DEFAULT NULL COMMENT '提供方确认时间',
    `responsible_user_id`   BIGINT         DEFAULT NULL COMMENT '该班次责任人ID',
    `create_dept`           BIGINT         DEFAULT NULL COMMENT '创建部门',
    `create_by`             BIGINT         DEFAULT NULL COMMENT '创建者',
    `create_time`           DATETIME       DEFAULT NULL COMMENT '创建时间',
    `update_by`             BIGINT         DEFAULT NULL COMMENT '更新者',
    `update_time`           DATETIME       DEFAULT NULL COMMENT '更新时间',
    `status`                CHAR(1)        DEFAULT '0' COMMENT '状态(0正常1停用)',
    `remark`                VARCHAR(500)   DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`id`),
    KEY `fk_transfers_req_store_id` (`request_store_id`),
    KEY `fk_transfers_prov_store_id` (`provider_store_id`),
    CONSTRAINT `fk_transfers_prov_store_id` FOREIGN KEY (`provider_store_id`) REFERENCES `hrp_stores` (`id`),
    CONSTRAINT `fk_transfers_req_store_id` FOREIGN KEY (`request_store_id`) REFERENCES `hrp_stores` (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='调货记录表';

CREATE TABLE `hrp_payroll_settings`
(
    `id`             BIGINT NOT NULL AUTO_INCREMENT COMMENT '规则 ID',
    `user_id`        BIGINT         DEFAULT NULL COMMENT '员工 ID',
    `base_salary`    DECIMAL(10, 2) DEFAULT NULL COMMENT '月薪(全职适用)',
    `hourly_rate`    DECIMAL(10, 2) DEFAULT NULL COMMENT '时薪(计时适用)',
    `effective_date` DATE           DEFAULT NULL COMMENT '生效日期',
    `create_dept`    BIGINT         DEFAULT NULL COMMENT '创建部门',
    `create_by`      BIGINT         DEFAULT NULL COMMENT '创建者',
    `create_time`    DATETIME       DEFAULT NULL COMMENT '创建时间',
    `update_by`      BIGINT         DEFAULT NULL COMMENT '更新者',
    `update_time`    DATETIME       DEFAULT NULL COMMENT '更新时间',
    `status`         CHAR(1)        DEFAULT '0' COMMENT '状态(0正常1停用)',
    `remark`         VARCHAR(500)   DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='薪资规则表';

CREATE TABLE `hrp_payroll_records`
(
    `id`               BIGINT NOT NULL AUTO_INCREMENT COMMENT '薪资单 ID',
    `user_id`          BIGINT         DEFAULT NULL COMMENT '员工 ID',
    `pay_period_start` DATE           DEFAULT NULL COMMENT '薪资周期开始',
    `pay_period_end`   DATE           DEFAULT NULL COMMENT '薪资周期结束',
    `total_hours`      DECIMAL(10, 2) DEFAULT NULL COMMENT '总工时',
    `base_pay`         DECIMAL(10, 2) DEFAULT NULL COMMENT '基本薪资',
    `overtime_pay`     DECIMAL(10, 2) DEFAULT NULL COMMENT '加班费',
    `deductions`       DECIMAL(10, 2) DEFAULT NULL COMMENT '扣款',
    `final_salary`     DECIMAL(10, 2) DEFAULT NULL COMMENT '最终薪资',
    `is_finalized`     BOOLEAN        DEFAULT NULL COMMENT '是否已确认(TRUE =是,FALSE = 否)',
    `create_dept`      BIGINT         DEFAULT NULL COMMENT '创建部门',
    `create_by`        BIGINT         DEFAULT NULL COMMENT '创建者',
    `create_time`      DATETIME       DEFAULT NULL COMMENT '创建时间',
    `update_by`        BIGINT         DEFAULT NULL COMMENT '更新者',
    `update_time`      DATETIME       DEFAULT NULL COMMENT '更新时间',
    `status`           CHAR(1)        DEFAULT '0' COMMENT '状态(0正常1停用)',
    `remark`           VARCHAR(500)   DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='薪资单记录表';



CREATE TABLE `hrp_cross_store_cost_allocation`
(
    `id`               BIGINT NOT NULL AUTO_INCREMENT COMMENT '唯一ID',
    `user_id`          BIGINT       DEFAULT NULL COMMENT '员工 ID',
    `allocation_type`  VARCHAR(10)  DEFAULT NULL COMMENT '分摊方式(字典: 依时数, 依比例)',
    `allocation_rules` JSON COMMENT '分摊规则 示例:{\"storeA\": 50, \"storeB\":50} (比例分摊)',
    `create_dept`      BIGINT       DEFAULT NULL COMMENT '创建部门',
    `create_by`        BIGINT       DEFAULT NULL COMMENT '创建者',
    `create_time`      DATETIME     DEFAULT NULL COMMENT '创建时间',
    `update_by`        BIGINT       DEFAULT NULL COMMENT '更新者',
    `update_time`      DATETIME     DEFAULT NULL COMMENT '更新时间',
    `status`           CHAR(1)      DEFAULT '0' COMMENT '状态(0正常1停用)',
    `remark`           VARCHAR(500) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='跨店成本分摊设定表';


-- 字典
INSERT INTO `sys_dict_type` (`dict_id`, `dict_name`, `dict_type`, `create_by`, `create_time`, `remark`)
VALUES (101, '跨日工时归属规则', 'hrp_cross_day_rule', 1, NOW(), '分店表的跨日工时归属规则'),
       (102, '分店事件类型', 'hrp_store_event_type', 1, NOW(), '分店特殊事件的类型'),
       (103, '员工分类', 'hrp_employee_type', 1, NOW(), '员工档案的员工分类'),
       (104, '人力需求日期类型', 'hrp_schedule_day_type', 1, NOW(), '每日人力需求的日期类型'),
       (105, '排班修改类型', 'hrp_schedule_change_type', 1, NOW(), '排班修改记录的变更类型'),
       (106, '通用审批状态', 'common_approval_status', 1, NOW(), '通用的审批状态(待审批/已批准/已驳回)'),
       (107, '排休申请状态', 'hrp_leave_request_status', 1, NOW(), '期望休假/排休记录的状态'),
       (108, '考勤异常类型', 'hrp_attendance_exception_type', 1, NOW(), '考勤异常记录的类型'),
       (109, '考勤异常处理状态', 'hrp_attendance_approval_status', 1, NOW(), '考勤异常的处理状态'),
       (110, '调货单状态', 'hrp_inventory_transfer_status', 1, NOW(), '库存调货记录的状态'),
       (111, '成本分摊方式', 'hrp_cost_allocation_type', 1, NOW(), '跨店成本分摊的设定方式');


INSERT INTO `sys_dict_data` (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `is_default`,
                             `create_by`, `create_time`)
VALUES
-- 跨日工时归属规则 (hrp_cross_day_rule)
(1001, 0, '按班次开始日', 'by_shift_start', 'hrp_cross_day_rule', 'Y', 1, NOW()),
(1002, 1, '按日历天拆分', 'by_calendar_day', 'hrp_cross_day_rule', 'N', 1, NOW()),

-- 分店事件类型 (hrp_store_event_type)
(1003, 0, '全天放假', '全天放假', 'hrp_store_event_type', 'Y', 1, NOW()),
(1004, 1, '上午放假', '上午放假', 'hrp_store_event_type', 'N', 1, NOW()),
(1005, 2, '下午放假', '下午放假', 'hrp_store_event_type', 'N', 1, NOW()),

-- 员工分类 (hrp_employee_type)
(1006, 0, '干部', '干部', 'hrp_employee_type', 'N', 1, NOW()),
(1007, 1, '全职', '全职', 'hrp_employee_type', 'Y', 1, NOW()),
(1008, 2, '计时', '计时', 'hrp_employee_type', 'N', 1, NOW()),

-- 人力需求日期类型 (hrp_schedule_day_type)
(1009, 0, '平日', '平日', 'hrp_schedule_day_type', 'Y', 1, NOW()),
(1010, 1, '假日', '假日', 'hrp_schedule_day_type', 'N', 1, NOW()),
(1011, 2, '特殊节日', '特殊节日', 'hrp_schedule_day_type', 'N', 1, NOW()),

-- 排班修改类型 (hrp_schedule_change_type)
(1012, 0, '调班', '调班', 'hrp_schedule_change_type', 'N', 1, NOW()),
(1013, 1, '替班', '替班', 'hrp_schedule_change_type', 'N', 1, NOW()),
(1014, 2, '新增临时', '新增临时', 'hrp_schedule_change_type', 'N', 1, NOW()),

-- 通用审批状态 (common_approval_status) -- 用于请假申请
(1015, 0, '待审批', '待审批', 'common_approval_status', 'Y', 1, NOW()),
(1016, 1, '已批准', '已批准', 'common_approval_status', 'N', 1, NOW()),
(1017, 2, '已驳回', '已驳回', 'common_approval_status', 'N', 1, NOW()),

-- 排休申请状态 (hrp_leave_request_status)
(1018, 0, '已提交', '已提交', 'hrp_leave_request_status', 'Y', 1, NOW()),
(1019, 1, '已锁定', '已锁定', 'hrp_leave_request_status', 'N', 1, NOW()),
(1020, 2, '已取消', '已取消', 'hrp_leave_request_status', 'N', 1, NOW()),

-- 考勤异常类型 (hrp_attendance_exception_type)
(1021, 0, '忘记打卡', '忘记打卡', 'hrp_attendance_exception_type', 'N', 1, NOW()),
(1022, 1, '迟到未请假', '迟到未请假', 'hrp_attendance_exception_type', 'N', 1, NOW()),
(1023, 2, '早退未请假', '早退未请假', 'hrp_attendance_exception_type', 'N', 1, NOW()),
(1024, 3, '缺勤', '缺勤', 'hrp_attendance_exception_type', 'N', 1, NOW()),

-- 考勤异常处理状态 (hrp_attendance_approval_status)
(1025, 0, '待处理', '待处理', 'hrp_attendance_approval_status', 'Y', 1, NOW()),
(1026, 1, '已修正', '已修正', 'hrp_attendance_approval_status', 'N', 1, NOW()),

-- 调货单状态 (hrp_inventory_transfer_status)
(1027, 0, '待审核', '待审核', 'hrp_inventory_transfer_status', 'Y', 1, NOW()),
(1028, 1, '待确认', '待确认', 'hrp_inventory_transfer_status', 'N', 1, NOW()),
(1029, 2, '已完成', '已完成', 'hrp_inventory_transfer_status', 'N', 1, NOW()),

-- 成本分摊方式 (hrp_cost_allocation_type)
(1030, 0, '依时数', '依时数', 'hrp_cost_allocation_type', 'Y', 1, NOW()),
(1031, 1, '依比例', '依比例', 'hrp_cost_allocation_type', 'N', 1, NOW());
