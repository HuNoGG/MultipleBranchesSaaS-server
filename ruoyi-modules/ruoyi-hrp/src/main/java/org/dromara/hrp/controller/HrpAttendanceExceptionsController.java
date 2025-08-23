package org.dromara.hrp.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.*;
import cn.dev33.satoken.annotation.SaCheckPermission;
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
import org.dromara.hrp.domain.vo.HrpAttendanceExceptionsVo;
import org.dromara.hrp.domain.bo.HrpAttendanceExceptionsBo;
import org.dromara.hrp.service.IHrpAttendanceExceptionsService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 考勤异常
 *
 * @author Lion Li
 * @date 2025-08-23
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/hrp/attendanceExceptions")
public class HrpAttendanceExceptionsController extends BaseController {

    private final IHrpAttendanceExceptionsService hrpAttendanceExceptionsService;

    /**
     * 查询考勤异常列表
     */
    @SaCheckPermission("hrp:attendanceExceptions:list")
    @GetMapping("/list")
    public TableDataInfo<HrpAttendanceExceptionsVo> list(HrpAttendanceExceptionsBo bo, PageQuery pageQuery) {
        return hrpAttendanceExceptionsService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出考勤异常列表
     */
    @SaCheckPermission("hrp:attendanceExceptions:export")
    @Log(title = "考勤异常", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HrpAttendanceExceptionsBo bo, HttpServletResponse response) {
        List<HrpAttendanceExceptionsVo> list = hrpAttendanceExceptionsService.queryList(bo);
        ExcelUtil.exportExcel(list, "考勤异常", HrpAttendanceExceptionsVo.class, response);
    }

    /**
     * 获取考勤异常详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("hrp:attendanceExceptions:query")
    @GetMapping("/{id}")
    public R<HrpAttendanceExceptionsVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(hrpAttendanceExceptionsService.queryById(id));
    }

    /**
     * 新增考勤异常
     */
    @SaCheckPermission("hrp:attendanceExceptions:add")
    @Log(title = "考勤异常", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody HrpAttendanceExceptionsBo bo) {
        return toAjax(hrpAttendanceExceptionsService.insertByBo(bo));
    }

    /**
     * 修改考勤异常
     */
    @SaCheckPermission("hrp:attendanceExceptions:edit")
    @Log(title = "考勤异常", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody HrpAttendanceExceptionsBo bo) {
        return toAjax(hrpAttendanceExceptionsService.updateByBo(bo));
    }

    /**
     * 删除考勤异常
     *
     * @param ids 主键串
     */
    @SaCheckPermission("hrp:attendanceExceptions:remove")
    @Log(title = "考勤异常", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(hrpAttendanceExceptionsService.deleteWithValidByIds(List.of(ids), true));
    }
}
