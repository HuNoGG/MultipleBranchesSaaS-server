-- MySQL dump 10.13  Distrib 8.0.28, for macos11 (arm64)
--
-- Host: 127.0.0.1    Database: private
-- ------------------------------------------------------
-- Server version	8.0.28

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `flow_category`
--

DROP TABLE IF EXISTS `flow_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `flow_category` (
                                 `category_id` bigint NOT NULL COMMENT '流程分类ID',
                                 `tenant_id` varchar(20) DEFAULT '000000' COMMENT '租户编号',
                                 `parent_id` bigint DEFAULT '0' COMMENT '父流程分类id',
                                 `ancestors` varchar(500) DEFAULT '' COMMENT '祖级列表',
                                 `category_name` varchar(30) NOT NULL COMMENT '流程分类名称',
                                 `order_num` int DEFAULT '0' COMMENT '显示顺序',
                                 `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 1代表删除）',
                                 `create_dept` bigint DEFAULT NULL COMMENT '创建部门',
                                 `create_by` bigint DEFAULT NULL COMMENT '创建者',
                                 `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                 `update_by` bigint DEFAULT NULL COMMENT '更新者',
                                 `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                 PRIMARY KEY (`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='流程分类';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `flow_category`
--

LOCK TABLES `flow_category` WRITE;
/*!40000 ALTER TABLE `flow_category` DISABLE KEYS */;
/*!40000 ALTER TABLE `flow_category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `flow_definition`
--

DROP TABLE IF EXISTS `flow_definition`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `flow_definition` (
                                   `id` bigint NOT NULL COMMENT '主键id',
                                   `flow_code` varchar(40) NOT NULL COMMENT '流程编码',
                                   `flow_name` varchar(100) NOT NULL COMMENT '流程名称',
                                   `category` varchar(100) DEFAULT NULL COMMENT '流程类别',
                                   `version` varchar(20) NOT NULL COMMENT '流程版本',
                                   `is_publish` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否发布（0未发布 1已发布 9失效）',
                                   `form_custom` char(1) DEFAULT 'N' COMMENT '审批表单是否自定义（Y是 N否）',
                                   `form_path` varchar(100) DEFAULT NULL COMMENT '审批表单路径',
                                   `activity_status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '流程激活状态（0挂起 1激活）',
                                   `listener_type` varchar(100) DEFAULT NULL COMMENT '监听器类型',
                                   `listener_path` varchar(400) DEFAULT NULL COMMENT '监听器路径',
                                   `ext` varchar(500) DEFAULT NULL COMMENT '业务详情 存业务表对象json字符串',
                                   `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                   `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                   `del_flag` char(1) DEFAULT '0' COMMENT '删除标志',
                                   `tenant_id` varchar(40) DEFAULT NULL COMMENT '租户id',
                                   PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='流程定义表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `flow_definition`
--

LOCK TABLES `flow_definition` WRITE;
/*!40000 ALTER TABLE `flow_definition` DISABLE KEYS */;
/*!40000 ALTER TABLE `flow_definition` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `flow_his_task`
--

DROP TABLE IF EXISTS `flow_his_task`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `flow_his_task` (
                                 `id` bigint NOT NULL COMMENT '主键id',
                                 `definition_id` bigint NOT NULL COMMENT '对应flow_definition表的id',
                                 `instance_id` bigint NOT NULL COMMENT '对应flow_instance表的id',
                                 `task_id` bigint NOT NULL COMMENT '对应flow_task表的id',
                                 `node_code` varchar(100) DEFAULT NULL COMMENT '开始节点编码',
                                 `node_name` varchar(100) DEFAULT NULL COMMENT '开始节点名称',
                                 `node_type` tinyint(1) DEFAULT NULL COMMENT '开始节点类型（0开始节点 1中间节点 2结束节点 3互斥网关 4并行网关）',
                                 `target_node_code` varchar(200) DEFAULT NULL COMMENT '目标节点编码',
                                 `target_node_name` varchar(200) DEFAULT NULL COMMENT '结束节点名称',
                                 `approver` varchar(40) DEFAULT NULL COMMENT '审批者',
                                 `cooperate_type` tinyint(1) NOT NULL DEFAULT '0' COMMENT '协作方式(1审批 2转办 3委派 4会签 5票签 6加签 7减签)',
                                 `collaborator` varchar(40) DEFAULT NULL COMMENT '协作人',
                                 `skip_type` varchar(10) NOT NULL COMMENT '流转类型（PASS通过 REJECT退回 NONE无动作）',
                                 `flow_status` varchar(20) NOT NULL COMMENT '流程状态（0待提交 1审批中 2审批通过 4终止 5作废 6撤销 8已完成 9已退回 10失效 11拿回）',
                                 `form_custom` char(1) DEFAULT 'N' COMMENT '审批表单是否自定义（Y是 N否）',
                                 `form_path` varchar(100) DEFAULT NULL COMMENT '审批表单路径',
                                 `message` varchar(500) DEFAULT NULL COMMENT '审批意见',
                                 `variable` text COMMENT '任务变量',
                                 `ext` text COMMENT '业务详情 存业务表对象json字符串',
                                 `create_time` datetime DEFAULT NULL COMMENT '任务开始时间',
                                 `update_time` datetime DEFAULT NULL COMMENT '审批完成时间',
                                 `del_flag` char(1) DEFAULT '0' COMMENT '删除标志',
                                 `tenant_id` varchar(40) DEFAULT NULL COMMENT '租户id',
                                 PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='历史任务记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `flow_his_task`
--

LOCK TABLES `flow_his_task` WRITE;
/*!40000 ALTER TABLE `flow_his_task` DISABLE KEYS */;
/*!40000 ALTER TABLE `flow_his_task` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `flow_instance`
--

DROP TABLE IF EXISTS `flow_instance`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `flow_instance` (
                                 `id` bigint NOT NULL COMMENT '主键id',
                                 `definition_id` bigint NOT NULL COMMENT '对应flow_definition表的id',
                                 `business_id` varchar(40) NOT NULL COMMENT '业务id',
                                 `node_type` tinyint(1) NOT NULL COMMENT '节点类型（0开始节点 1中间节点 2结束节点 3互斥网关 4并行网关）',
                                 `node_code` varchar(40) NOT NULL COMMENT '流程节点编码',
                                 `node_name` varchar(100) DEFAULT NULL COMMENT '流程节点名称',
                                 `variable` text COMMENT '任务变量',
                                 `flow_status` varchar(20) NOT NULL COMMENT '流程状态（0待提交 1审批中 2审批通过 4终止 5作废 6撤销 8已完成 9已退回 10失效 11拿回）',
                                 `activity_status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '流程激活状态（0挂起 1激活）',
                                 `def_json` text COMMENT '流程定义json',
                                 `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
                                 `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                 `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                 `ext` varchar(500) DEFAULT NULL COMMENT '扩展字段，预留给业务系统使用',
                                 `del_flag` char(1) DEFAULT '0' COMMENT '删除标志',
                                 `tenant_id` varchar(40) DEFAULT NULL COMMENT '租户id',
                                 PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='流程实例表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `flow_instance`
--

LOCK TABLES `flow_instance` WRITE;
/*!40000 ALTER TABLE `flow_instance` DISABLE KEYS */;
/*!40000 ALTER TABLE `flow_instance` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `flow_node`
--

DROP TABLE IF EXISTS `flow_node`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `flow_node` (
                             `id` bigint NOT NULL COMMENT '主键id',
                             `node_type` tinyint(1) NOT NULL COMMENT '节点类型（0开始节点 1中间节点 2结束节点 3互斥网关 4并行网关）',
                             `definition_id` bigint NOT NULL COMMENT '流程定义id',
                             `node_code` varchar(100) NOT NULL COMMENT '流程节点编码',
                             `node_name` varchar(100) DEFAULT NULL COMMENT '流程节点名称',
                             `permission_flag` varchar(200) DEFAULT NULL COMMENT '权限标识（权限类型:权限标识，可以多个，用@@隔开)',
                             `node_ratio` decimal(6,3) DEFAULT NULL COMMENT '流程签署比例值',
                             `coordinate` varchar(100) DEFAULT NULL COMMENT '坐标',
                             `any_node_skip` varchar(100) DEFAULT NULL COMMENT '任意结点跳转',
                             `listener_type` varchar(100) DEFAULT NULL COMMENT '监听器类型',
                             `listener_path` varchar(400) DEFAULT NULL COMMENT '监听器路径',
                             `handler_type` varchar(100) DEFAULT NULL COMMENT '处理器类型',
                             `handler_path` varchar(400) DEFAULT NULL COMMENT '处理器路径',
                             `form_custom` char(1) DEFAULT 'N' COMMENT '审批表单是否自定义（Y是 N否）',
                             `form_path` varchar(100) DEFAULT NULL COMMENT '审批表单路径',
                             `version` varchar(20) NOT NULL COMMENT '版本',
                             `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                             `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                             `ext` text COMMENT '节点扩展属性',
                             `del_flag` char(1) DEFAULT '0' COMMENT '删除标志',
                             `tenant_id` varchar(40) DEFAULT NULL COMMENT '租户id',
                             PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='流程节点表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `flow_node`
--

LOCK TABLES `flow_node` WRITE;
/*!40000 ALTER TABLE `flow_node` DISABLE KEYS */;
/*!40000 ALTER TABLE `flow_node` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `flow_skip`
--

DROP TABLE IF EXISTS `flow_skip`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `flow_skip` (
                             `id` bigint NOT NULL COMMENT '主键id',
                             `definition_id` bigint NOT NULL COMMENT '流程定义id',
                             `now_node_code` varchar(100) NOT NULL COMMENT '当前流程节点的编码',
                             `now_node_type` tinyint(1) DEFAULT NULL COMMENT '当前节点类型（0开始节点 1中间节点 2结束节点 3互斥网关 4并行网关）',
                             `next_node_code` varchar(100) NOT NULL COMMENT '下一个流程节点的编码',
                             `next_node_type` tinyint(1) DEFAULT NULL COMMENT '下一个节点类型（0开始节点 1中间节点 2结束节点 3互斥网关 4并行网关）',
                             `skip_name` varchar(100) DEFAULT NULL COMMENT '跳转名称',
                             `skip_type` varchar(40) DEFAULT NULL COMMENT '跳转类型（PASS审批通过 REJECT退回）',
                             `skip_condition` varchar(200) DEFAULT NULL COMMENT '跳转条件',
                             `coordinate` varchar(100) DEFAULT NULL COMMENT '坐标',
                             `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                             `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                             `del_flag` char(1) DEFAULT '0' COMMENT '删除标志',
                             `tenant_id` varchar(40) DEFAULT NULL COMMENT '租户id',
                             PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='节点跳转关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `flow_skip`
--

LOCK TABLES `flow_skip` WRITE;
/*!40000 ALTER TABLE `flow_skip` DISABLE KEYS */;
/*!40000 ALTER TABLE `flow_skip` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `flow_task`
--

DROP TABLE IF EXISTS `flow_task`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `flow_task` (
                             `id` bigint NOT NULL COMMENT '主键id',
                             `definition_id` bigint NOT NULL COMMENT '对应flow_definition表的id',
                             `instance_id` bigint NOT NULL COMMENT '对应flow_instance表的id',
                             `node_code` varchar(100) NOT NULL COMMENT '节点编码',
                             `node_name` varchar(100) DEFAULT NULL COMMENT '节点名称',
                             `node_type` tinyint(1) NOT NULL COMMENT '节点类型（0开始节点 1中间节点 2结束节点 3互斥网关 4并行网关）',
                             `flow_status` varchar(20) NOT NULL COMMENT '流程状态（0待提交 1审批中 2审批通过 4终止 5作废 6撤销 8已完成 9已退回 10失效 11拿回）',
                             `form_custom` char(1) DEFAULT 'N' COMMENT '审批表单是否自定义（Y是 N否）',
                             `form_path` varchar(100) DEFAULT NULL COMMENT '审批表单路径',
                             `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                             `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                             `del_flag` char(1) DEFAULT '0' COMMENT '删除标志',
                             `tenant_id` varchar(40) DEFAULT NULL COMMENT '租户id',
                             PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='待办任务表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `flow_task`
--

LOCK TABLES `flow_task` WRITE;
/*!40000 ALTER TABLE `flow_task` DISABLE KEYS */;
/*!40000 ALTER TABLE `flow_task` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `flow_user`
--

DROP TABLE IF EXISTS `flow_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `flow_user` (
                             `id` bigint NOT NULL COMMENT '主键id',
                             `type` char(1) NOT NULL COMMENT '人员类型（1待办任务的审批人权限 2待办任务的转办人权限 3待办任务的委托人权限）',
                             `processed_by` varchar(80) DEFAULT NULL COMMENT '权限人',
                             `associated` bigint NOT NULL COMMENT '任务表id',
                             `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                             `create_by` varchar(80) DEFAULT NULL COMMENT '创建人',
                             `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                             `del_flag` char(1) DEFAULT '0' COMMENT '删除标志',
                             `tenant_id` varchar(40) DEFAULT NULL COMMENT '租户id',
                             PRIMARY KEY (`id`),
                             KEY `user_associated` (`associated`),
                             KEY `user_processed_type` (`processed_by`,`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='流程用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `flow_user`
--

LOCK TABLES `flow_user` WRITE;
/*!40000 ALTER TABLE `flow_user` DISABLE KEYS */;
/*!40000 ALTER TABLE `flow_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `gen_table`
--

DROP TABLE IF EXISTS `gen_table`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `gen_table` (
                             `table_id` bigint NOT NULL COMMENT '编号',
                             `data_name` varchar(200) DEFAULT '' COMMENT '数据源名称',
                             `table_name` varchar(200) DEFAULT '' COMMENT '表名称',
                             `table_comment` varchar(500) DEFAULT '' COMMENT '表描述',
                             `sub_table_name` varchar(64) DEFAULT NULL COMMENT '关联子表的表名',
                             `sub_table_fk_name` varchar(64) DEFAULT NULL COMMENT '子表关联的外键名',
                             `class_name` varchar(100) DEFAULT '' COMMENT '实体类名称',
                             `tpl_category` varchar(200) DEFAULT 'crud' COMMENT '使用的模板（crud单表操作 tree树表操作）',
                             `package_name` varchar(100) DEFAULT NULL COMMENT '生成包路径',
                             `module_name` varchar(30) DEFAULT NULL COMMENT '生成模块名',
                             `business_name` varchar(30) DEFAULT NULL COMMENT '生成业务名',
                             `function_name` varchar(50) DEFAULT NULL COMMENT '生成功能名',
                             `function_author` varchar(50) DEFAULT NULL COMMENT '生成功能作者',
                             `gen_type` char(1) DEFAULT '0' COMMENT '生成代码方式（0zip压缩包 1自定义路径）',
                             `gen_path` varchar(200) DEFAULT '/' COMMENT '生成路径（不填默认项目路径）',
                             `options` varchar(1000) DEFAULT NULL COMMENT '其它生成选项',
                             `create_dept` bigint DEFAULT NULL COMMENT '创建部门',
                             `create_by` bigint DEFAULT NULL COMMENT '创建者',
                             `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                             `update_by` bigint DEFAULT NULL COMMENT '更新者',
                             `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                             `remark` varchar(500) DEFAULT NULL COMMENT '备注',
                             PRIMARY KEY (`table_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='代码生成业务表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `gen_table`
--

LOCK TABLES `gen_table` WRITE;
/*!40000 ALTER TABLE `gen_table` DISABLE KEYS */;
/*!40000 ALTER TABLE `gen_table` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `gen_table_column`
--

DROP TABLE IF EXISTS `gen_table_column`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `gen_table_column` (
                                    `column_id` bigint NOT NULL COMMENT '编号',
                                    `table_id` bigint DEFAULT NULL COMMENT '归属表编号',
                                    `column_name` varchar(200) DEFAULT NULL COMMENT '列名称',
                                    `column_comment` varchar(500) DEFAULT NULL COMMENT '列描述',
                                    `column_type` varchar(100) DEFAULT NULL COMMENT '列类型',
                                    `java_type` varchar(500) DEFAULT NULL COMMENT 'JAVA类型',
                                    `java_field` varchar(200) DEFAULT NULL COMMENT 'JAVA字段名',
                                    `is_pk` char(1) DEFAULT NULL COMMENT '是否主键（1是）',
                                    `is_increment` char(1) DEFAULT NULL COMMENT '是否自增（1是）',
                                    `is_required` char(1) DEFAULT NULL COMMENT '是否必填（1是）',
                                    `is_insert` char(1) DEFAULT NULL COMMENT '是否为插入字段（1是）',
                                    `is_edit` char(1) DEFAULT NULL COMMENT '是否编辑字段（1是）',
                                    `is_list` char(1) DEFAULT NULL COMMENT '是否列表字段（1是）',
                                    `is_query` char(1) DEFAULT NULL COMMENT '是否查询字段（1是）',
                                    `query_type` varchar(200) DEFAULT 'EQ' COMMENT '查询方式（等于、不等于、大于、小于、范围）',
                                    `html_type` varchar(200) DEFAULT NULL COMMENT '显示类型（文本框、文本域、下拉框、复选框、单选框、日期控件）',
                                    `dict_type` varchar(200) DEFAULT '' COMMENT '字典类型',
                                    `sort` int DEFAULT NULL COMMENT '排序',
                                    `create_dept` bigint DEFAULT NULL COMMENT '创建部门',
                                    `create_by` bigint DEFAULT NULL COMMENT '创建者',
                                    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                    `update_by` bigint DEFAULT NULL COMMENT '更新者',
                                    `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                    PRIMARY KEY (`column_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='代码生成业务表字段';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `gen_table_column`
--

LOCK TABLES `gen_table_column` WRITE;
/*!40000 ALTER TABLE `gen_table_column` DISABLE KEYS */;
/*!40000 ALTER TABLE `gen_table_column` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hrp_attendance_exceptions`
--

DROP TABLE IF EXISTS `hrp_attendance_exceptions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hrp_attendance_exceptions` (
                                             `id` bigint NOT NULL AUTO_INCREMENT COMMENT '异常记录 ID',
                                             `user_id` bigint DEFAULT NULL COMMENT '员工 ID',
                                             `exception_date` date NOT NULL COMMENT '异常日期',
                                             `exception_type` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '异常类型(字典: 忘记打卡, 迟到未请假, 早退未请假, 缺勤)',
                                             `original_clock_in` timestamp NULL DEFAULT NULL COMMENT '原始打卡(上班)',
                                             `original_clock_out` timestamp NULL DEFAULT NULL COMMENT '原始打卡(下班)',
                                             `corrected_clock_in` timestamp NULL DEFAULT NULL COMMENT '修正后打卡(上班)',
                                             `corrected_clock_out` timestamp NULL DEFAULT NULL COMMENT '修正后打卡(下班)',
                                             `approval_status` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '状态(字典: 待处理, 已修正)',
                                             `approved_by` bigint DEFAULT NULL COMMENT '审核人 ID',
                                             `create_dept` bigint DEFAULT NULL COMMENT '创建部门',
                                             `create_by` bigint DEFAULT NULL COMMENT '创建者',
                                             `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                             `update_by` bigint DEFAULT NULL COMMENT '更新者',
                                             `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                             `status` char(1) COLLATE utf8mb4_unicode_ci DEFAULT '0' COMMENT '状态(0正常1停用)',
                                             `remark` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
                                             `tenant_id` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT '000000' COMMENT '租户id',
                                             PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='考勤异常表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hrp_attendance_exceptions`
--

LOCK TABLES `hrp_attendance_exceptions` WRITE;
/*!40000 ALTER TABLE `hrp_attendance_exceptions` DISABLE KEYS */;
INSERT INTO `hrp_attendance_exceptions` VALUES (1,102,'2024-08-25','迟到未请假','2024-08-25 00:10:00','2024-08-25 08:01:00',NULL,NULL,'待处理',NULL,NULL,1,'2025-08-23 17:35:02',NULL,NULL,'0',NULL,'000000'),(2,104,'2024-08-25','早退未请假','2024-08-25 07:59:00','2024-08-25 15:30:00',NULL,NULL,'待处理',NULL,NULL,1,'2025-08-23 17:35:02',NULL,NULL,'0',NULL,'000000'),(3,103,'2024-08-25','忘记打卡',NULL,NULL,NULL,NULL,'待处理',NULL,NULL,1,'2025-08-23 17:35:02',NULL,NULL,'0',NULL,'000000');
/*!40000 ALTER TABLE `hrp_attendance_exceptions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hrp_attendance_records`
--

DROP TABLE IF EXISTS `hrp_attendance_records`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hrp_attendance_records` (
                                          `id` bigint NOT NULL AUTO_INCREMENT COMMENT '记录唯一ID',
                                          `user_id` bigint DEFAULT NULL COMMENT '员工 ID',
                                          `schedule_id` bigint DEFAULT NULL COMMENT '关联的排班记录ID',
                                          `clock_in_time` timestamp NULL DEFAULT NULL COMMENT '上班打卡时间',
                                          `clock_out_time` timestamp NULL DEFAULT NULL COMMENT '下班打卡时间',
                                          `record_date` date DEFAULT NULL COMMENT '记录日期',
                                          `tenant_id` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT '000000' COMMENT '租户id',
                                          PRIMARY KEY (`id`),
                                          KEY `fk_records_schedule_id` (`schedule_id`),
                                          CONSTRAINT `fk_records_schedule_id` FOREIGN KEY (`schedule_id`) REFERENCES `hrp_schedules` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='打卡记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hrp_attendance_records`
--

LOCK TABLES `hrp_attendance_records` WRITE;
/*!40000 ALTER TABLE `hrp_attendance_records` DISABLE KEYS */;
INSERT INTO `hrp_attendance_records` VALUES (1,101,1,'2024-08-24 23:58:00','2024-08-25 08:05:00','2024-08-25','000000'),(2,102,2,'2024-08-25 00:10:00','2024-08-25 08:01:00','2024-08-25','000000'),(3,104,3,'2024-08-25 07:59:00','2024-08-25 15:30:00','2024-08-25','000000'),(4,103,4,NULL,NULL,'2024-08-25','000000');
/*!40000 ALTER TABLE `hrp_attendance_records` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hrp_cross_store_cost_allocation`
--

DROP TABLE IF EXISTS `hrp_cross_store_cost_allocation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hrp_cross_store_cost_allocation` (
                                                   `id` bigint NOT NULL AUTO_INCREMENT COMMENT '唯一ID',
                                                   `user_id` bigint DEFAULT NULL COMMENT '员工 ID',
                                                   `allocation_type` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '分摊方式(字典: 依时数, 依比例)',
                                                   `allocation_rules` json DEFAULT NULL COMMENT '分摊规则 示例:{"storeA": 50, "storeB":50} (比例分摊)',
                                                   `create_dept` bigint DEFAULT NULL COMMENT '创建部门',
                                                   `create_by` bigint DEFAULT NULL COMMENT '创建者',
                                                   `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                                   `update_by` bigint DEFAULT NULL COMMENT '更新者',
                                                   `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                                   `status` char(1) COLLATE utf8mb4_unicode_ci DEFAULT '0' COMMENT '状态(0正常1停用)',
                                                   `remark` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
                                                   `tenant_id` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT '000000' COMMENT '租户id',
                                                   PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='跨店成本分摊设定表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hrp_cross_store_cost_allocation`
--

LOCK TABLES `hrp_cross_store_cost_allocation` WRITE;
/*!40000 ALTER TABLE `hrp_cross_store_cost_allocation` DISABLE KEYS */;
/*!40000 ALTER TABLE `hrp_cross_store_cost_allocation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hrp_inventory_transfers`
--

DROP TABLE IF EXISTS `hrp_inventory_transfers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hrp_inventory_transfers` (
                                           `id` bigint NOT NULL AUTO_INCREMENT COMMENT '调货单 ID',
                                           `request_store_id` bigint DEFAULT NULL COMMENT '申请方分店ID (A分店)',
                                           `provider_store_id` bigint DEFAULT NULL COMMENT '提供方分店ID(B分店)',
                                           `request_user_id` bigint DEFAULT NULL COMMENT '申请人ID',
                                           `transfer_details` json DEFAULT NULL COMMENT '调货内容(品项,数量)示例:[{"item":"商品A","qty":10}]',
                                           `approval_status` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '状态(字典: 待审核, 待确认, 已完成)',
                                           `request_confirmed_at` timestamp NULL DEFAULT NULL COMMENT '申请方确认时间',
                                           `provider_confirmed_at` timestamp NULL DEFAULT NULL COMMENT '提供方确认时间',
                                           `responsible_user_id` bigint DEFAULT NULL COMMENT '该班次责任人ID',
                                           `create_dept` bigint DEFAULT NULL COMMENT '创建部门',
                                           `create_by` bigint DEFAULT NULL COMMENT '创建者',
                                           `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                           `update_by` bigint DEFAULT NULL COMMENT '更新者',
                                           `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                           `status` char(1) COLLATE utf8mb4_unicode_ci DEFAULT '0' COMMENT '状态(0正常1停用)',
                                           `remark` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
                                           `tenant_id` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT '000000' COMMENT '租户id',
                                           PRIMARY KEY (`id`),
                                           KEY `fk_transfers_req_store_id` (`request_store_id`),
                                           KEY `fk_transfers_prov_store_id` (`provider_store_id`),
                                           CONSTRAINT `fk_transfers_prov_store_id` FOREIGN KEY (`provider_store_id`) REFERENCES `hrp_stores` (`id`),
                                           CONSTRAINT `fk_transfers_req_store_id` FOREIGN KEY (`request_store_id`) REFERENCES `hrp_stores` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='调货记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hrp_inventory_transfers`
--

LOCK TABLES `hrp_inventory_transfers` WRITE;
/*!40000 ALTER TABLE `hrp_inventory_transfers` DISABLE KEYS */;
/*!40000 ALTER TABLE `hrp_inventory_transfers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hrp_leave_applications`
--

DROP TABLE IF EXISTS `hrp_leave_applications`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hrp_leave_applications` (
                                          `id` bigint NOT NULL AUTO_INCREMENT COMMENT '申请 ID',
                                          `user_id` bigint DEFAULT NULL COMMENT '申请人ID(关联系统用户表)',
                                          `leave_type` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '请假类型(如:事假,病假,年假,婚假)',
                                          `start_time` datetime NOT NULL COMMENT '请假开始时间(精确到时分)',
                                          `end_time` datetime NOT NULL COMMENT '请假结束时间(精确到时分)',
                                          `leave_days` decimal(5,1) DEFAULT '0.0' COMMENT '请假天数(自动计算,支持0.5天粒度)',
                                          `reason` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '请假事由',
                                          `attachment_url` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '附件链接(如病假证明、休假凭证)',
                                          `approval_status` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '审批状态(字典: 待审批, 已批准, 已驳回)',
                                          `approved_by` bigint DEFAULT NULL COMMENT '审批人ID(关联系统用户表,审批后生效)',
                                          `create_dept` bigint DEFAULT NULL COMMENT '创建部门(申请人所属部门)',
                                          `create_by` bigint DEFAULT NULL COMMENT '创建者(通常为申请人本人)',
                                          `create_time` datetime DEFAULT NULL COMMENT '申请创建时间',
                                          `update_by` bigint DEFAULT NULL COMMENT '更新者(如审批人或申请人修改时)',
                                          `update_time` datetime DEFAULT NULL COMMENT '记录更新时间',
                                          `status` char(1) COLLATE utf8mb4_unicode_ci DEFAULT '0' COMMENT '数据状态(0正常1停用/作废)',
                                          `remark` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注(如审批意见、特殊说明)',
                                          `tenant_id` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT '000000' COMMENT '租户id',
                                          PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='正式请假申请表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hrp_leave_applications`
--

LOCK TABLES `hrp_leave_applications` WRITE;
/*!40000 ALTER TABLE `hrp_leave_applications` DISABLE KEYS */;
/*!40000 ALTER TABLE `hrp_leave_applications` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hrp_leave_requests`
--

DROP TABLE IF EXISTS `hrp_leave_requests`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hrp_leave_requests` (
                                      `id` bigint NOT NULL AUTO_INCREMENT COMMENT '唯一ID',
                                      `user_id` bigint DEFAULT NULL COMMENT '员工 ID',
                                      `leave_date` date NOT NULL COMMENT '期望休假日期',
                                      `approval_status` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '状态(字典: 已提交, 已锁定, 已取消)',
                                      `create_dept` bigint DEFAULT NULL COMMENT '创建部门',
                                      `create_by` bigint DEFAULT NULL COMMENT '创建者',
                                      `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                      `update_by` bigint DEFAULT NULL COMMENT '更新者',
                                      `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                      `status` char(1) COLLATE utf8mb4_unicode_ci DEFAULT '0' COMMENT '状态(0正常1停用)',
                                      `remark` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
                                      `tenant_id` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT '000000' COMMENT '租户id',
                                      PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='期望休假/排休记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hrp_leave_requests`
--

LOCK TABLES `hrp_leave_requests` WRITE;
/*!40000 ALTER TABLE `hrp_leave_requests` DISABLE KEYS */;
INSERT INTO `hrp_leave_requests` VALUES (1,102,'2024-08-26','已提交',NULL,102,'2025-08-23 17:35:02',NULL,NULL,'0',NULL,'000000');
/*!40000 ALTER TABLE `hrp_leave_requests` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hrp_payroll_records`
--

DROP TABLE IF EXISTS `hrp_payroll_records`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hrp_payroll_records` (
                                       `id` bigint NOT NULL AUTO_INCREMENT COMMENT '薪资单 ID',
                                       `user_id` bigint DEFAULT NULL COMMENT '员工 ID',
                                       `pay_period_start` date DEFAULT NULL COMMENT '薪资周期开始',
                                       `pay_period_end` date DEFAULT NULL COMMENT '薪资周期结束',
                                       `total_hours` decimal(10,2) DEFAULT NULL COMMENT '总工时',
                                       `base_pay` decimal(10,2) DEFAULT NULL COMMENT '基本薪资',
                                       `overtime_pay` decimal(10,2) DEFAULT NULL COMMENT '加班费',
                                       `deductions` decimal(10,2) DEFAULT NULL COMMENT '扣款',
                                       `final_salary` decimal(10,2) DEFAULT NULL COMMENT '最终薪资',
                                       `is_finalized` tinyint(1) DEFAULT NULL COMMENT '是否已确认(TRUE =是,FALSE = 否)',
                                       `create_dept` bigint DEFAULT NULL COMMENT '创建部门',
                                       `create_by` bigint DEFAULT NULL COMMENT '创建者',
                                       `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                       `update_by` bigint DEFAULT NULL COMMENT '更新者',
                                       `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                       `status` char(1) COLLATE utf8mb4_unicode_ci DEFAULT '0' COMMENT '状态(0正常1停用)',
                                       `remark` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
                                       `tenant_id` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT '000000' COMMENT '租户id',
                                       PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='薪资单记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hrp_payroll_records`
--

LOCK TABLES `hrp_payroll_records` WRITE;
/*!40000 ALTER TABLE `hrp_payroll_records` DISABLE KEYS */;
/*!40000 ALTER TABLE `hrp_payroll_records` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hrp_payroll_settings`
--

DROP TABLE IF EXISTS `hrp_payroll_settings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hrp_payroll_settings` (
                                        `id` bigint NOT NULL AUTO_INCREMENT COMMENT '规则 ID',
                                        `user_id` bigint DEFAULT NULL COMMENT '员工 ID',
                                        `base_salary` decimal(10,2) DEFAULT NULL COMMENT '月薪(全职适用)',
                                        `hourly_rate` decimal(10,2) DEFAULT NULL COMMENT '时薪(计时适用)',
                                        `effective_date` date DEFAULT NULL COMMENT '生效日期',
                                        `create_dept` bigint DEFAULT NULL COMMENT '创建部门',
                                        `create_by` bigint DEFAULT NULL COMMENT '创建者',
                                        `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                        `update_by` bigint DEFAULT NULL COMMENT '更新者',
                                        `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                        `status` char(1) COLLATE utf8mb4_unicode_ci DEFAULT '0' COMMENT '状态(0正常1停用)',
                                        `remark` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
                                        `tenant_id` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT '000000' COMMENT '租户id',
                                        PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='薪资规则表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hrp_payroll_settings`
--

LOCK TABLES `hrp_payroll_settings` WRITE;
/*!40000 ALTER TABLE `hrp_payroll_settings` DISABLE KEYS */;
INSERT INTO `hrp_payroll_settings` VALUES (1,101,12000.00,NULL,'2024-01-01',NULL,1,'2025-08-23 17:35:02',NULL,NULL,'0',NULL,'000000'),(2,102,8000.00,NULL,'2024-01-01',NULL,1,'2025-08-23 17:35:02',NULL,NULL,'0',NULL,'000000'),(3,103,7500.00,NULL,'2024-01-01',NULL,1,'2025-08-23 17:35:02',NULL,NULL,'0',NULL,'000000'),(4,104,NULL,25.00,'2024-01-01',NULL,1,'2025-08-23 17:35:02',NULL,NULL,'0',NULL,'000000'),(5,105,NULL,22.00,'2024-01-01',NULL,1,'2025-08-23 17:35:02',NULL,NULL,'0',NULL,'000000');
/*!40000 ALTER TABLE `hrp_payroll_settings` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hrp_schedule_modifications`
--

DROP TABLE IF EXISTS `hrp_schedule_modifications`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hrp_schedule_modifications` (
                                              `id` bigint NOT NULL AUTO_INCREMENT COMMENT '记录 ID',
                                              `schedule_id` bigint DEFAULT NULL COMMENT '关联的排班记录ID(原排班表主键)',
                                              `original_user_id` bigint DEFAULT NULL COMMENT '原员工ID(修改前的排班员工)',
                                              `new_user_id` bigint DEFAULT NULL COMMENT '新员工ID(修改后的排班员工,change_type为''新增临时'')',
                                              `change_type` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '修改类型(字典: 调班, 替班, 新增临时)',
                                              `changed_by` bigint DEFAULT NULL COMMENT '修改人ID(操作修改的用户)',
                                              `change_time` datetime DEFAULT NULL COMMENT '修改时间',
                                              `remark` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注(如:调班原因、替班说明等)',
                                              `tenant_id` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT '000000' COMMENT '租户id',
                                              PRIMARY KEY (`id`),
                                              KEY `fk_mods_schedule_id` (`schedule_id`),
                                              CONSTRAINT `fk_mods_schedule_id` FOREIGN KEY (`schedule_id`) REFERENCES `hrp_schedules` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='排班修改记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hrp_schedule_modifications`
--

LOCK TABLES `hrp_schedule_modifications` WRITE;
/*!40000 ALTER TABLE `hrp_schedule_modifications` DISABLE KEYS */;
/*!40000 ALTER TABLE `hrp_schedule_modifications` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hrp_schedule_requirements`
--

DROP TABLE IF EXISTS `hrp_schedule_requirements`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hrp_schedule_requirements` (
                                             `id` bigint NOT NULL AUTO_INCREMENT COMMENT '唯一ID',
                                             `store_id` bigint DEFAULT NULL COMMENT '分店 ID',
                                             `day_type` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '日期类型(字典: 平日, 假日, 特殊节日)',
                                             `shift_id` bigint DEFAULT NULL COMMENT '班别 ID',
                                             `skill_id` bigint DEFAULT NULL COMMENT '岗位(技能) ID',
                                             `required_count` int NOT NULL COMMENT '需求人数',
                                             `create_dept` bigint DEFAULT NULL COMMENT '创建部门',
                                             `create_by` bigint DEFAULT NULL COMMENT '创建者',
                                             `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                             `update_by` bigint DEFAULT NULL COMMENT '更新者',
                                             `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                             `status` char(1) COLLATE utf8mb4_unicode_ci DEFAULT '0' COMMENT '状态(0正常1停用)',
                                             `remark` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
                                             `tenant_id` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT '000000' COMMENT '租户id',
                                             PRIMARY KEY (`id`),
                                             KEY `fk_reqs_store_id` (`store_id`),
                                             KEY `fk_reqs_shift_id` (`shift_id`),
                                             KEY `fk_reqs_skill_id` (`skill_id`),
                                             CONSTRAINT `fk_reqs_shift_id` FOREIGN KEY (`shift_id`) REFERENCES `hrp_shifts` (`id`),
                                             CONSTRAINT `fk_reqs_skill_id` FOREIGN KEY (`skill_id`) REFERENCES `hrp_skills` (`id`),
                                             CONSTRAINT `fk_reqs_store_id` FOREIGN KEY (`store_id`) REFERENCES `hrp_stores` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='每日人力需求表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hrp_schedule_requirements`
--

LOCK TABLES `hrp_schedule_requirements` WRITE;
/*!40000 ALTER TABLE `hrp_schedule_requirements` DISABLE KEYS */;
INSERT INTO `hrp_schedule_requirements` VALUES (1,1,'平日',1,3,1,NULL,NULL,NULL,NULL,NULL,'0',NULL,'000000'),(2,1,'平日',1,4,2,NULL,NULL,NULL,NULL,NULL,'0',NULL,'000000'),(3,1,'平日',2,2,1,NULL,NULL,NULL,NULL,NULL,'0',NULL,'000000'),(4,1,'平日',2,3,1,NULL,NULL,NULL,NULL,NULL,'0',NULL,'000000'),(5,1,'平日',2,4,2,NULL,NULL,NULL,NULL,NULL,'0',NULL,'000000'),(6,1,'平日',3,5,1,NULL,NULL,NULL,NULL,NULL,'0',NULL,'000000'),(7,1,'假日',1,2,1,NULL,NULL,NULL,NULL,NULL,'0',NULL,'000000'),(8,1,'假日',1,3,2,NULL,NULL,NULL,NULL,NULL,'0',NULL,'000000'),(9,1,'假日',1,4,3,NULL,NULL,NULL,NULL,NULL,'0',NULL,'000000'),(10,1,'假日',2,2,2,NULL,NULL,NULL,NULL,NULL,'0',NULL,'000000'),(11,1,'假日',2,3,2,NULL,NULL,NULL,NULL,NULL,'0',NULL,'000000'),(12,1,'假日',2,4,3,NULL,NULL,NULL,NULL,NULL,'0',NULL,'000000');
/*!40000 ALTER TABLE `hrp_schedule_requirements` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hrp_schedules`
--

DROP TABLE IF EXISTS `hrp_schedules`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hrp_schedules` (
                                 `id` bigint NOT NULL AUTO_INCREMENT COMMENT '排班记录唯一ID',
                                 `user_id` bigint DEFAULT NULL COMMENT '员工 ID',
                                 `store_id` bigint DEFAULT NULL COMMENT '上班分店 ID',
                                 `shift_id` bigint DEFAULT NULL COMMENT '班别 ID',
                                 `skill_id` bigint DEFAULT NULL COMMENT '担任岗位 ID',
                                 `schedule_date` date NOT NULL COMMENT '排班日期',
                                 `version` int DEFAULT NULL COMMENT '版本号,用于管理与回溯',
                                 `tenant_id` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT '000000' COMMENT '租户id',
                                 PRIMARY KEY (`id`),
                                 KEY `fk_schedules_store_id` (`store_id`),
                                 KEY `fk_schedules_shift_id` (`shift_id`),
                                 KEY `fk_schedules_skill_id` (`skill_id`),
                                 CONSTRAINT `fk_schedules_shift_id` FOREIGN KEY (`shift_id`) REFERENCES `hrp_shifts` (`id`),
                                 CONSTRAINT `fk_schedules_skill_id` FOREIGN KEY (`skill_id`) REFERENCES `hrp_skills` (`id`),
                                 CONSTRAINT `fk_schedules_store_id` FOREIGN KEY (`store_id`) REFERENCES `hrp_stores` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='排班表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hrp_schedules`
--

LOCK TABLES `hrp_schedules` WRITE;
/*!40000 ALTER TABLE `hrp_schedules` DISABLE KEYS */;
INSERT INTO `hrp_schedules` VALUES (1,101,1,1,3,'2024-08-25',1,'000000'),(2,102,1,1,4,'2024-08-25',1,'000000'),(3,104,1,2,2,'2024-08-25',1,'000000'),(4,103,2,4,3,'2024-08-25',1,'000000'),(5,104,1,3,5,'2025-08-18',NULL,'000000'),(6,101,1,1,3,'2025-08-21',NULL,'000000'),(7,101,1,1,3,'2025-08-22',NULL,'000000'),(8,102,1,1,4,'2025-08-22',NULL,'000000'),(9,104,1,3,5,'2025-08-19',NULL,'000000'),(10,102,1,1,4,'2025-08-18',NULL,'000000'),(11,101,1,1,3,'2025-08-18',NULL,'000000'),(12,101,1,1,3,'2025-08-19',NULL,'000000'),(13,102,1,1,4,'2025-08-19',NULL,'000000'),(14,102,1,1,4,'2025-08-20',NULL,'000000'),(15,102,1,1,4,'2025-08-21',NULL,'000000'),(16,101,1,1,3,'2025-08-20',NULL,'000000');
/*!40000 ALTER TABLE `hrp_schedules` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hrp_shift_breaks`
--

DROP TABLE IF EXISTS `hrp_shift_breaks`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hrp_shift_breaks` (
                                    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '唯一ID',
                                    `shift_id` bigint DEFAULT NULL COMMENT '班别 ID',
                                    `break_start_time` time NOT NULL COMMENT '休息开始时间',
                                    `break_end_time` time NOT NULL COMMENT '休息结束时间',
                                    `tenant_id` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT '000000' COMMENT '租户id',
                                    PRIMARY KEY (`id`),
                                    KEY `fk_breaks_shift_id` (`shift_id`),
                                    CONSTRAINT `fk_breaks_shift_id` FOREIGN KEY (`shift_id`) REFERENCES `hrp_shifts` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='班别休息时段表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hrp_shift_breaks`
--

LOCK TABLES `hrp_shift_breaks` WRITE;
/*!40000 ALTER TABLE `hrp_shift_breaks` DISABLE KEYS */;
INSERT INTO `hrp_shift_breaks` VALUES (1,1,'12:00:00','13:00:00','000000'),(2,2,'18:00:00','19:00:00','000000'),(3,4,'12:30:00','13:30:00','000000'),(4,5,'19:00:00','20:00:00','000000');
/*!40000 ALTER TABLE `hrp_shift_breaks` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hrp_shifts`
--

DROP TABLE IF EXISTS `hrp_shifts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hrp_shifts` (
                              `id` bigint NOT NULL AUTO_INCREMENT COMMENT '班别唯一ID',
                              `store_id` bigint DEFAULT NULL COMMENT '所属分店 ID',
                              `name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '班别名称(如:早班)',
                              `code` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '班别代码(如:早)',
                              `start_time` time NOT NULL COMMENT '开始时间',
                              `end_time` time NOT NULL COMMENT '结束时间',
                              `is_cross_day` tinyint(1) DEFAULT '0' COMMENT '是否跨日(TRUE =是,FALSE =否)',
                              `color_code` varchar(7) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '班表显示颜色(如:#FF5733)',
                              `create_dept` bigint DEFAULT NULL COMMENT '创建部门',
                              `create_by` bigint DEFAULT NULL COMMENT '创建者',
                              `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                              `update_by` bigint DEFAULT NULL COMMENT '更新者',
                              `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                              `status` char(1) COLLATE utf8mb4_unicode_ci DEFAULT '0' COMMENT '状态(0正常1停用)',
                              `remark` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
                              `tenant_id` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT '000000' COMMENT '租户id',
                              PRIMARY KEY (`id`),
                              KEY `fk_shifts_store_id` (`store_id`),
                              CONSTRAINT `fk_shifts_store_id` FOREIGN KEY (`store_id`) REFERENCES `hrp_stores` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='班别设定表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hrp_shifts`
--

LOCK TABLES `hrp_shifts` WRITE;
/*!40000 ALTER TABLE `hrp_shifts` DISABLE KEYS */;
INSERT INTO `hrp_shifts` VALUES (1,1,'总店早班','早','08:00:00','16:00:00',0,'#409EFF',NULL,1,'2025-08-23 17:35:02',NULL,NULL,'0',NULL,'000000'),(2,1,'总店晚班','晚','16:00:00','23:59:59',0,'#F56C6C',NULL,1,'2025-08-23 17:35:02',NULL,NULL,'0',NULL,'000000'),(3,1,'总店打烊班','烊','22:00:00','02:00:00',1,'#67C23A',NULL,1,'2025-08-23 17:35:02',NULL,NULL,'0',NULL,'000000'),(4,2,'西区店早班','早','09:00:00','17:00:00',0,'#409EFF',NULL,1,'2025-08-23 17:35:02',NULL,NULL,'0',NULL,'000000'),(5,2,'西区店晚班','晚','17:00:00','01:00:00',1,'#F56C6C',NULL,1,'2025-08-23 17:35:02',NULL,NULL,'0',NULL,'000000');
/*!40000 ALTER TABLE `hrp_shifts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hrp_skills`
--

DROP TABLE IF EXISTS `hrp_skills`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hrp_skills` (
                              `id` bigint NOT NULL AUTO_INCREMENT COMMENT '技能唯一ID',
                              `name` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '技能名称(如: 出锅,带位)',
                              `create_dept` bigint DEFAULT NULL COMMENT '创建部门',
                              `create_by` bigint DEFAULT NULL COMMENT '创建者',
                              `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                              `update_by` bigint DEFAULT NULL COMMENT '更新者',
                              `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                              `status` char(1) COLLATE utf8mb4_unicode_ci DEFAULT '0' COMMENT '状态(0正常1停用)',
                              `remark` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
                              `tenant_id` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT '000000' COMMENT '租户id',
                              PRIMARY KEY (`id`),
                              UNIQUE KEY `uk_skill_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='技能岗位表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hrp_skills`
--

LOCK TABLES `hrp_skills` WRITE;
/*!40000 ALTER TABLE `hrp_skills` DISABLE KEYS */;
INSERT INTO `hrp_skills` VALUES (1,'出锅',NULL,1,'2025-08-23 17:35:02',NULL,NULL,'0',NULL,'000000'),(2,'带位',NULL,1,'2025-08-23 17:35:02',NULL,NULL,'0',NULL,'000000'),(3,'收银',NULL,1,'2025-08-23 17:35:02',NULL,NULL,'0',NULL,'000000'),(4,'后厨',NULL,1,'2025-08-23 17:35:02',NULL,NULL,'0',NULL,'000000'),(5,'清洁',NULL,1,'2025-08-23 17:35:02',NULL,NULL,'0',NULL,'000000');
/*!40000 ALTER TABLE `hrp_skills` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hrp_store_events`
--

DROP TABLE IF EXISTS `hrp_store_events`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hrp_store_events` (
                                    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '事件ID',
                                    `store_id` bigint DEFAULT NULL COMMENT '分店 ID (关联分店表)',
                                    `event_date` date NOT NULL COMMENT '事件日期',
                                    `event_type` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '事件类型(字典: 全天放假, 上午放假, 下午放假)',
                                    `description` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '事件描述(如:法定节假日放假、设备检修)',
                                    `create_by` bigint DEFAULT NULL COMMENT '创建者(关联系统用户表 sys_user.user_id)',
                                    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                    `tenant_id` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT '000000' COMMENT '租户id',
                                    PRIMARY KEY (`id`),
                                    KEY `fk_events_store_id` (`store_id`),
                                    CONSTRAINT `fk_events_store_id` FOREIGN KEY (`store_id`) REFERENCES `hrp_stores` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='分店特殊事件表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hrp_store_events`
--

LOCK TABLES `hrp_store_events` WRITE;
/*!40000 ALTER TABLE `hrp_store_events` DISABLE KEYS */;
INSERT INTO `hrp_store_events` VALUES (1,1,'2024-10-01','全天放假','国庆节法定节假日',1,'2025-08-23 17:35:02','000000');
/*!40000 ALTER TABLE `hrp_store_events` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hrp_store_support_relations`
--

DROP TABLE IF EXISTS `hrp_store_support_relations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hrp_store_support_relations` (
                                               `requesting_store_id` bigint NOT NULL COMMENT '请求支援的分店ID',
                                               `supporting_store_id` bigint NOT NULL COMMENT '可提供支援的分店 ID',
                                               `tenant_id` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT '000000' COMMENT '租户id',
                                               PRIMARY KEY (`requesting_store_id`,`supporting_store_id`),
                                               KEY `fk_support_supporting_id` (`supporting_store_id`),
                                               CONSTRAINT `fk_support_requesting_id` FOREIGN KEY (`requesting_store_id`) REFERENCES `hrp_stores` (`id`),
                                               CONSTRAINT `fk_support_supporting_id` FOREIGN KEY (`supporting_store_id`) REFERENCES `hrp_stores` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='分店支援关系表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hrp_store_support_relations`
--

LOCK TABLES `hrp_store_support_relations` WRITE;
/*!40000 ALTER TABLE `hrp_store_support_relations` DISABLE KEYS */;
INSERT INTO `hrp_store_support_relations` VALUES (2,1,'000000');
/*!40000 ALTER TABLE `hrp_store_support_relations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hrp_stores`
--

DROP TABLE IF EXISTS `hrp_stores`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hrp_stores` (
                              `id` bigint NOT NULL AUTO_INCREMENT COMMENT '分店唯一ID',
                              `tenant_id` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT '000000' COMMENT '租户编号',
                              `name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '分店名称',
                              `address` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '分店地址',
                              `cross_day_rule` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT 'by_shift_start' COMMENT '跨日工时归属规则(字典: by_shift_start, by_calendar_day)',
                              `create_dept` bigint DEFAULT NULL COMMENT '创建部门',
                              `create_by` bigint DEFAULT NULL COMMENT '创建者',
                              `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                              `update_by` bigint DEFAULT NULL COMMENT '更新者',
                              `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                              `status` char(1) COLLATE utf8mb4_unicode_ci DEFAULT '0' COMMENT '状态(0正常1停用)',
                              `remark` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
                              PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='分店表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hrp_stores`
--

LOCK TABLES `hrp_stores` WRITE;
/*!40000 ALTER TABLE `hrp_stores` DISABLE KEYS */;
INSERT INTO `hrp_stores` VALUES (1,'000000','市中心总店','A市中山路1号','by_shift_start',NULL,1,'2025-08-23 17:35:02',NULL,NULL,'0',NULL),(2,'000000','西区旗舰店','A市解放西路88号','by_calendar_day',NULL,1,'2025-08-23 17:35:02',NULL,NULL,'0',NULL);
/*!40000 ALTER TABLE `hrp_stores` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hrp_user_availability`
--

DROP TABLE IF EXISTS `hrp_user_availability`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hrp_user_availability` (
                                         `id` bigint NOT NULL AUTO_INCREMENT COMMENT '唯一ID',
                                         `user_id` bigint DEFAULT NULL COMMENT '员工 ID',
                                         `day_of_week` int DEFAULT NULL COMMENT '星期几 (1=周一, 2=周二, ..., 7=周日)',
                                         `start_time` time DEFAULT NULL COMMENT '可上班的开始时间',
                                         `end_time` time DEFAULT NULL COMMENT '可上班的结束时间',
                                         `tenant_id` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT '000000' COMMENT '租户id',
                                         PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='员工可上班时段表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hrp_user_availability`
--

LOCK TABLES `hrp_user_availability` WRITE;
/*!40000 ALTER TABLE `hrp_user_availability` DISABLE KEYS */;
INSERT INTO `hrp_user_availability` VALUES (1,104,1,'17:00:00','22:00:00','000000'),(2,104,2,'17:00:00','22:00:00','000000'),(3,104,6,'10:00:00','22:00:00','000000'),(4,104,7,'10:00:00','22:00:00','000000'),(5,105,3,'09:00:00','15:00:00','000000'),(6,105,4,'09:00:00','15:00:00','000000'),(7,105,5,'09:00:00','15:00:00','000000');
/*!40000 ALTER TABLE `hrp_user_availability` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hrp_user_profile`
--

DROP TABLE IF EXISTS `hrp_user_profile`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hrp_user_profile` (
                                    `user_id` bigint NOT NULL COMMENT '员工ID(关联系统用户表)',
                                    `tenant_id` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT '000000' COMMENT '租户编号',
                                    `employee_type` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '员工分类(字典: 干部, 全职, 计时)',
                                    `main_store_id` bigint DEFAULT NULL COMMENT '主要归属分店 ID',
                                    `priority_score` int DEFAULT '0' COMMENT '分配工作优先分数',
                                    `create_dept` bigint DEFAULT NULL COMMENT '创建部门',
                                    `create_by` bigint DEFAULT NULL COMMENT '创建者',
                                    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                    `update_by` bigint DEFAULT NULL COMMENT '更新者',
                                    `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                    `status` char(1) COLLATE utf8mb4_unicode_ci DEFAULT '0' COMMENT '状态(0在职1离职)',
                                    `remark` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
                                    PRIMARY KEY (`user_id`),
                                    KEY `fk_profile_store_id` (`main_store_id`),
                                    CONSTRAINT `fk_profile_store_id` FOREIGN KEY (`main_store_id`) REFERENCES `hrp_stores` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='员工档案扩展表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hrp_user_profile`
--

LOCK TABLES `hrp_user_profile` WRITE;
/*!40000 ALTER TABLE `hrp_user_profile` DISABLE KEYS */;
INSERT INTO `hrp_user_profile` VALUES (101,'000000','干部',1,100,NULL,1,'2025-08-23 17:35:02',NULL,NULL,'0',NULL),(102,'000000','全职',1,80,NULL,1,'2025-08-23 17:35:02',NULL,NULL,'0',NULL),(103,'000000','全职',2,80,NULL,1,'2025-08-23 17:35:02',NULL,NULL,'0',NULL),(104,'000000','计时',1,50,NULL,1,'2025-08-23 17:35:02',NULL,NULL,'0',NULL),(105,'000000','计时',2,50,NULL,1,'2025-08-23 17:35:02',NULL,NULL,'0',NULL);
/*!40000 ALTER TABLE `hrp_user_profile` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hrp_user_skills`
--

DROP TABLE IF EXISTS `hrp_user_skills`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hrp_user_skills` (
                                   `user_id` bigint NOT NULL COMMENT '员工 ID',
                                   `skill_id` bigint NOT NULL COMMENT '技能 ID',
                                   `tenant_id` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT '000000' COMMENT '租户id',
                                   PRIMARY KEY (`user_id`,`skill_id`),
                                   KEY `fk_userskills_skill_id` (`skill_id`),
                                   CONSTRAINT `fk_userskills_skill_id` FOREIGN KEY (`skill_id`) REFERENCES `hrp_skills` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='员工技能关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hrp_user_skills`
--

LOCK TABLES `hrp_user_skills` WRITE;
/*!40000 ALTER TABLE `hrp_user_skills` DISABLE KEYS */;
INSERT INTO `hrp_user_skills` VALUES (101,1,'000000'),(101,2,'000000'),(101,3,'000000'),(101,4,'000000'),(101,5,'000000'),(102,1,'000000'),(102,4,'000000'),(103,2,'000000'),(103,3,'000000'),(104,2,'000000'),(104,5,'000000'),(105,4,'000000'),(105,5,'000000');
/*!40000 ALTER TABLE `hrp_user_skills` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hrp_user_store_access`
--

DROP TABLE IF EXISTS `hrp_user_store_access`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hrp_user_store_access` (
                                         `user_id` bigint NOT NULL COMMENT '员工 ID',
                                         `store_id` bigint NOT NULL COMMENT '授权支援的分店 ID',
                                         `tenant_id` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT '000000' COMMENT '租户id',
                                         PRIMARY KEY (`user_id`,`store_id`),
                                         KEY `fk_access_store_id` (`store_id`),
                                         CONSTRAINT `fk_access_store_id` FOREIGN KEY (`store_id`) REFERENCES `hrp_stores` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='员工跨店权限表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hrp_user_store_access`
--

LOCK TABLES `hrp_user_store_access` WRITE;
/*!40000 ALTER TABLE `hrp_user_store_access` DISABLE KEYS */;
INSERT INTO `hrp_user_store_access` VALUES (102,2,'000000');
/*!40000 ALTER TABLE `hrp_user_store_access` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_client`
--

DROP TABLE IF EXISTS `sys_client`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_client` (
                              `id` bigint NOT NULL COMMENT 'id',
                              `client_id` varchar(64) DEFAULT NULL COMMENT '客户端id',
                              `client_key` varchar(32) DEFAULT NULL COMMENT '客户端key',
                              `client_secret` varchar(255) DEFAULT NULL COMMENT '客户端秘钥',
                              `grant_type` varchar(255) DEFAULT NULL COMMENT '授权类型',
                              `device_type` varchar(32) DEFAULT NULL COMMENT '设备类型',
                              `active_timeout` int DEFAULT '1800' COMMENT 'token活跃超时时间',
                              `timeout` int DEFAULT '604800' COMMENT 'token固定超时',
                              `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
                              `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 1代表删除）',
                              `create_dept` bigint DEFAULT NULL COMMENT '创建部门',
                              `create_by` bigint DEFAULT NULL COMMENT '创建者',
                              `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                              `update_by` bigint DEFAULT NULL COMMENT '更新者',
                              `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                              PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统授权表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_client`
--

LOCK TABLES `sys_client` WRITE;
/*!40000 ALTER TABLE `sys_client` DISABLE KEYS */;
INSERT INTO `sys_client` VALUES (1,'e5cd7e4891bf95d1d19206ce24a7b32e','pc','pc123','password,social','pc',1800,604800,'0','0',103,1,'2025-08-23 17:33:43',1,'2025-08-23 17:33:43'),(2,'428a8310cd442757ae699df5d894f051','app','app123','password,sms,social','android',1800,604800,'0','0',103,1,'2025-08-23 17:33:43',1,'2025-08-23 17:33:43');
/*!40000 ALTER TABLE `sys_client` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_config`
--

DROP TABLE IF EXISTS `sys_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_config` (
                              `config_id` bigint NOT NULL COMMENT '参数主键',
                              `tenant_id` varchar(20) DEFAULT '000000' COMMENT '租户编号',
                              `config_name` varchar(100) DEFAULT '' COMMENT '参数名称',
                              `config_key` varchar(100) DEFAULT '' COMMENT '参数键名',
                              `config_value` varchar(500) DEFAULT '' COMMENT '参数键值',
                              `config_type` char(1) DEFAULT 'N' COMMENT '系统内置（Y是 N否）',
                              `create_dept` bigint DEFAULT NULL COMMENT '创建部门',
                              `create_by` bigint DEFAULT NULL COMMENT '创建者',
                              `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                              `update_by` bigint DEFAULT NULL COMMENT '更新者',
                              `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                              `remark` varchar(500) DEFAULT NULL COMMENT '备注',
                              PRIMARY KEY (`config_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='参数配置表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_config`
--

LOCK TABLES `sys_config` WRITE;
/*!40000 ALTER TABLE `sys_config` DISABLE KEYS */;
INSERT INTO `sys_config` VALUES (1,'000000','主框架页-默认皮肤样式名称','sys.index.skinName','skin-blue','Y',103,1,'2025-08-23 17:33:43',NULL,NULL,'蓝色 skin-blue、绿色 skin-green、紫色 skin-purple、红色 skin-red、黄色 skin-yellow'),(2,'000000','用户管理-账号初始密码','sys.user.initPassword','123456','Y',103,1,'2025-08-23 17:33:43',NULL,NULL,'初始化密码 123456'),(3,'000000','主框架页-侧边栏主题','sys.index.sideTheme','theme-dark','Y',103,1,'2025-08-23 17:33:43',NULL,NULL,'深色主题theme-dark，浅色主题theme-light'),(5,'000000','账号自助-是否开启用户注册功能','sys.account.registerUser','false','Y',103,1,'2025-08-23 17:33:43',NULL,NULL,'是否开启注册用户功能（true开启，false关闭）'),(11,'000000','OSS预览列表资源开关','sys.oss.previewListResource','true','Y',103,1,'2025-08-23 17:33:43',NULL,NULL,'true:开启, false:关闭');
/*!40000 ALTER TABLE `sys_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_dept`
--

DROP TABLE IF EXISTS `sys_dept`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_dept` (
                            `dept_id` bigint NOT NULL COMMENT '部门id',
                            `tenant_id` varchar(20) DEFAULT '000000' COMMENT '租户编号',
                            `parent_id` bigint DEFAULT '0' COMMENT '父部门id',
                            `ancestors` varchar(500) DEFAULT '' COMMENT '祖级列表',
                            `dept_name` varchar(30) DEFAULT '' COMMENT '部门名称',
                            `dept_category` varchar(100) DEFAULT NULL COMMENT '部门类别编码',
                            `order_num` int DEFAULT '0' COMMENT '显示顺序',
                            `leader` bigint DEFAULT NULL COMMENT '负责人',
                            `phone` varchar(11) DEFAULT NULL COMMENT '联系电话',
                            `email` varchar(50) DEFAULT NULL COMMENT '邮箱',
                            `status` char(1) DEFAULT '0' COMMENT '部门状态（0正常 1停用）',
                            `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 1代表删除）',
                            `create_dept` bigint DEFAULT NULL COMMENT '创建部门',
                            `create_by` bigint DEFAULT NULL COMMENT '创建者',
                            `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                            `update_by` bigint DEFAULT NULL COMMENT '更新者',
                            `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                            PRIMARY KEY (`dept_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='部门表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_dept`
--

LOCK TABLES `sys_dept` WRITE;
/*!40000 ALTER TABLE `sys_dept` DISABLE KEYS */;
INSERT INTO `sys_dept` VALUES (100,'000000',0,'0','XXX科技',NULL,0,NULL,'15888888888','xxx@qq.com','0','0',103,1,'2025-08-23 17:33:38',NULL,NULL),(101,'000000',100,'0,100','深圳总公司',NULL,1,NULL,'15888888888','xxx@qq.com','0','0',103,1,'2025-08-23 17:33:38',NULL,NULL),(102,'000000',100,'0,100','长沙分公司',NULL,2,NULL,'15888888888','xxx@qq.com','0','0',103,1,'2025-08-23 17:33:38',NULL,NULL),(103,'000000',101,'0,100,101','研发部门',NULL,1,1,'15888888888','xxx@qq.com','0','0',103,1,'2025-08-23 17:33:38',NULL,NULL),(104,'000000',101,'0,100,101','市场部门',NULL,2,NULL,'15888888888','xxx@qq.com','0','0',103,1,'2025-08-23 17:33:38',NULL,NULL),(105,'000000',101,'0,100,101','测试部门',NULL,3,NULL,'15888888888','xxx@qq.com','0','0',103,1,'2025-08-23 17:33:38',NULL,NULL),(106,'000000',101,'0,100,101','财务部门',NULL,4,NULL,'15888888888','xxx@qq.com','0','0',103,1,'2025-08-23 17:33:38',NULL,NULL),(107,'000000',101,'0,100,101','运维部门',NULL,5,NULL,'15888888888','xxx@qq.com','0','0',103,1,'2025-08-23 17:33:38',NULL,NULL),(108,'000000',102,'0,100,102','市场部门',NULL,1,NULL,'15888888888','xxx@qq.com','0','0',103,1,'2025-08-23 17:33:38',NULL,NULL),(109,'000000',102,'0,100,102','财务部门',NULL,2,NULL,'15888888888','xxx@qq.com','0','0',103,1,'2025-08-23 17:33:38',NULL,NULL);
/*!40000 ALTER TABLE `sys_dept` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_dict_data`
--

DROP TABLE IF EXISTS `sys_dict_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_dict_data` (
                                 `dict_code` bigint NOT NULL COMMENT '字典编码',
                                 `tenant_id` varchar(20) DEFAULT '000000' COMMENT '租户编号',
                                 `dict_sort` int DEFAULT '0' COMMENT '字典排序',
                                 `dict_label` varchar(100) DEFAULT '' COMMENT '字典标签',
                                 `dict_value` varchar(100) DEFAULT '' COMMENT '字典键值',
                                 `dict_type` varchar(100) DEFAULT '' COMMENT '字典类型',
                                 `css_class` varchar(100) DEFAULT NULL COMMENT '样式属性（其他样式扩展）',
                                 `list_class` varchar(100) DEFAULT NULL COMMENT '表格回显样式',
                                 `is_default` char(1) DEFAULT 'N' COMMENT '是否默认（Y是 N否）',
                                 `create_dept` bigint DEFAULT NULL COMMENT '创建部门',
                                 `create_by` bigint DEFAULT NULL COMMENT '创建者',
                                 `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                 `update_by` bigint DEFAULT NULL COMMENT '更新者',
                                 `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                 `remark` varchar(500) DEFAULT NULL COMMENT '备注',
                                 PRIMARY KEY (`dict_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='字典数据表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_dict_data`
--

LOCK TABLES `sys_dict_data` WRITE;
/*!40000 ALTER TABLE `sys_dict_data` DISABLE KEYS */;
INSERT INTO `sys_dict_data` VALUES (1,'000000',1,'男','0','sys_user_sex','','','Y',103,1,'2025-08-23 17:33:43',NULL,NULL,'性别男'),(2,'000000',2,'女','1','sys_user_sex','','','N',103,1,'2025-08-23 17:33:43',NULL,NULL,'性别女'),(3,'000000',3,'未知','2','sys_user_sex','','','N',103,1,'2025-08-23 17:33:43',NULL,NULL,'性别未知'),(4,'000000',1,'显示','0','sys_show_hide','','primary','Y',103,1,'2025-08-23 17:33:43',NULL,NULL,'显示菜单'),(5,'000000',2,'隐藏','1','sys_show_hide','','danger','N',103,1,'2025-08-23 17:33:43',NULL,NULL,'隐藏菜单'),(6,'000000',1,'正常','0','sys_normal_disable','','primary','Y',103,1,'2025-08-23 17:33:43',NULL,NULL,'正常状态'),(7,'000000',2,'停用','1','sys_normal_disable','','danger','N',103,1,'2025-08-23 17:33:43',NULL,NULL,'停用状态'),(12,'000000',1,'是','Y','sys_yes_no','','primary','Y',103,1,'2025-08-23 17:33:43',NULL,NULL,'系统默认是'),(13,'000000',2,'否','N','sys_yes_no','','danger','N',103,1,'2025-08-23 17:33:43',NULL,NULL,'系统默认否'),(14,'000000',1,'通知','1','sys_notice_type','','warning','Y',103,1,'2025-08-23 17:33:43',NULL,NULL,'通知'),(15,'000000',2,'公告','2','sys_notice_type','','success','N',103,1,'2025-08-23 17:33:43',NULL,NULL,'公告'),(16,'000000',1,'正常','0','sys_notice_status','','primary','Y',103,1,'2025-08-23 17:33:43',NULL,NULL,'正常状态'),(17,'000000',2,'关闭','1','sys_notice_status','','danger','N',103,1,'2025-08-23 17:33:43',NULL,NULL,'关闭状态'),(18,'000000',1,'新增','1','sys_oper_type','','info','N',103,1,'2025-08-23 17:33:43',NULL,NULL,'新增操作'),(19,'000000',2,'修改','2','sys_oper_type','','info','N',103,1,'2025-08-23 17:33:43',NULL,NULL,'修改操作'),(20,'000000',3,'删除','3','sys_oper_type','','danger','N',103,1,'2025-08-23 17:33:43',NULL,NULL,'删除操作'),(21,'000000',4,'授权','4','sys_oper_type','','primary','N',103,1,'2025-08-23 17:33:43',NULL,NULL,'授权操作'),(22,'000000',5,'导出','5','sys_oper_type','','warning','N',103,1,'2025-08-23 17:33:43',NULL,NULL,'导出操作'),(23,'000000',6,'导入','6','sys_oper_type','','warning','N',103,1,'2025-08-23 17:33:43',NULL,NULL,'导入操作'),(24,'000000',7,'强退','7','sys_oper_type','','danger','N',103,1,'2025-08-23 17:33:43',NULL,NULL,'强退操作'),(25,'000000',8,'生成代码','8','sys_oper_type','','warning','N',103,1,'2025-08-23 17:33:43',NULL,NULL,'生成操作'),(26,'000000',9,'清空数据','9','sys_oper_type','','danger','N',103,1,'2025-08-23 17:33:43',NULL,NULL,'清空操作'),(27,'000000',1,'成功','0','sys_common_status','','primary','N',103,1,'2025-08-23 17:33:43',NULL,NULL,'正常状态'),(28,'000000',2,'失败','1','sys_common_status','','danger','N',103,1,'2025-08-23 17:33:43',NULL,NULL,'停用状态'),(29,'000000',99,'其他','0','sys_oper_type','','info','N',103,1,'2025-08-23 17:33:43',NULL,NULL,'其他操作'),(30,'000000',0,'密码认证','password','sys_grant_type','el-check-tag','default','N',103,1,'2025-08-23 17:33:43',NULL,NULL,'密码认证'),(31,'000000',0,'短信认证','sms','sys_grant_type','el-check-tag','default','N',103,1,'2025-08-23 17:33:43',NULL,NULL,'短信认证'),(32,'000000',0,'邮件认证','email','sys_grant_type','el-check-tag','default','N',103,1,'2025-08-23 17:33:43',NULL,NULL,'邮件认证'),(33,'000000',0,'小程序认证','xcx','sys_grant_type','el-check-tag','default','N',103,1,'2025-08-23 17:33:43',NULL,NULL,'小程序认证'),(34,'000000',0,'三方登录认证','social','sys_grant_type','el-check-tag','default','N',103,1,'2025-08-23 17:33:43',NULL,NULL,'三方登录认证'),(35,'000000',0,'PC','pc','sys_device_type','','default','N',103,1,'2025-08-23 17:33:43',NULL,NULL,'PC'),(36,'000000',0,'安卓','android','sys_device_type','','default','N',103,1,'2025-08-23 17:33:43',NULL,NULL,'安卓'),(37,'000000',0,'iOS','ios','sys_device_type','','default','N',103,1,'2025-08-23 17:33:43',NULL,NULL,'iOS'),(38,'000000',0,'小程序','xcx','sys_device_type','','default','N',103,1,'2025-08-23 17:33:43',NULL,NULL,'小程序'),(1001,'000000',0,'按班次开始日','by_shift_start','hrp_cross_day_rule',NULL,NULL,'Y',NULL,1,'2025-08-23 17:33:56',NULL,NULL,NULL),(1002,'000000',1,'按日历天拆分','by_calendar_day','hrp_cross_day_rule',NULL,NULL,'N',NULL,1,'2025-08-23 17:33:56',NULL,NULL,NULL),(1003,'000000',0,'全天放假','全天放假','hrp_store_event_type',NULL,NULL,'Y',NULL,1,'2025-08-23 17:33:56',NULL,NULL,NULL),(1004,'000000',1,'上午放假','上午放假','hrp_store_event_type',NULL,NULL,'N',NULL,1,'2025-08-23 17:33:56',NULL,NULL,NULL),(1005,'000000',2,'下午放假','下午放假','hrp_store_event_type',NULL,NULL,'N',NULL,1,'2025-08-23 17:33:56',NULL,NULL,NULL),(1006,'000000',0,'干部','干部','hrp_employee_type',NULL,NULL,'N',NULL,1,'2025-08-23 17:33:56',NULL,NULL,NULL),(1007,'000000',1,'全职','全职','hrp_employee_type',NULL,NULL,'Y',NULL,1,'2025-08-23 17:33:56',NULL,NULL,NULL),(1008,'000000',2,'计时','计时','hrp_employee_type',NULL,NULL,'N',NULL,1,'2025-08-23 17:33:56',NULL,NULL,NULL),(1009,'000000',0,'平日','平日','hrp_schedule_day_type',NULL,NULL,'Y',NULL,1,'2025-08-23 17:33:56',NULL,NULL,NULL),(1010,'000000',1,'假日','假日','hrp_schedule_day_type',NULL,NULL,'N',NULL,1,'2025-08-23 17:33:56',NULL,NULL,NULL),(1011,'000000',2,'特殊节日','特殊节日','hrp_schedule_day_type',NULL,NULL,'N',NULL,1,'2025-08-23 17:33:56',NULL,NULL,NULL),(1012,'000000',0,'调班','调班','hrp_schedule_change_type',NULL,NULL,'N',NULL,1,'2025-08-23 17:33:56',NULL,NULL,NULL),(1013,'000000',1,'替班','替班','hrp_schedule_change_type',NULL,NULL,'N',NULL,1,'2025-08-23 17:33:56',NULL,NULL,NULL),(1014,'000000',2,'新增临时','新增临时','hrp_schedule_change_type',NULL,NULL,'N',NULL,1,'2025-08-23 17:33:56',NULL,NULL,NULL),(1015,'000000',0,'待审批','待审批','common_approval_status',NULL,NULL,'Y',NULL,1,'2025-08-23 17:33:56',NULL,NULL,NULL),(1016,'000000',1,'已批准','已批准','common_approval_status',NULL,NULL,'N',NULL,1,'2025-08-23 17:33:56',NULL,NULL,NULL),(1017,'000000',2,'已驳回','已驳回','common_approval_status',NULL,NULL,'N',NULL,1,'2025-08-23 17:33:56',NULL,NULL,NULL),(1018,'000000',0,'已提交','已提交','hrp_leave_request_status',NULL,NULL,'Y',NULL,1,'2025-08-23 17:33:56',NULL,NULL,NULL),(1019,'000000',1,'已锁定','已锁定','hrp_leave_request_status',NULL,NULL,'N',NULL,1,'2025-08-23 17:33:56',NULL,NULL,NULL),(1020,'000000',2,'已取消','已取消','hrp_leave_request_status',NULL,NULL,'N',NULL,1,'2025-08-23 17:33:56',NULL,NULL,NULL),(1021,'000000',0,'忘记打卡','忘记打卡','hrp_attendance_exception_type',NULL,NULL,'N',NULL,1,'2025-08-23 17:33:56',NULL,NULL,NULL),(1022,'000000',1,'迟到未请假','迟到未请假','hrp_attendance_exception_type',NULL,NULL,'N',NULL,1,'2025-08-23 17:33:56',NULL,NULL,NULL),(1023,'000000',2,'早退未请假','早退未请假','hrp_attendance_exception_type',NULL,NULL,'N',NULL,1,'2025-08-23 17:33:56',NULL,NULL,NULL),(1024,'000000',3,'缺勤','缺勤','hrp_attendance_exception_type',NULL,NULL,'N',NULL,1,'2025-08-23 17:33:56',NULL,NULL,NULL),(1025,'000000',0,'待处理','待处理','hrp_attendance_approval_status',NULL,NULL,'Y',NULL,1,'2025-08-23 17:33:56',NULL,NULL,NULL),(1026,'000000',1,'已修正','已修正','hrp_attendance_approval_status',NULL,NULL,'N',NULL,1,'2025-08-23 17:33:56',NULL,NULL,NULL),(1027,'000000',0,'待审核','待审核','hrp_inventory_transfer_status',NULL,NULL,'Y',NULL,1,'2025-08-23 17:33:56',NULL,NULL,NULL),(1028,'000000',1,'待确认','待确认','hrp_inventory_transfer_status',NULL,NULL,'N',NULL,1,'2025-08-23 17:33:56',NULL,NULL,NULL),(1029,'000000',2,'已完成','已完成','hrp_inventory_transfer_status',NULL,NULL,'N',NULL,1,'2025-08-23 17:33:56',NULL,NULL,NULL),(1030,'000000',0,'依时数','依时数','hrp_cost_allocation_type',NULL,NULL,'Y',NULL,1,'2025-08-23 17:33:56',NULL,NULL,NULL),(1031,'000000',1,'依比例','依比例','hrp_cost_allocation_type',NULL,NULL,'N',NULL,1,'2025-08-23 17:33:56',NULL,NULL,NULL);
/*!40000 ALTER TABLE `sys_dict_data` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_dict_type`
--

DROP TABLE IF EXISTS `sys_dict_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_dict_type` (
                                 `dict_id` bigint NOT NULL COMMENT '字典主键',
                                 `tenant_id` varchar(20) DEFAULT '000000' COMMENT '租户编号',
                                 `dict_name` varchar(100) DEFAULT '' COMMENT '字典名称',
                                 `dict_type` varchar(100) DEFAULT '' COMMENT '字典类型',
                                 `create_dept` bigint DEFAULT NULL COMMENT '创建部门',
                                 `create_by` bigint DEFAULT NULL COMMENT '创建者',
                                 `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                 `update_by` bigint DEFAULT NULL COMMENT '更新者',
                                 `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                 `remark` varchar(500) DEFAULT NULL COMMENT '备注',
                                 PRIMARY KEY (`dict_id`),
                                 UNIQUE KEY `tenant_id` (`tenant_id`,`dict_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='字典类型表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_dict_type`
--

LOCK TABLES `sys_dict_type` WRITE;
/*!40000 ALTER TABLE `sys_dict_type` DISABLE KEYS */;
INSERT INTO `sys_dict_type` VALUES (1,'000000','用户性别','sys_user_sex',103,1,'2025-08-23 17:33:43',NULL,NULL,'用户性别列表'),(2,'000000','菜单状态','sys_show_hide',103,1,'2025-08-23 17:33:43',NULL,NULL,'菜单状态列表'),(3,'000000','系统开关','sys_normal_disable',103,1,'2025-08-23 17:33:43',NULL,NULL,'系统开关列表'),(6,'000000','系统是否','sys_yes_no',103,1,'2025-08-23 17:33:43',NULL,NULL,'系统是否列表'),(7,'000000','通知类型','sys_notice_type',103,1,'2025-08-23 17:33:43',NULL,NULL,'通知类型列表'),(8,'000000','通知状态','sys_notice_status',103,1,'2025-08-23 17:33:43',NULL,NULL,'通知状态列表'),(9,'000000','操作类型','sys_oper_type',103,1,'2025-08-23 17:33:43',NULL,NULL,'操作类型列表'),(10,'000000','系统状态','sys_common_status',103,1,'2025-08-23 17:33:43',NULL,NULL,'登录状态列表'),(11,'000000','授权类型','sys_grant_type',103,1,'2025-08-23 17:33:43',NULL,NULL,'认证授权类型'),(12,'000000','设备类型','sys_device_type',103,1,'2025-08-23 17:33:43',NULL,NULL,'客户端设备类型'),(101,'000000','跨日工时归属规则','hrp_cross_day_rule',NULL,1,'2025-08-23 17:33:56',NULL,NULL,'分店表的跨日工时归属规则'),(102,'000000','分店事件类型','hrp_store_event_type',NULL,1,'2025-08-23 17:33:56',NULL,NULL,'分店特殊事件的类型'),(103,'000000','员工分类','hrp_employee_type',NULL,1,'2025-08-23 17:33:56',NULL,NULL,'员工档案的员工分类'),(104,'000000','人力需求日期类型','hrp_schedule_day_type',NULL,1,'2025-08-23 17:33:56',NULL,NULL,'每日人力需求的日期类型'),(105,'000000','排班修改类型','hrp_schedule_change_type',NULL,1,'2025-08-23 17:33:56',NULL,NULL,'排班修改记录的变更类型'),(106,'000000','通用审批状态','common_approval_status',NULL,1,'2025-08-23 17:33:56',NULL,NULL,'通用的审批状态(待审批/已批准/已驳回)'),(107,'000000','排休申请状态','hrp_leave_request_status',NULL,1,'2025-08-23 17:33:56',NULL,NULL,'期望休假/排休记录的状态'),(108,'000000','考勤异常类型','hrp_attendance_exception_type',NULL,1,'2025-08-23 17:33:56',NULL,NULL,'考勤异常记录的类型'),(109,'000000','考勤异常处理状态','hrp_attendance_approval_status',NULL,1,'2025-08-23 17:33:56',NULL,NULL,'考勤异常的处理状态'),(110,'000000','调货单状态','hrp_inventory_transfer_status',NULL,1,'2025-08-23 17:33:56',NULL,NULL,'库存调货记录的状态'),(111,'000000','成本分摊方式','hrp_cost_allocation_type',NULL,1,'2025-08-23 17:33:56',NULL,NULL,'跨店成本分摊的设定方式');
/*!40000 ALTER TABLE `sys_dict_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_logininfor`
--

DROP TABLE IF EXISTS `sys_logininfor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_logininfor` (
                                  `info_id` bigint NOT NULL COMMENT '访问ID',
                                  `tenant_id` varchar(20) DEFAULT '000000' COMMENT '租户编号',
                                  `user_name` varchar(50) DEFAULT '' COMMENT '用户账号',
                                  `client_key` varchar(32) DEFAULT '' COMMENT '客户端',
                                  `device_type` varchar(32) DEFAULT '' COMMENT '设备类型',
                                  `ipaddr` varchar(128) DEFAULT '' COMMENT '登录IP地址',
                                  `login_location` varchar(255) DEFAULT '' COMMENT '登录地点',
                                  `browser` varchar(50) DEFAULT '' COMMENT '浏览器类型',
                                  `os` varchar(50) DEFAULT '' COMMENT '操作系统',
                                  `status` char(1) DEFAULT '0' COMMENT '登录状态（0成功 1失败）',
                                  `msg` varchar(255) DEFAULT '' COMMENT '提示消息',
                                  `login_time` datetime DEFAULT NULL COMMENT '访问时间',
                                  PRIMARY KEY (`info_id`),
                                  KEY `idx_sys_logininfor_s` (`status`),
                                  KEY `idx_sys_logininfor_lt` (`login_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统访问记录';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_logininfor`
--

LOCK TABLES `sys_logininfor` WRITE;
/*!40000 ALTER TABLE `sys_logininfor` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_logininfor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_menu`
--

DROP TABLE IF EXISTS `sys_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_menu` (
                            `menu_id` bigint NOT NULL COMMENT '菜单ID',
                            `menu_name` varchar(50) NOT NULL COMMENT '菜单名称',
                            `parent_id` bigint DEFAULT '0' COMMENT '父菜单ID',
                            `order_num` int DEFAULT '0' COMMENT '显示顺序',
                            `path` varchar(200) DEFAULT '' COMMENT '路由地址',
                            `component` varchar(255) DEFAULT NULL COMMENT '组件路径',
                            `query_param` varchar(255) DEFAULT NULL COMMENT '路由参数',
                            `is_frame` int DEFAULT '1' COMMENT '是否为外链（0是 1否）',
                            `is_cache` int DEFAULT '0' COMMENT '是否缓存（0缓存 1不缓存）',
                            `menu_type` char(1) DEFAULT '' COMMENT '菜单类型（M目录 C菜单 F按钮）',
                            `visible` char(1) DEFAULT '0' COMMENT '显示状态（0显示 1隐藏）',
                            `status` char(1) DEFAULT '0' COMMENT '菜单状态（0正常 1停用）',
                            `perms` varchar(100) DEFAULT NULL COMMENT '权限标识',
                            `icon` varchar(100) DEFAULT '#' COMMENT '菜单图标',
                            `create_dept` bigint DEFAULT NULL COMMENT '创建部门',
                            `create_by` bigint DEFAULT NULL COMMENT '创建者',
                            `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                            `update_by` bigint DEFAULT NULL COMMENT '更新者',
                            `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                            `remark` varchar(500) DEFAULT '' COMMENT '备注',
                            PRIMARY KEY (`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='菜单权限表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_menu`
--

LOCK TABLES `sys_menu` WRITE;
/*!40000 ALTER TABLE `sys_menu` DISABLE KEYS */;
INSERT INTO `sys_menu` VALUES (1,'系统管理',0,1,'system',NULL,'',1,0,'M','0','0','','system',103,1,'2025-08-23 17:33:39',NULL,NULL,'系统管理目录'),(2,'系统监控',0,3,'monitor',NULL,'',1,0,'M','0','0','','monitor',103,1,'2025-08-23 17:33:39',NULL,NULL,'系统监控目录'),(3,'系统工具',0,4,'tool',NULL,'',1,0,'M','0','0','','tool',103,1,'2025-08-23 17:33:39',NULL,NULL,'系统工具目录'),(4,'PLUS官网',0,5,'https://gitee.com/dromara/RuoYi-Vue-Plus',NULL,'',0,0,'M','0','0','','guide',103,1,'2025-08-23 17:33:39',NULL,NULL,'RuoYi-Vue-Plus官网地址'),(5,'测试菜单',0,5,'demo',NULL,'',1,0,'M','0','0','','star',103,1,'2025-08-23 17:33:39',NULL,NULL,'测试菜单'),(6,'租户管理',0,2,'tenant',NULL,'',1,0,'M','0','0','','chart',103,1,'2025-08-23 17:33:39',NULL,NULL,'租户管理目录'),(100,'用户管理',1,1,'user','system/user/index','',1,0,'C','0','0','system:user:list','user',103,1,'2025-08-23 17:33:39',NULL,NULL,'用户管理菜单'),(101,'角色管理',1,2,'role','system/role/index','',1,0,'C','0','0','system:role:list','peoples',103,1,'2025-08-23 17:33:39',NULL,NULL,'角色管理菜单'),(102,'菜单管理',1,3,'menu','system/menu/index','',1,0,'C','0','0','system:menu:list','tree-table',103,1,'2025-08-23 17:33:39',NULL,NULL,'菜单管理菜单'),(103,'部门管理',1,4,'dept','system/dept/index','',1,0,'C','0','0','system:dept:list','tree',103,1,'2025-08-23 17:33:39',NULL,NULL,'部门管理菜单'),(104,'岗位管理',1,5,'post','system/post/index','',1,0,'C','0','0','system:post:list','post',103,1,'2025-08-23 17:33:39',NULL,NULL,'岗位管理菜单'),(105,'字典管理',1,6,'dict','system/dict/index','',1,0,'C','0','0','system:dict:list','dict',103,1,'2025-08-23 17:33:39',NULL,NULL,'字典管理菜单'),(106,'参数设置',1,7,'config','system/config/index','',1,0,'C','0','0','system:config:list','edit',103,1,'2025-08-23 17:33:39',NULL,NULL,'参数设置菜单'),(107,'通知公告',1,8,'notice','system/notice/index','',1,0,'C','0','0','system:notice:list','message',103,1,'2025-08-23 17:33:39',NULL,NULL,'通知公告菜单'),(108,'日志管理',1,9,'log','','',1,0,'M','0','0','','log',103,1,'2025-08-23 17:33:39',NULL,NULL,'日志管理菜单'),(109,'在线用户',2,1,'online','monitor/online/index','',1,0,'C','0','0','monitor:online:list','online',103,1,'2025-08-23 17:33:39',NULL,NULL,'在线用户菜单'),(113,'缓存监控',2,5,'cache','monitor/cache/index','',1,0,'C','0','0','monitor:cache:list','redis',103,1,'2025-08-23 17:33:39',NULL,NULL,'缓存监控菜单'),(115,'代码生成',3,2,'gen','tool/gen/index','',1,0,'C','0','0','tool:gen:list','code',103,1,'2025-08-23 17:33:39',NULL,NULL,'代码生成菜单'),(116,'修改生成配置',3,2,'gen-edit/index/:tableId','tool/gen/editTable','',1,1,'C','1','0','tool:gen:edit','#',103,1,'2025-08-23 17:33:39',NULL,NULL,''),(117,'Admin监控',2,5,'Admin','monitor/admin/index','',1,0,'C','0','0','monitor:admin:list','dashboard',103,1,'2025-08-23 17:33:39',NULL,NULL,'Admin监控菜单'),(118,'文件管理',1,10,'oss','system/oss/index','',1,0,'C','0','0','system:oss:list','upload',103,1,'2025-08-23 17:33:39',NULL,NULL,'文件管理菜单'),(120,'任务调度中心',2,6,'snailjob','monitor/snailjob/index','',1,0,'C','0','0','monitor:snailjob:list','job',103,1,'2025-08-23 17:33:39',NULL,NULL,'SnailJob控制台菜单'),(121,'租户管理',6,1,'tenant','system/tenant/index','',1,0,'C','0','0','system:tenant:list','list',103,1,'2025-08-23 17:33:39',NULL,NULL,'租户管理菜单'),(122,'租户套餐管理',6,2,'tenantPackage','system/tenantPackage/index','',1,0,'C','0','0','system:tenantPackage:list','form',103,1,'2025-08-23 17:33:39',NULL,NULL,'租户套餐管理菜单'),(123,'客户端管理',1,11,'client','system/client/index','',1,0,'C','0','0','system:client:list','international',103,1,'2025-08-23 17:33:39',NULL,NULL,'客户端管理菜单'),(130,'分配用户',1,2,'role-auth/user/:roleId','system/role/authUser','',1,1,'C','1','0','system:role:edit','#',103,1,'2025-08-23 17:33:39',NULL,NULL,''),(131,'分配角色',1,1,'user-auth/role/:userId','system/user/authRole','',1,1,'C','1','0','system:user:edit','#',103,1,'2025-08-23 17:33:39',NULL,NULL,''),(132,'字典数据',1,6,'dict-data/index/:dictId','system/dict/data','',1,1,'C','1','0','system:dict:list','#',103,1,'2025-08-23 17:33:39',NULL,NULL,''),(133,'文件配置管理',1,10,'oss-config/index','system/oss/config','',1,1,'C','1','0','system:ossConfig:list','#',103,1,'2025-08-23 17:33:39',NULL,NULL,''),(500,'操作日志',108,1,'operlog','monitor/operlog/index','',1,0,'C','0','0','monitor:operlog:list','form',103,1,'2025-08-23 17:33:39',NULL,NULL,'操作日志菜单'),(501,'登录日志',108,2,'logininfor','monitor/logininfor/index','',1,0,'C','0','0','monitor:logininfor:list','logininfor',103,1,'2025-08-23 17:33:39',NULL,NULL,'登录日志菜单'),(1001,'用户查询',100,1,'','','',1,0,'F','0','0','system:user:query','#',103,1,'2025-08-23 17:33:39',NULL,NULL,''),(1002,'用户新增',100,2,'','','',1,0,'F','0','0','system:user:add','#',103,1,'2025-08-23 17:33:39',NULL,NULL,''),(1003,'用户修改',100,3,'','','',1,0,'F','0','0','system:user:edit','#',103,1,'2025-08-23 17:33:39',NULL,NULL,''),(1004,'用户删除',100,4,'','','',1,0,'F','0','0','system:user:remove','#',103,1,'2025-08-23 17:33:39',NULL,NULL,''),(1005,'用户导出',100,5,'','','',1,0,'F','0','0','system:user:export','#',103,1,'2025-08-23 17:33:39',NULL,NULL,''),(1006,'用户导入',100,6,'','','',1,0,'F','0','0','system:user:import','#',103,1,'2025-08-23 17:33:39',NULL,NULL,''),(1007,'重置密码',100,7,'','','',1,0,'F','0','0','system:user:resetPwd','#',103,1,'2025-08-23 17:33:39',NULL,NULL,''),(1008,'角色查询',101,1,'','','',1,0,'F','0','0','system:role:query','#',103,1,'2025-08-23 17:33:39',NULL,NULL,''),(1009,'角色新增',101,2,'','','',1,0,'F','0','0','system:role:add','#',103,1,'2025-08-23 17:33:39',NULL,NULL,''),(1010,'角色修改',101,3,'','','',1,0,'F','0','0','system:role:edit','#',103,1,'2025-08-23 17:33:39',NULL,NULL,''),(1011,'角色删除',101,4,'','','',1,0,'F','0','0','system:role:remove','#',103,1,'2025-08-23 17:33:39',NULL,NULL,''),(1012,'角色导出',101,5,'','','',1,0,'F','0','0','system:role:export','#',103,1,'2025-08-23 17:33:39',NULL,NULL,''),(1013,'菜单查询',102,1,'','','',1,0,'F','0','0','system:menu:query','#',103,1,'2025-08-23 17:33:39',NULL,NULL,''),(1014,'菜单新增',102,2,'','','',1,0,'F','0','0','system:menu:add','#',103,1,'2025-08-23 17:33:39',NULL,NULL,''),(1015,'菜单修改',102,3,'','','',1,0,'F','0','0','system:menu:edit','#',103,1,'2025-08-23 17:33:39',NULL,NULL,''),(1016,'菜单删除',102,4,'','','',1,0,'F','0','0','system:menu:remove','#',103,1,'2025-08-23 17:33:39',NULL,NULL,''),(1017,'部门查询',103,1,'','','',1,0,'F','0','0','system:dept:query','#',103,1,'2025-08-23 17:33:39',NULL,NULL,''),(1018,'部门新增',103,2,'','','',1,0,'F','0','0','system:dept:add','#',103,1,'2025-08-23 17:33:39',NULL,NULL,''),(1019,'部门修改',103,3,'','','',1,0,'F','0','0','system:dept:edit','#',103,1,'2025-08-23 17:33:39',NULL,NULL,''),(1020,'部门删除',103,4,'','','',1,0,'F','0','0','system:dept:remove','#',103,1,'2025-08-23 17:33:39',NULL,NULL,''),(1021,'岗位查询',104,1,'','','',1,0,'F','0','0','system:post:query','#',103,1,'2025-08-23 17:33:39',NULL,NULL,''),(1022,'岗位新增',104,2,'','','',1,0,'F','0','0','system:post:add','#',103,1,'2025-08-23 17:33:39',NULL,NULL,''),(1023,'岗位修改',104,3,'','','',1,0,'F','0','0','system:post:edit','#',103,1,'2025-08-23 17:33:40',NULL,NULL,''),(1024,'岗位删除',104,4,'','','',1,0,'F','0','0','system:post:remove','#',103,1,'2025-08-23 17:33:40',NULL,NULL,''),(1025,'岗位导出',104,5,'','','',1,0,'F','0','0','system:post:export','#',103,1,'2025-08-23 17:33:40',NULL,NULL,''),(1026,'字典查询',105,1,'#','','',1,0,'F','0','0','system:dict:query','#',103,1,'2025-08-23 17:33:40',NULL,NULL,''),(1027,'字典新增',105,2,'#','','',1,0,'F','0','0','system:dict:add','#',103,1,'2025-08-23 17:33:40',NULL,NULL,''),(1028,'字典修改',105,3,'#','','',1,0,'F','0','0','system:dict:edit','#',103,1,'2025-08-23 17:33:40',NULL,NULL,''),(1029,'字典删除',105,4,'#','','',1,0,'F','0','0','system:dict:remove','#',103,1,'2025-08-23 17:33:40',NULL,NULL,''),(1030,'字典导出',105,5,'#','','',1,0,'F','0','0','system:dict:export','#',103,1,'2025-08-23 17:33:40',NULL,NULL,''),(1031,'参数查询',106,1,'#','','',1,0,'F','0','0','system:config:query','#',103,1,'2025-08-23 17:33:40',NULL,NULL,''),(1032,'参数新增',106,2,'#','','',1,0,'F','0','0','system:config:add','#',103,1,'2025-08-23 17:33:40',NULL,NULL,''),(1033,'参数修改',106,3,'#','','',1,0,'F','0','0','system:config:edit','#',103,1,'2025-08-23 17:33:40',NULL,NULL,''),(1034,'参数删除',106,4,'#','','',1,0,'F','0','0','system:config:remove','#',103,1,'2025-08-23 17:33:40',NULL,NULL,''),(1035,'参数导出',106,5,'#','','',1,0,'F','0','0','system:config:export','#',103,1,'2025-08-23 17:33:40',NULL,NULL,''),(1036,'公告查询',107,1,'#','','',1,0,'F','0','0','system:notice:query','#',103,1,'2025-08-23 17:33:40',NULL,NULL,''),(1037,'公告新增',107,2,'#','','',1,0,'F','0','0','system:notice:add','#',103,1,'2025-08-23 17:33:40',NULL,NULL,''),(1038,'公告修改',107,3,'#','','',1,0,'F','0','0','system:notice:edit','#',103,1,'2025-08-23 17:33:40',NULL,NULL,''),(1039,'公告删除',107,4,'#','','',1,0,'F','0','0','system:notice:remove','#',103,1,'2025-08-23 17:33:40',NULL,NULL,''),(1040,'操作查询',500,1,'#','','',1,0,'F','0','0','monitor:operlog:query','#',103,1,'2025-08-23 17:33:40',NULL,NULL,''),(1041,'操作删除',500,2,'#','','',1,0,'F','0','0','monitor:operlog:remove','#',103,1,'2025-08-23 17:33:40',NULL,NULL,''),(1042,'日志导出',500,4,'#','','',1,0,'F','0','0','monitor:operlog:export','#',103,1,'2025-08-23 17:33:40',NULL,NULL,''),(1043,'登录查询',501,1,'#','','',1,0,'F','0','0','monitor:logininfor:query','#',103,1,'2025-08-23 17:33:40',NULL,NULL,''),(1044,'登录删除',501,2,'#','','',1,0,'F','0','0','monitor:logininfor:remove','#',103,1,'2025-08-23 17:33:40',NULL,NULL,''),(1045,'日志导出',501,3,'#','','',1,0,'F','0','0','monitor:logininfor:export','#',103,1,'2025-08-23 17:33:40',NULL,NULL,''),(1046,'在线查询',109,1,'#','','',1,0,'F','0','0','monitor:online:query','#',103,1,'2025-08-23 17:33:40',NULL,NULL,''),(1047,'批量强退',109,2,'#','','',1,0,'F','0','0','monitor:online:batchLogout','#',103,1,'2025-08-23 17:33:40',NULL,NULL,''),(1048,'单条强退',109,3,'#','','',1,0,'F','0','0','monitor:online:forceLogout','#',103,1,'2025-08-23 17:33:40',NULL,NULL,''),(1050,'账户解锁',501,4,'#','','',1,0,'F','0','0','monitor:logininfor:unlock','#',103,1,'2025-08-23 17:33:40',NULL,NULL,''),(1055,'生成查询',115,1,'#','','',1,0,'F','0','0','tool:gen:query','#',103,1,'2025-08-23 17:33:40',NULL,NULL,''),(1056,'生成修改',115,2,'#','','',1,0,'F','0','0','tool:gen:edit','#',103,1,'2025-08-23 17:33:40',NULL,NULL,''),(1057,'生成删除',115,3,'#','','',1,0,'F','0','0','tool:gen:remove','#',103,1,'2025-08-23 17:33:40',NULL,NULL,''),(1058,'导入代码',115,2,'#','','',1,0,'F','0','0','tool:gen:import','#',103,1,'2025-08-23 17:33:40',NULL,NULL,''),(1059,'预览代码',115,4,'#','','',1,0,'F','0','0','tool:gen:preview','#',103,1,'2025-08-23 17:33:40',NULL,NULL,''),(1060,'生成代码',115,5,'#','','',1,0,'F','0','0','tool:gen:code','#',103,1,'2025-08-23 17:33:40',NULL,NULL,''),(1061,'客户端管理查询',123,1,'#','','',1,0,'F','0','0','system:client:query','#',103,1,'2025-08-23 17:33:40',NULL,NULL,''),(1062,'客户端管理新增',123,2,'#','','',1,0,'F','0','0','system:client:add','#',103,1,'2025-08-23 17:33:40',NULL,NULL,''),(1063,'客户端管理修改',123,3,'#','','',1,0,'F','0','0','system:client:edit','#',103,1,'2025-08-23 17:33:40',NULL,NULL,''),(1064,'客户端管理删除',123,4,'#','','',1,0,'F','0','0','system:client:remove','#',103,1,'2025-08-23 17:33:40',NULL,NULL,''),(1065,'客户端管理导出',123,5,'#','','',1,0,'F','0','0','system:client:export','#',103,1,'2025-08-23 17:33:40',NULL,NULL,''),(1500,'测试单表',5,1,'demo','demo/demo/index','',1,0,'C','0','0','demo:demo:list','#',103,1,'2025-08-23 17:33:40',NULL,NULL,'测试单表菜单'),(1501,'测试单表查询',1500,1,'#','','',1,0,'F','0','0','demo:demo:query','#',103,1,'2025-08-23 17:33:40',NULL,NULL,''),(1502,'测试单表新增',1500,2,'#','','',1,0,'F','0','0','demo:demo:add','#',103,1,'2025-08-23 17:33:40',NULL,NULL,''),(1503,'测试单表修改',1500,3,'#','','',1,0,'F','0','0','demo:demo:edit','#',103,1,'2025-08-23 17:33:40',NULL,NULL,''),(1504,'测试单表删除',1500,4,'#','','',1,0,'F','0','0','demo:demo:remove','#',103,1,'2025-08-23 17:33:40',NULL,NULL,''),(1505,'测试单表导出',1500,5,'#','','',1,0,'F','0','0','demo:demo:export','#',103,1,'2025-08-23 17:33:40',NULL,NULL,''),(1506,'测试树表',5,1,'tree','demo/tree/index','',1,0,'C','0','0','demo:tree:list','#',103,1,'2025-08-23 17:33:40',NULL,NULL,'测试树表菜单'),(1507,'测试树表查询',1506,1,'#','','',1,0,'F','0','0','demo:tree:query','#',103,1,'2025-08-23 17:33:40',NULL,NULL,''),(1508,'测试树表新增',1506,2,'#','','',1,0,'F','0','0','demo:tree:add','#',103,1,'2025-08-23 17:33:40',NULL,NULL,''),(1509,'测试树表修改',1506,3,'#','','',1,0,'F','0','0','demo:tree:edit','#',103,1,'2025-08-23 17:33:40',NULL,NULL,''),(1510,'测试树表删除',1506,4,'#','','',1,0,'F','0','0','demo:tree:remove','#',103,1,'2025-08-23 17:33:40',NULL,NULL,''),(1511,'测试树表导出',1506,5,'#','','',1,0,'F','0','0','demo:tree:export','#',103,1,'2025-08-23 17:33:41',NULL,NULL,''),(1600,'文件查询',118,1,'#','','',1,0,'F','0','0','system:oss:query','#',103,1,'2025-08-23 17:33:40',NULL,NULL,''),(1601,'文件上传',118,2,'#','','',1,0,'F','0','0','system:oss:upload','#',103,1,'2025-08-23 17:33:40',NULL,NULL,''),(1602,'文件下载',118,3,'#','','',1,0,'F','0','0','system:oss:download','#',103,1,'2025-08-23 17:33:40',NULL,NULL,''),(1603,'文件删除',118,4,'#','','',1,0,'F','0','0','system:oss:remove','#',103,1,'2025-08-23 17:33:40',NULL,NULL,''),(1606,'租户查询',121,1,'#','','',1,0,'F','0','0','system:tenant:query','#',103,1,'2025-08-23 17:33:40',NULL,NULL,''),(1607,'租户新增',121,2,'#','','',1,0,'F','0','0','system:tenant:add','#',103,1,'2025-08-23 17:33:40',NULL,NULL,''),(1608,'租户修改',121,3,'#','','',1,0,'F','0','0','system:tenant:edit','#',103,1,'2025-08-23 17:33:40',NULL,NULL,''),(1609,'租户删除',121,4,'#','','',1,0,'F','0','0','system:tenant:remove','#',103,1,'2025-08-23 17:33:40',NULL,NULL,''),(1610,'租户导出',121,5,'#','','',1,0,'F','0','0','system:tenant:export','#',103,1,'2025-08-23 17:33:40',NULL,NULL,''),(1611,'租户套餐查询',122,1,'#','','',1,0,'F','0','0','system:tenantPackage:query','#',103,1,'2025-08-23 17:33:40',NULL,NULL,''),(1612,'租户套餐新增',122,2,'#','','',1,0,'F','0','0','system:tenantPackage:add','#',103,1,'2025-08-23 17:33:40',NULL,NULL,''),(1613,'租户套餐修改',122,3,'#','','',1,0,'F','0','0','system:tenantPackage:edit','#',103,1,'2025-08-23 17:33:40',NULL,NULL,''),(1614,'租户套餐删除',122,4,'#','','',1,0,'F','0','0','system:tenantPackage:remove','#',103,1,'2025-08-23 17:33:40',NULL,NULL,''),(1615,'租户套餐导出',122,5,'#','','',1,0,'F','0','0','system:tenantPackage:export','#',103,1,'2025-08-23 17:33:40',NULL,NULL,''),(1620,'配置列表',118,5,'#','','',1,0,'F','0','0','system:ossConfig:list','#',103,1,'2025-08-23 17:33:40',NULL,NULL,''),(1621,'配置添加',118,6,'#','','',1,0,'F','0','0','system:ossConfig:add','#',103,1,'2025-08-23 17:33:40',NULL,NULL,''),(1622,'配置编辑',118,6,'#','','',1,0,'F','0','0','system:ossConfig:edit','#',103,1,'2025-08-23 17:33:40',NULL,NULL,''),(1623,'配置删除',118,6,'#','','',1,0,'F','0','0','system:ossConfig:remove','#',103,1,'2025-08-23 17:33:40',NULL,NULL,'');
/*!40000 ALTER TABLE `sys_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_notice`
--

DROP TABLE IF EXISTS `sys_notice`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_notice` (
                              `notice_id` bigint NOT NULL COMMENT '公告ID',
                              `tenant_id` varchar(20) DEFAULT '000000' COMMENT '租户编号',
                              `notice_title` varchar(50) NOT NULL COMMENT '公告标题',
                              `notice_type` char(1) NOT NULL COMMENT '公告类型（1通知 2公告）',
                              `notice_content` longblob COMMENT '公告内容',
                              `status` char(1) DEFAULT '0' COMMENT '公告状态（0正常 1关闭）',
                              `create_dept` bigint DEFAULT NULL COMMENT '创建部门',
                              `create_by` bigint DEFAULT NULL COMMENT '创建者',
                              `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                              `update_by` bigint DEFAULT NULL COMMENT '更新者',
                              `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                              `remark` varchar(255) DEFAULT NULL COMMENT '备注',
                              PRIMARY KEY (`notice_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='通知公告表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_notice`
--

LOCK TABLES `sys_notice` WRITE;
/*!40000 ALTER TABLE `sys_notice` DISABLE KEYS */;
INSERT INTO `sys_notice` VALUES (1,'000000','温馨提醒：2018-07-01 新版本发布啦','2',_binary '新版本内容','0',103,1,'2025-08-23 17:33:43',NULL,NULL,'管理员'),(2,'000000','维护通知：2018-07-01 系统凌晨维护','1',_binary '维护内容','0',103,1,'2025-08-23 17:33:43',NULL,NULL,'管理员');
/*!40000 ALTER TABLE `sys_notice` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_oper_log`
--

DROP TABLE IF EXISTS `sys_oper_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_oper_log` (
                                `oper_id` bigint NOT NULL COMMENT '日志主键',
                                `tenant_id` varchar(20) DEFAULT '000000' COMMENT '租户编号',
                                `title` varchar(50) DEFAULT '' COMMENT '模块标题',
                                `business_type` int DEFAULT '0' COMMENT '业务类型（0其它 1新增 2修改 3删除）',
                                `method` varchar(100) DEFAULT '' COMMENT '方法名称',
                                `request_method` varchar(10) DEFAULT '' COMMENT '请求方式',
                                `operator_type` int DEFAULT '0' COMMENT '操作类别（0其它 1后台用户 2手机端用户）',
                                `oper_name` varchar(50) DEFAULT '' COMMENT '操作人员',
                                `dept_name` varchar(50) DEFAULT '' COMMENT '部门名称',
                                `oper_url` varchar(255) DEFAULT '' COMMENT '请求URL',
                                `oper_ip` varchar(128) DEFAULT '' COMMENT '主机地址',
                                `oper_location` varchar(255) DEFAULT '' COMMENT '操作地点',
                                `oper_param` varchar(4000) DEFAULT '' COMMENT '请求参数',
                                `json_result` varchar(4000) DEFAULT '' COMMENT '返回参数',
                                `status` int DEFAULT '0' COMMENT '操作状态（0正常 1异常）',
                                `error_msg` varchar(4000) DEFAULT '' COMMENT '错误消息',
                                `oper_time` datetime DEFAULT NULL COMMENT '操作时间',
                                `cost_time` bigint DEFAULT '0' COMMENT '消耗时间',
                                PRIMARY KEY (`oper_id`),
                                KEY `idx_sys_oper_log_bt` (`business_type`),
                                KEY `idx_sys_oper_log_s` (`status`),
                                KEY `idx_sys_oper_log_ot` (`oper_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='操作日志记录';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_oper_log`
--

LOCK TABLES `sys_oper_log` WRITE;
/*!40000 ALTER TABLE `sys_oper_log` DISABLE KEYS */;
INSERT INTO `sys_oper_log` VALUES (1959188146402267137,'000000','智能排班',1,'org.dromara.hrp.controller.HrpSchedulesController.generateSchedule()','POST',1,'admin','研发部门','/hrp/schedules/generate','0:0:0:0:0:0:0:1','内网IP','{\"createDept\":null,\"createBy\":null,\"createTime\":null,\"updateBy\":null,\"updateTime\":null,\"params\":{\"endDate\":\"2025-08-24\"},\"id\":null,\"userId\":null,\"storeId\":1,\"shiftId\":null,\"skillId\":null,\"scheduleDate\":\"2025-08-18\"}','',1,'\n### Error querying database.  Cause: org.apache.ibatis.binding.BindingException: Parameter \'stroeId\' not found. Available parameters are [storeId, param1]\n### Cause: org.apache.ibatis.binding.BindingException: Parameter \'stroeId\' not found. Available parameters are [storeId, param1]','2025-08-23 17:37:09',60),(1959188283363069953,'000000','智能排班',1,'org.dromara.hrp.controller.HrpSchedulesController.generateSchedule()','POST',1,'admin','研发部门','/hrp/schedules/generate','0:0:0:0:0:0:0:1','内网IP','{\"createDept\":null,\"createBy\":null,\"createTime\":null,\"updateBy\":null,\"updateTime\":null,\"params\":{\"endDate\":\"2025-08-24\"},\"id\":null,\"userId\":null,\"storeId\":1,\"shiftId\":null,\"skillId\":null,\"scheduleDate\":\"2025-08-18\"}','',1,'\n### Error querying database.  Cause: org.apache.ibatis.binding.BindingException: Parameter \'stroeId\' not found. Available parameters are [storeId, param1]\n### Cause: org.apache.ibatis.binding.BindingException: Parameter \'stroeId\' not found. Available parameters are [storeId, param1]','2025-08-23 17:37:41',44),(1959189756201291777,'000000','智能排班',1,'org.dromara.hrp.controller.HrpSchedulesController.generateSchedule()','POST',1,'admin','研发部门','/hrp/schedules/generate','0:0:0:0:0:0:0:1','内网IP','{\"createDept\":null,\"createBy\":null,\"createTime\":null,\"updateBy\":null,\"updateTime\":null,\"params\":{\"endDate\":\"2025-08-24\"},\"id\":null,\"userId\":null,\"storeId\":1,\"shiftId\":null,\"skillId\":null,\"scheduleDate\":\"2025-08-18\"}','',1,'\n### Error querying database.  Cause: org.apache.ibatis.binding.BindingException: Parameter \'stroeId\' not found. Available parameters are [storeId, param1]\n### Cause: org.apache.ibatis.binding.BindingException: Parameter \'stroeId\' not found. Available parameters are [storeId, param1]','2025-08-23 17:43:33',59),(1959190266534842370,'000000','智能排班',1,'org.dromara.hrp.controller.HrpSchedulesController.generateSchedule()','POST',1,'admin','研发部门','/hrp/schedules/generate','0:0:0:0:0:0:0:1','内网IP','{\"createDept\":null,\"createBy\":null,\"createTime\":null,\"updateBy\":null,\"updateTime\":null,\"params\":{\"endDate\":\"2025-08-24\"},\"id\":null,\"userId\":null,\"storeId\":1,\"shiftId\":null,\"skillId\":null,\"scheduleDate\":\"2025-08-18\"}','',1,'\n### Error querying database.  Cause: org.apache.ibatis.binding.BindingException: Parameter \'stroeId\' not found. Available parameters are [storeId, param1]\n### Cause: org.apache.ibatis.binding.BindingException: Parameter \'stroeId\' not found. Available parameters are [storeId, param1]','2025-08-23 17:45:34',41224),(1959190869659041793,'000000','智能排班',1,'org.dromara.hrp.controller.HrpSchedulesController.generateSchedule()','POST',1,'admin','研发部门','/hrp/schedules/generate','0:0:0:0:0:0:0:1','内网IP','{\"createDept\":null,\"createBy\":null,\"createTime\":null,\"updateBy\":null,\"updateTime\":null,\"params\":{\"endDate\":\"2025-08-24\"},\"id\":null,\"userId\":null,\"storeId\":1,\"shiftId\":null,\"skillId\":null,\"scheduleDate\":\"2025-08-18\"}','{\"code\":200,\"msg\":\"智能排班任务已启动\",\"data\":null}',0,'','2025-08-23 17:47:58',13425),(1959191544203788289,'000000','智能排班',1,'org.dromara.hrp.controller.HrpSchedulesController.generateSchedule()','POST',1,'admin','研发部门','/hrp/schedules/generate','0:0:0:0:0:0:0:1','内网IP','{\"createDept\":null,\"createBy\":null,\"createTime\":null,\"updateBy\":null,\"updateTime\":null,\"params\":{\"endDate\":\"2025-08-24\"},\"id\":null,\"userId\":null,\"storeId\":1,\"shiftId\":null,\"skillId\":null,\"scheduleDate\":\"2025-08-18\"}','{\"code\":200,\"msg\":\"智能排班任务已启动\",\"data\":null}',0,'','2025-08-23 17:50:39',85615),(1959192528531415041,'000000','智能排班',1,'org.dromara.hrp.controller.HrpSchedulesController.generateSchedule()','POST',1,'admin','研发部门','/hrp/schedules/generate','0:0:0:0:0:0:0:1','内网IP','{\"createDept\":null,\"createBy\":null,\"createTime\":null,\"updateBy\":null,\"updateTime\":null,\"params\":{\"endDate\":\"2025-08-24\"},\"id\":null,\"userId\":null,\"storeId\":1,\"shiftId\":null,\"skillId\":null,\"scheduleDate\":\"2025-08-18\"}','{\"code\":200,\"msg\":\"智能排班任务已启动\",\"data\":null}',0,'','2025-08-23 17:54:33',138536),(1959193670124494849,'000000','智能排班',1,'org.dromara.hrp.controller.HrpSchedulesController.generateSchedule()','POST',1,'admin','研发部门','/hrp/schedules/generate','0:0:0:0:0:0:0:1','内网IP','{\"createDept\":null,\"createBy\":null,\"createTime\":null,\"updateBy\":null,\"updateTime\":null,\"params\":{\"endDate\":\"2025-08-24\"},\"id\":null,\"userId\":null,\"storeId\":1,\"shiftId\":null,\"skillId\":null,\"scheduleDate\":\"2025-08-18\"}','{\"code\":200,\"msg\":\"智能排班任务已启动\",\"data\":null}',0,'','2025-08-23 17:59:06',177291),(1959196459072413697,'000000','智能排班',1,'org.dromara.hrp.controller.HrpSchedulesController.generateSchedule()','POST',1,'admin','研发部门','/hrp/schedules/generate','0:0:0:0:0:0:0:1','内网IP','{\"createDept\":null,\"createBy\":null,\"createTime\":null,\"updateBy\":null,\"updateTime\":null,\"params\":{\"endDate\":\"2025-08-24\"},\"id\":null,\"userId\":null,\"storeId\":1,\"shiftId\":null,\"skillId\":null,\"scheduleDate\":\"2025-08-18\"}','',1,'\n### Error updating database.  Cause: com.mysql.cj.jdbc.exceptions.MysqlDataTruncation: Data truncation: Out of range value for column \'version\' at row 1\n### The error may exist in file [/Users/thesong/Desktop/project/20_vben/04-project/01_tengjia/01_multipleBranches_SaaS/server/ruoyi-modules/ruoyi-hrp/target/classes/mapper/hrp/HrpSchedulesMapper.xml]\n### The error may involve defaultParameterMap\n### The error occurred while setting parameters\n### SQL: INSERT INTO hrp_schedules (user_id, store_id, shift_id, skill_id, schedule_date, version, tenant_id) VALUES (?, ?, ?, ?, ?, ?, \'000000\'), (?, ?, ?, ?, ?, ?, \'000000\'), (?, ?, ?, ?, ?, ?, \'000000\'), (?, ?, ?, ?, ?, ?, \'000000\'), (?, ?, ?, ?, ?, ?, \'000000\'), (?, ?, ?, ?, ?, ?, \'000000\'), (?, ?, ?, ?, ?, ?, \'000000\'), (?, ?, ?, ?, ?, ?, \'000000\'), (?, ?, ?, ?, ?, ?, \'000000\'), (?, ?, ?, ?, ?, ?, \'000000\'), (?, ?, ?, ?, ?, ?, \'000000\'), (?, ?, ?, ?, ?, ?, \'000000\')\n### Cause: com.mysql.cj.jdbc.exceptions.MysqlDataTruncation: Data truncation: Out of range value for column \'version\' at row 1\n; Data truncation: Out of range value for column \'version\' at row 1','2025-08-23 18:10:11',598),(1959197762737643522,'000000','智能排班',1,'org.dromara.hrp.controller.HrpSchedulesController.generateSchedule()','POST',1,'admin','研发部门','/hrp/schedules/generate','0:0:0:0:0:0:0:1','内网IP','{\"createDept\":null,\"createBy\":null,\"createTime\":null,\"updateBy\":null,\"updateTime\":null,\"params\":{\"endDate\":\"2025-08-24\"},\"id\":null,\"userId\":null,\"storeId\":1,\"shiftId\":null,\"skillId\":null,\"scheduleDate\":\"2025-08-18\"}','{\"code\":200,\"msg\":\"智能排班任务已启动\",\"data\":null}',0,'','2025-08-23 18:15:21',116);
/*!40000 ALTER TABLE `sys_oper_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_oss`
--

DROP TABLE IF EXISTS `sys_oss`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_oss` (
                           `oss_id` bigint NOT NULL COMMENT '对象存储主键',
                           `tenant_id` varchar(20) DEFAULT '000000' COMMENT '租户编号',
                           `file_name` varchar(255) NOT NULL DEFAULT '' COMMENT '文件名',
                           `original_name` varchar(255) NOT NULL DEFAULT '' COMMENT '原名',
                           `file_suffix` varchar(10) NOT NULL DEFAULT '' COMMENT '文件后缀名',
                           `url` varchar(500) NOT NULL COMMENT 'URL地址',
                           `ext1` text COMMENT '扩展字段',
                           `create_dept` bigint DEFAULT NULL COMMENT '创建部门',
                           `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                           `create_by` bigint DEFAULT NULL COMMENT '上传人',
                           `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                           `update_by` bigint DEFAULT NULL COMMENT '更新人',
                           `service` varchar(20) NOT NULL DEFAULT 'minio' COMMENT '服务商',
                           PRIMARY KEY (`oss_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='OSS对象存储表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_oss`
--

LOCK TABLES `sys_oss` WRITE;
/*!40000 ALTER TABLE `sys_oss` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_oss` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_oss_config`
--

DROP TABLE IF EXISTS `sys_oss_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_oss_config` (
                                  `oss_config_id` bigint NOT NULL COMMENT '主键',
                                  `tenant_id` varchar(20) DEFAULT '000000' COMMENT '租户编号',
                                  `config_key` varchar(20) NOT NULL DEFAULT '' COMMENT '配置key',
                                  `access_key` varchar(255) DEFAULT '' COMMENT 'accessKey',
                                  `secret_key` varchar(255) DEFAULT '' COMMENT '秘钥',
                                  `bucket_name` varchar(255) DEFAULT '' COMMENT '桶名称',
                                  `prefix` varchar(255) DEFAULT '' COMMENT '前缀',
                                  `endpoint` varchar(255) DEFAULT '' COMMENT '访问站点',
                                  `domain` varchar(255) DEFAULT '' COMMENT '自定义域名',
                                  `is_https` char(1) DEFAULT 'N' COMMENT '是否https（Y=是,N=否）',
                                  `region` varchar(255) DEFAULT '' COMMENT '域',
                                  `access_policy` char(1) NOT NULL DEFAULT '1' COMMENT '桶权限类型(0=private 1=public 2=custom)',
                                  `status` char(1) DEFAULT '1' COMMENT '是否默认（0=是,1=否）',
                                  `ext1` varchar(255) DEFAULT '' COMMENT '扩展字段',
                                  `create_dept` bigint DEFAULT NULL COMMENT '创建部门',
                                  `create_by` bigint DEFAULT NULL COMMENT '创建者',
                                  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                  `update_by` bigint DEFAULT NULL COMMENT '更新者',
                                  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
                                  PRIMARY KEY (`oss_config_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='对象存储配置表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_oss_config`
--

LOCK TABLES `sys_oss_config` WRITE;
/*!40000 ALTER TABLE `sys_oss_config` DISABLE KEYS */;
INSERT INTO `sys_oss_config` VALUES (1,'000000','minio','ruoyi','ruoyi123','ruoyi','','127.0.0.1:9000','','N','','1','0','',103,1,'2025-08-23 17:33:43',1,'2025-08-23 17:33:43',NULL),(2,'000000','qiniu','XXXXXXXXXXXXXXX','XXXXXXXXXXXXXXX','ruoyi','','s3-cn-north-1.qiniucs.com','','N','','1','1','',103,1,'2025-08-23 17:33:43',1,'2025-08-23 17:33:43',NULL),(3,'000000','aliyun','XXXXXXXXXXXXXXX','XXXXXXXXXXXXXXX','ruoyi','','oss-cn-beijing.aliyuncs.com','','N','','1','1','',103,1,'2025-08-23 17:33:43',1,'2025-08-23 17:33:43',NULL),(4,'000000','qcloud','XXXXXXXXXXXXXXX','XXXXXXXXXXXXXXX','ruoyi-1240000000','','cos.ap-beijing.myqcloud.com','','N','ap-beijing','1','1','',103,1,'2025-08-23 17:33:43',1,'2025-08-23 17:33:43',NULL),(5,'000000','image','ruoyi','ruoyi123','ruoyi','image','127.0.0.1:9000','','N','','1','1','',103,1,'2025-08-23 17:33:43',1,'2025-08-23 17:33:43',NULL);
/*!40000 ALTER TABLE `sys_oss_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_post`
--

DROP TABLE IF EXISTS `sys_post`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_post` (
                            `post_id` bigint NOT NULL COMMENT '岗位ID',
                            `tenant_id` varchar(20) DEFAULT '000000' COMMENT '租户编号',
                            `dept_id` bigint NOT NULL COMMENT '部门id',
                            `post_code` varchar(64) NOT NULL COMMENT '岗位编码',
                            `post_category` varchar(100) DEFAULT NULL COMMENT '岗位类别编码',
                            `post_name` varchar(50) NOT NULL COMMENT '岗位名称',
                            `post_sort` int NOT NULL COMMENT '显示顺序',
                            `status` char(1) NOT NULL COMMENT '状态（0正常 1停用）',
                            `create_dept` bigint DEFAULT NULL COMMENT '创建部门',
                            `create_by` bigint DEFAULT NULL COMMENT '创建者',
                            `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                            `update_by` bigint DEFAULT NULL COMMENT '更新者',
                            `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                            `remark` varchar(500) DEFAULT NULL COMMENT '备注',
                            PRIMARY KEY (`post_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='岗位信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_post`
--

LOCK TABLES `sys_post` WRITE;
/*!40000 ALTER TABLE `sys_post` DISABLE KEYS */;
INSERT INTO `sys_post` VALUES (1,'000000',103,'ceo',NULL,'董事长',1,'0',103,1,'2025-08-23 17:33:38',NULL,NULL,''),(2,'000000',100,'se',NULL,'项目经理',2,'0',103,1,'2025-08-23 17:33:38',NULL,NULL,''),(3,'000000',100,'hr',NULL,'人力资源',3,'0',103,1,'2025-08-23 17:33:38',NULL,NULL,''),(4,'000000',100,'user',NULL,'普通员工',4,'0',103,1,'2025-08-23 17:33:38',NULL,NULL,'');
/*!40000 ALTER TABLE `sys_post` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_role`
--

DROP TABLE IF EXISTS `sys_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_role` (
                            `role_id` bigint NOT NULL COMMENT '角色ID',
                            `tenant_id` varchar(20) DEFAULT '000000' COMMENT '租户编号',
                            `role_name` varchar(30) NOT NULL COMMENT '角色名称',
                            `role_key` varchar(100) NOT NULL COMMENT '角色权限字符串',
                            `role_sort` int NOT NULL COMMENT '显示顺序',
                            `data_scope` char(1) DEFAULT '1' COMMENT '数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限 5：仅本人数据权限 6：部门及以下或本人数据权限）',
                            `menu_check_strictly` tinyint(1) DEFAULT '1' COMMENT '菜单树选择项是否关联显示',
                            `dept_check_strictly` tinyint(1) DEFAULT '1' COMMENT '部门树选择项是否关联显示',
                            `status` char(1) NOT NULL COMMENT '角色状态（0正常 1停用）',
                            `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 1代表删除）',
                            `create_dept` bigint DEFAULT NULL COMMENT '创建部门',
                            `create_by` bigint DEFAULT NULL COMMENT '创建者',
                            `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                            `update_by` bigint DEFAULT NULL COMMENT '更新者',
                            `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                            `remark` varchar(500) DEFAULT NULL COMMENT '备注',
                            PRIMARY KEY (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role`
--

LOCK TABLES `sys_role` WRITE;
/*!40000 ALTER TABLE `sys_role` DISABLE KEYS */;
INSERT INTO `sys_role` VALUES (1,'000000','超级管理员','superadmin',1,'1',1,1,'0','0',103,1,'2025-08-23 17:33:38',NULL,NULL,'超级管理员'),(3,'000000','本部门及以下','test1',3,'4',1,1,'0','0',103,1,'2025-08-23 17:33:38',NULL,NULL,''),(4,'000000','仅本人','test2',4,'5',1,1,'0','0',103,1,'2025-08-23 17:33:39',NULL,NULL,'');
/*!40000 ALTER TABLE `sys_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_role_dept`
--

DROP TABLE IF EXISTS `sys_role_dept`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_role_dept` (
                                 `role_id` bigint NOT NULL COMMENT '角色ID',
                                 `dept_id` bigint NOT NULL COMMENT '部门ID',
                                 PRIMARY KEY (`role_id`,`dept_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色和部门关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role_dept`
--

LOCK TABLES `sys_role_dept` WRITE;
/*!40000 ALTER TABLE `sys_role_dept` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_role_dept` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_role_menu`
--

DROP TABLE IF EXISTS `sys_role_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_role_menu` (
                                 `role_id` bigint NOT NULL COMMENT '角色ID',
                                 `menu_id` bigint NOT NULL COMMENT '菜单ID',
                                 PRIMARY KEY (`role_id`,`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色和菜单关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role_menu`
--

LOCK TABLES `sys_role_menu` WRITE;
/*!40000 ALTER TABLE `sys_role_menu` DISABLE KEYS */;
INSERT INTO `sys_role_menu` VALUES (3,1),(3,5),(3,100),(3,101),(3,102),(3,103),(3,104),(3,105),(3,106),(3,107),(3,108),(3,118),(3,123),(3,130),(3,131),(3,132),(3,133),(3,500),(3,501),(3,1001),(3,1002),(3,1003),(3,1004),(3,1005),(3,1006),(3,1007),(3,1008),(3,1009),(3,1010),(3,1011),(3,1012),(3,1013),(3,1014),(3,1015),(3,1016),(3,1017),(3,1018),(3,1019),(3,1020),(3,1021),(3,1022),(3,1023),(3,1024),(3,1025),(3,1026),(3,1027),(3,1028),(3,1029),(3,1030),(3,1031),(3,1032),(3,1033),(3,1034),(3,1035),(3,1036),(3,1037),(3,1038),(3,1039),(3,1040),(3,1041),(3,1042),(3,1043),(3,1044),(3,1045),(3,1050),(3,1061),(3,1062),(3,1063),(3,1064),(3,1065),(3,1500),(3,1501),(3,1502),(3,1503),(3,1504),(3,1505),(3,1506),(3,1507),(3,1508),(3,1509),(3,1510),(3,1511),(3,1600),(3,1601),(3,1602),(3,1603),(3,1620),(3,1621),(3,1622),(3,1623),(3,11616),(3,11618),(3,11619),(3,11622),(3,11623),(3,11629),(3,11632),(3,11633),(3,11638),(3,11639),(3,11640),(3,11641),(3,11642),(3,11643),(3,11701),(4,5),(4,1500),(4,1501),(4,1502),(4,1503),(4,1504),(4,1505),(4,1506),(4,1507),(4,1508),(4,1509),(4,1510),(4,1511);
/*!40000 ALTER TABLE `sys_role_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_social`
--

DROP TABLE IF EXISTS `sys_social`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_social` (
                              `id` bigint NOT NULL COMMENT '主键',
                              `user_id` bigint NOT NULL COMMENT '用户ID',
                              `tenant_id` varchar(20) DEFAULT '000000' COMMENT '租户id',
                              `auth_id` varchar(255) NOT NULL COMMENT '平台+平台唯一id',
                              `source` varchar(255) NOT NULL COMMENT '用户来源',
                              `open_id` varchar(255) DEFAULT NULL COMMENT '平台编号唯一id',
                              `user_name` varchar(30) NOT NULL COMMENT '登录账号',
                              `nick_name` varchar(30) DEFAULT '' COMMENT '用户昵称',
                              `email` varchar(255) DEFAULT '' COMMENT '用户邮箱',
                              `avatar` varchar(500) DEFAULT '' COMMENT '头像地址',
                              `access_token` varchar(2000) NOT NULL COMMENT '用户的授权令牌',
                              `expire_in` int DEFAULT NULL COMMENT '用户的授权令牌的有效期，部分平台可能没有',
                              `refresh_token` varchar(255) DEFAULT NULL COMMENT '刷新令牌，部分平台可能没有',
                              `access_code` varchar(2000) DEFAULT NULL COMMENT '平台的授权信息，部分平台可能没有',
                              `union_id` varchar(255) DEFAULT NULL COMMENT '用户的 unionid',
                              `scope` varchar(255) DEFAULT NULL COMMENT '授予的权限，部分平台可能没有',
                              `token_type` varchar(255) DEFAULT NULL COMMENT '个别平台的授权信息，部分平台可能没有',
                              `id_token` varchar(2000) DEFAULT NULL COMMENT 'id token，部分平台可能没有',
                              `mac_algorithm` varchar(255) DEFAULT NULL COMMENT '小米平台用户的附带属性，部分平台可能没有',
                              `mac_key` varchar(255) DEFAULT NULL COMMENT '小米平台用户的附带属性，部分平台可能没有',
                              `code` varchar(255) DEFAULT NULL COMMENT '用户的授权code，部分平台可能没有',
                              `oauth_token` varchar(255) DEFAULT NULL COMMENT 'Twitter平台用户的附带属性，部分平台可能没有',
                              `oauth_token_secret` varchar(255) DEFAULT NULL COMMENT 'Twitter平台用户的附带属性，部分平台可能没有',
                              `create_dept` bigint DEFAULT NULL COMMENT '创建部门',
                              `create_by` bigint DEFAULT NULL COMMENT '创建者',
                              `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                              `update_by` bigint DEFAULT NULL COMMENT '更新者',
                              `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                              `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 1代表删除）',
                              PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='社会化关系表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_social`
--

LOCK TABLES `sys_social` WRITE;
/*!40000 ALTER TABLE `sys_social` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_social` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_tenant`
--

DROP TABLE IF EXISTS `sys_tenant`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_tenant` (
                              `id` bigint NOT NULL COMMENT 'id',
                              `tenant_id` varchar(20) NOT NULL COMMENT '租户编号',
                              `contact_user_name` varchar(20) DEFAULT NULL COMMENT '联系人',
                              `contact_phone` varchar(20) DEFAULT NULL COMMENT '联系电话',
                              `company_name` varchar(30) DEFAULT NULL COMMENT '企业名称',
                              `license_number` varchar(30) DEFAULT NULL COMMENT '统一社会信用代码',
                              `address` varchar(200) DEFAULT NULL COMMENT '地址',
                              `intro` varchar(200) DEFAULT NULL COMMENT '企业简介',
                              `domain` varchar(200) DEFAULT NULL COMMENT '域名',
                              `remark` varchar(200) DEFAULT NULL COMMENT '备注',
                              `package_id` bigint DEFAULT NULL COMMENT '租户套餐编号',
                              `expire_time` datetime DEFAULT NULL COMMENT '过期时间',
                              `account_count` int DEFAULT '-1' COMMENT '用户数量（-1不限制）',
                              `status` char(1) DEFAULT '0' COMMENT '租户状态（0正常 1停用）',
                              `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 1代表删除）',
                              `create_dept` bigint DEFAULT NULL COMMENT '创建部门',
                              `create_by` bigint DEFAULT NULL COMMENT '创建者',
                              `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                              `update_by` bigint DEFAULT NULL COMMENT '更新者',
                              `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                              PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='租户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_tenant`
--

LOCK TABLES `sys_tenant` WRITE;
/*!40000 ALTER TABLE `sys_tenant` DISABLE KEYS */;
INSERT INTO `sys_tenant` VALUES (1,'000000','管理组','15888888888','XXX有限公司',NULL,NULL,'多租户通用后台管理管理系统',NULL,NULL,NULL,NULL,-1,'0','0',103,1,'2025-08-23 17:33:38',NULL,NULL);
/*!40000 ALTER TABLE `sys_tenant` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_tenant_package`
--

DROP TABLE IF EXISTS `sys_tenant_package`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_tenant_package` (
                                      `package_id` bigint NOT NULL COMMENT '租户套餐id',
                                      `package_name` varchar(20) DEFAULT NULL COMMENT '套餐名称',
                                      `menu_ids` varchar(3000) DEFAULT NULL COMMENT '关联菜单id',
                                      `remark` varchar(200) DEFAULT NULL COMMENT '备注',
                                      `menu_check_strictly` tinyint(1) DEFAULT '1' COMMENT '菜单树选择项是否关联显示',
                                      `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
                                      `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 1代表删除）',
                                      `create_dept` bigint DEFAULT NULL COMMENT '创建部门',
                                      `create_by` bigint DEFAULT NULL COMMENT '创建者',
                                      `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                      `update_by` bigint DEFAULT NULL COMMENT '更新者',
                                      `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                      PRIMARY KEY (`package_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='租户套餐表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_tenant_package`
--

LOCK TABLES `sys_tenant_package` WRITE;
/*!40000 ALTER TABLE `sys_tenant_package` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_tenant_package` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user`
--

DROP TABLE IF EXISTS `sys_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_user` (
                            `user_id` bigint NOT NULL COMMENT '用户ID',
                            `tenant_id` varchar(20) DEFAULT '000000' COMMENT '租户编号',
                            `dept_id` bigint DEFAULT NULL COMMENT '部门ID',
                            `user_name` varchar(30) NOT NULL COMMENT '用户账号',
                            `nick_name` varchar(30) NOT NULL COMMENT '用户昵称',
                            `user_type` varchar(10) DEFAULT 'sys_user' COMMENT '用户类型（sys_user系统用户）',
                            `email` varchar(50) DEFAULT '' COMMENT '用户邮箱',
                            `phonenumber` varchar(11) DEFAULT '' COMMENT '手机号码',
                            `sex` char(1) DEFAULT '0' COMMENT '用户性别（0男 1女 2未知）',
                            `avatar` bigint DEFAULT NULL COMMENT '头像地址',
                            `password` varchar(100) DEFAULT '' COMMENT '密码',
                            `status` char(1) DEFAULT '0' COMMENT '帐号状态（0正常 1停用）',
                            `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 1代表删除）',
                            `login_ip` varchar(128) DEFAULT '' COMMENT '最后登录IP',
                            `login_date` datetime DEFAULT NULL COMMENT '最后登录时间',
                            `create_dept` bigint DEFAULT NULL COMMENT '创建部门',
                            `create_by` bigint DEFAULT NULL COMMENT '创建者',
                            `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                            `update_by` bigint DEFAULT NULL COMMENT '更新者',
                            `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                            `remark` varchar(500) DEFAULT NULL COMMENT '备注',
                            PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user`
--

LOCK TABLES `sys_user` WRITE;
/*!40000 ALTER TABLE `sys_user` DISABLE KEYS */;
INSERT INTO `sys_user` VALUES (1,'000000',103,'admin','疯狂的狮子Li','sys_user','crazyLionLi@163.com','15888888888','1',NULL,'$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2','0','0','127.0.0.1','2025-08-23 17:33:38',103,1,'2025-08-23 17:33:38',NULL,NULL,'管理员'),(3,'000000',108,'test','本部门及以下 密码666666','sys_user','','','0',NULL,'$2a$10$b8yUzN0C71sbz.PhNOCgJe.Tu1yWC3RNrTyjSQ8p1W0.aaUXUJ.Ne','0','0','127.0.0.1','2025-08-23 17:33:38',103,1,'2025-08-23 17:33:38',3,'2025-08-23 17:33:38',NULL),(4,'000000',102,'test1','仅本人 密码666666','sys_user','','','0',NULL,'$2a$10$b8yUzN0C71sbz.PhNOCgJe.Tu1yWC3RNrTyjSQ8p1W0.aaUXUJ.Ne','0','0','127.0.0.1','2025-08-23 17:33:38',103,1,'2025-08-23 17:33:38',4,'2025-08-23 17:33:38',NULL),(101,'000000',105,'张三','张三','sys_user','zhangsan@example.com','13900000001','0',NULL,'$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.BtvSoEexvejSoYldBBsU.Ne','0','0','',NULL,NULL,1,'2025-08-23 17:35:02',1,'2025-08-23 17:35:02',NULL),(102,'000000',105,'李四','李四','sys_user','lisi@example.com','13900000002','1',NULL,'$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.BtvSoEexvejSoYldBBsU.Ne','0','0','',NULL,NULL,1,'2025-08-23 17:35:02',1,'2025-08-23 17:35:02',NULL),(103,'000000',105,'王五','王五','sys_user','wangwu@example.com','13900000003','0',NULL,'$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.BtvSoEexvejSoYldBBsU.Ne','0','0','',NULL,NULL,1,'2025-08-23 17:35:02',1,'2025-08-23 17:35:02',NULL),(104,'000000',105,'赵六','赵六','sys_user','zhaoliu@example.com','13900000004','1',NULL,'$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.BtvSoEexvejSoYldBBsU.Ne','0','0','',NULL,NULL,1,'2025-08-23 17:35:02',1,'2025-08-23 17:35:02',NULL),(105,'000000',105,'孙七','孙七','sys_user','sunqi@example.com','13900000005','0',NULL,'$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.BtvSoEexvejSoYldBBsU.Ne','0','0','',NULL,NULL,1,'2025-08-23 17:35:02',1,'2025-08-23 17:35:02',NULL);
/*!40000 ALTER TABLE `sys_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user_post`
--

DROP TABLE IF EXISTS `sys_user_post`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_user_post` (
                                 `user_id` bigint NOT NULL COMMENT '用户ID',
                                 `post_id` bigint NOT NULL COMMENT '岗位ID',
                                 PRIMARY KEY (`user_id`,`post_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户与岗位关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user_post`
--

LOCK TABLES `sys_user_post` WRITE;
/*!40000 ALTER TABLE `sys_user_post` DISABLE KEYS */;
INSERT INTO `sys_user_post` VALUES (1,1);
/*!40000 ALTER TABLE `sys_user_post` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user_role`
--

DROP TABLE IF EXISTS `sys_user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_user_role` (
                                 `user_id` bigint NOT NULL COMMENT '用户ID',
                                 `role_id` bigint NOT NULL COMMENT '角色ID',
                                 PRIMARY KEY (`user_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户和角色关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user_role`
--

LOCK TABLES `sys_user_role` WRITE;
/*!40000 ALTER TABLE `sys_user_role` DISABLE KEYS */;
INSERT INTO `sys_user_role` VALUES (1,1),(3,3),(4,4);
/*!40000 ALTER TABLE `sys_user_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `test_demo`
--

DROP TABLE IF EXISTS `test_demo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `test_demo` (
                             `id` bigint NOT NULL COMMENT '主键',
                             `tenant_id` varchar(20) DEFAULT '000000' COMMENT '租户编号',
                             `dept_id` bigint DEFAULT NULL COMMENT '部门id',
                             `user_id` bigint DEFAULT NULL COMMENT '用户id',
                             `order_num` int DEFAULT '0' COMMENT '排序号',
                             `test_key` varchar(255) DEFAULT NULL COMMENT 'key键',
                             `value` varchar(255) DEFAULT NULL COMMENT '值',
                             `version` int DEFAULT '0' COMMENT '版本',
                             `create_dept` bigint DEFAULT NULL COMMENT '创建部门',
                             `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                             `create_by` bigint DEFAULT NULL COMMENT '创建人',
                             `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                             `update_by` bigint DEFAULT NULL COMMENT '更新人',
                             `del_flag` int DEFAULT '0' COMMENT '删除标志',
                             PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='测试单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `test_demo`
--

LOCK TABLES `test_demo` WRITE;
/*!40000 ALTER TABLE `test_demo` DISABLE KEYS */;
INSERT INTO `test_demo` VALUES (1,'000000',102,4,1,'测试数据权限','测试',0,103,'2025-08-23 17:33:44',1,NULL,NULL,0),(2,'000000',102,3,2,'子节点1','111',0,103,'2025-08-23 17:33:44',1,NULL,NULL,0),(3,'000000',102,3,3,'子节点2','222',0,103,'2025-08-23 17:33:44',1,NULL,NULL,0),(4,'000000',108,4,4,'测试数据','demo',0,103,'2025-08-23 17:33:44',1,NULL,NULL,0),(5,'000000',108,3,13,'子节点11','1111',0,103,'2025-08-23 17:33:44',1,NULL,NULL,0),(6,'000000',108,3,12,'子节点22','2222',0,103,'2025-08-23 17:33:44',1,NULL,NULL,0),(7,'000000',108,3,11,'子节点33','3333',0,103,'2025-08-23 17:33:44',1,NULL,NULL,0),(8,'000000',108,3,10,'子节点44','4444',0,103,'2025-08-23 17:33:44',1,NULL,NULL,0),(9,'000000',108,3,9,'子节点55','5555',0,103,'2025-08-23 17:33:44',1,NULL,NULL,0),(10,'000000',108,3,8,'子节点66','6666',0,103,'2025-08-23 17:33:44',1,NULL,NULL,0),(11,'000000',108,3,7,'子节点77','7777',0,103,'2025-08-23 17:33:44',1,NULL,NULL,0),(12,'000000',108,3,6,'子节点88','8888',0,103,'2025-08-23 17:33:44',1,NULL,NULL,0),(13,'000000',108,3,5,'子节点99','9999',0,103,'2025-08-23 17:33:44',1,NULL,NULL,0);
/*!40000 ALTER TABLE `test_demo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `test_leave`
--

DROP TABLE IF EXISTS `test_leave`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `test_leave` (
                              `id` bigint NOT NULL COMMENT 'id',
                              `tenant_id` varchar(20) DEFAULT '000000' COMMENT '租户编号',
                              `leave_type` varchar(255) NOT NULL COMMENT '请假类型',
                              `start_date` datetime NOT NULL COMMENT '开始时间',
                              `end_date` datetime NOT NULL COMMENT '结束时间',
                              `leave_days` int NOT NULL COMMENT '请假天数',
                              `remark` varchar(255) DEFAULT NULL COMMENT '请假原因',
                              `status` varchar(255) DEFAULT NULL COMMENT '状态',
                              `create_dept` bigint DEFAULT NULL COMMENT '创建部门',
                              `create_by` bigint DEFAULT NULL COMMENT '创建者',
                              `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                              `update_by` bigint DEFAULT NULL COMMENT '更新者',
                              `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                              PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='请假申请表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `test_leave`
--

LOCK TABLES `test_leave` WRITE;
/*!40000 ALTER TABLE `test_leave` DISABLE KEYS */;
/*!40000 ALTER TABLE `test_leave` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `test_tree`
--

DROP TABLE IF EXISTS `test_tree`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `test_tree` (
                             `id` bigint NOT NULL COMMENT '主键',
                             `tenant_id` varchar(20) DEFAULT '000000' COMMENT '租户编号',
                             `parent_id` bigint DEFAULT '0' COMMENT '父id',
                             `dept_id` bigint DEFAULT NULL COMMENT '部门id',
                             `user_id` bigint DEFAULT NULL COMMENT '用户id',
                             `tree_name` varchar(255) DEFAULT NULL COMMENT '值',
                             `version` int DEFAULT '0' COMMENT '版本',
                             `create_dept` bigint DEFAULT NULL COMMENT '创建部门',
                             `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                             `create_by` bigint DEFAULT NULL COMMENT '创建人',
                             `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                             `update_by` bigint DEFAULT NULL COMMENT '更新人',
                             `del_flag` int DEFAULT '0' COMMENT '删除标志',
                             PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='测试树表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `test_tree`
--

LOCK TABLES `test_tree` WRITE;
/*!40000 ALTER TABLE `test_tree` DISABLE KEYS */;
INSERT INTO `test_tree` VALUES (1,'000000',0,102,4,'测试数据权限',0,103,'2025-08-23 17:33:44',1,NULL,NULL,0),(2,'000000',1,102,3,'子节点1',0,103,'2025-08-23 17:33:44',1,NULL,NULL,0),(3,'000000',2,102,3,'子节点2',0,103,'2025-08-23 17:33:44',1,NULL,NULL,0),(4,'000000',0,108,4,'测试树1',0,103,'2025-08-23 17:33:44',1,NULL,NULL,0),(5,'000000',4,108,3,'子节点11',0,103,'2025-08-23 17:33:44',1,NULL,NULL,0),(6,'000000',4,108,3,'子节点22',0,103,'2025-08-23 17:33:44',1,NULL,NULL,0),(7,'000000',4,108,3,'子节点33',0,103,'2025-08-23 17:33:44',1,NULL,NULL,0),(8,'000000',5,108,3,'子节点44',0,103,'2025-08-23 17:33:44',1,NULL,NULL,0),(9,'000000',6,108,3,'子节点55',0,103,'2025-08-23 17:33:44',1,NULL,NULL,0),(10,'000000',7,108,3,'子节点66',0,103,'2025-08-23 17:33:44',1,NULL,NULL,0),(11,'000000',7,108,3,'子节点77',0,103,'2025-08-23 17:33:44',1,NULL,NULL,0),(12,'000000',10,108,3,'子节点88',0,103,'2025-08-23 17:33:44',1,NULL,NULL,0),(13,'000000',10,108,3,'子节点99',0,103,'2025-08-23 17:33:44',1,NULL,NULL,0);
/*!40000 ALTER TABLE `test_tree` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-08-23 18:18:16
