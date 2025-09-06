package org.dromara.hrp.domain.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * 结构化的排班反馈项 DTO
 * 用于向前端返回详细、可交互的排班问题（如人力缺口、规则冲突等）
 *
 * @Author Gemini AI Assistant
 * @Date 2025-08-27
 * @Version 2.0
 */
@Data
@Builder
public class FeedbackItem implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 反馈类型
     */
    private FeedbackType type;

    /**
     * 严重级别
     */
    private Severity severity;

    /**
     * 详细描述信息
     */
    private String message;

    /**
     * 问题发生的日期
     */
    private LocalDate date;

    /**
     * 关联的班次ID (如果适用)
     */
    private Long shiftId;

    /**
     * 关联的技能/岗位ID (如果适用)
     */
    private Long skillId;

    /**
     * 关联的用户ID列表 (如果适用)
     */
    private List<Long> relatedUserIds;

    /**
     * 反馈类型枚举
     */
    public enum FeedbackType {
        MANPOWER_SHORTAGE,      // 人力缺口
        CONSTRAINT_VIOLATION,   // 规则冲突
        OPTIMIZATION_INFO,
        SYSTEM_WARNING          // 系统警告
    }

    /**
     * 严重级别枚举
     */
    public enum Severity {
        WARNING,                // 警告
        INFO, ERROR                   // 错误（严重问题）
    }
}
