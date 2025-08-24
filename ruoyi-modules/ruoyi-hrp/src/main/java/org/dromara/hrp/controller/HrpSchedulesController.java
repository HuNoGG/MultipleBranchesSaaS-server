package org.dromara.hrp.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.*;
import cn.dev33.satoken.annotation.SaCheckPermission;
import org.dromara.hrp.domain.dto.ScheduleGenerateDto;
import org.dromara.hrp.domain.dto.ScheduleGenerationResult;
import org.dromara.hrp.domain.dto.SchedulePlanDto;
import org.dromara.hrp.domain.dto.WeeklyScheduleDto;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import org.dromara.common.idempotent.annotation.RepeatSubmit;
import org.dromara.common.log.annotation.Log;
import org.dromara.common.web.core.BaseController;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.core.domain.R;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.log.enums.BusinessType;
import org.dromara.common.excel.utils.ExcelUtil;
import org.dromara.hrp.domain.vo.HrpSchedulesVo;
import org.dromara.hrp.domain.bo.HrpSchedulesBo;
import org.dromara.hrp.service.IHrpSchedulesService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 排班
 *
 * @author Lion Li
 * @date 2025-08-23
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/hrp/schedules")
public class HrpSchedulesController extends BaseController {

    private final IHrpSchedulesService hrpSchedulesService;



    /**
     * 获取周度排班数据（智能判断是草稿还是已发布）
     * @param storeId 分店ID
     * @param startDate 开始日期 (格式: yyyy-MM-dd)
     * @param endDate 结束日期 (格式: yyyy-MM-dd)
     * @return 结构化的周度排班数据
     */
    @GetMapping("/week")
    public R<WeeklyScheduleDto> getWeeklySchedule(@RequestParam Long storeId,
        @RequestParam String startDate,
        @RequestParam String endDate) {
        return R.ok(hrpSchedulesService.getWeeklySchedule(storeId, startDate, endDate));
    }

    /**
     * 发布一周的排班计划
     * @param storeId 分店ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 操作结果
     */
    @Log(title = "发布排班", businessType = BusinessType.UPDATE)
    @GetMapping("/publish")
    public R<Void> publishSchedule(@RequestParam Long storeId,
        @RequestParam String startDate,
        @RequestParam String endDate) {
        return toAjax(hrpSchedulesService.publishSchedule(storeId, startDate, endDate));
    }


    /**
     * 查询排班历史列表
     * @param storeId 分店ID
     * @param startDate 开始日期 (格式: yyyy-MM-dd)
     * @param endDate 结束日期 (格式: yyyy-MM-dd)
     * @return 结构化的排班历史数据
     */
    @GetMapping("/history")
    public R<SchedulePlanDto> getScheduleHistory(@RequestParam Long storeId,
        @RequestParam String startDate,
        @RequestParam String endDate) {
        // 复用getSchedulePlan DTO，但service层实现不同，会包含考勤状态
        SchedulePlanDto scheduleHistory = hrpSchedulesService.getScheduleHistory(storeId, startDate, endDate);
        return R.ok(scheduleHistory);
    }

    /**
     * 智能生成排班计划
     * @param dto 包含分店、日期范围、员工、岗位需求的业务对象
     * @return 生成的排班计划草稿
     */
    @Log(title = "智能排班", businessType = BusinessType.INSERT)
    @PostMapping("/generate")
    public R<ScheduleGenerationResult> generateSchedule(@Validated @RequestBody ScheduleGenerateDto dto) {
        ScheduleGenerationResult result = hrpSchedulesService.generateSchedule(dto);
        return R.ok(result);
    }



    /**
     * 查询排班列表
     */
    @SaCheckPermission("hrp:schedules:list")
    @GetMapping("/list")
    public TableDataInfo<HrpSchedulesVo> list(HrpSchedulesBo bo, PageQuery pageQuery) {
        return hrpSchedulesService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出排班列表
     */
    @SaCheckPermission("hrp:schedules:export")
    @Log(title = "排班", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HrpSchedulesBo bo, HttpServletResponse response) {
        List<HrpSchedulesVo> list = hrpSchedulesService.queryList(bo);
        ExcelUtil.exportExcel(list, "排班", HrpSchedulesVo.class, response);
    }

    /**
     * 获取排班详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("hrp:schedules:query")
    @GetMapping("/{id}")
    public R<HrpSchedulesVo> getInfo(@NotNull(message = "主键不能为空")
    @PathVariable Long id) {
        return R.ok(hrpSchedulesService.queryById(id));
    }

    /**
     * 新增排班
     */
    @SaCheckPermission("hrp:schedules:add")
    @Log(title = "排班", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody HrpSchedulesBo bo) {
        return toAjax(hrpSchedulesService.insertByBo(bo));
    }

    /**
     * 修改排班
     */
    @SaCheckPermission("hrp:schedules:edit")
    @Log(title = "排班", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody HrpSchedulesBo bo) {
        return toAjax(hrpSchedulesService.updateByBo(bo));
    }

    /**
     * 删除排班
     *
     * @param ids 主键串
     */
    @SaCheckPermission("hrp:schedules:remove")
    @Log(title = "排班", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
    @PathVariable Long[] ids) {
        return toAjax(hrpSchedulesService.deleteWithValidByIds(List.of(ids), true));
    }
}
