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
import org.dromara.hrp.domain.vo.HrpShiftsVo;
import org.dromara.hrp.domain.bo.HrpShiftsBo;
import org.dromara.hrp.service.IHrpShiftsService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 班别设定
 *
 * @author Lion Li
 * @date 2025-08-23
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/hrp/shifts")
public class HrpShiftsController extends BaseController {

    private final IHrpShiftsService hrpShiftsService;

    /**
     * 查询班别设定列表
     */
    @SaCheckPermission("hrp:shifts:list")
    @GetMapping("/list")
    public TableDataInfo<HrpShiftsVo> list(HrpShiftsBo bo, PageQuery pageQuery) {
        return hrpShiftsService.queryPageList(bo, pageQuery);
    }


    /**
     * 查询当前班次设定与休息时间
     */
    @SaCheckPermission("hrp:shifts:list")
    @GetMapping("/shiftsAndRestTime")
    public R<List<HrpShiftsVo>> shiftsAndRestTime(HrpShiftsBo bo) {
        return R.ok(hrpShiftsService.queryShiftsAndRestTime(bo));
    }

    /**
     * 导出班别设定列表
     */
    @SaCheckPermission("hrp:shifts:export")
    @Log(title = "班别设定", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HrpShiftsBo bo, HttpServletResponse response) {
        List<HrpShiftsVo> list = hrpShiftsService.queryList(bo);
        ExcelUtil.exportExcel(list, "班别设定", HrpShiftsVo.class, response);
    }

    /**
     * 获取班别设定详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("hrp:shifts:query")
    @GetMapping("/{id}")
    public R<HrpShiftsVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(hrpShiftsService.queryById(id));
    }

    /**
     * 新增班别设定
     */
    @SaCheckPermission("hrp:shifts:add")
    @Log(title = "班别设定", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody HrpShiftsBo bo) {
        return toAjax(hrpShiftsService.insertByBo(bo));
    }

    /**
     * 修改班别设定
     */
    @SaCheckPermission("hrp:shifts:edit")
    @Log(title = "班别设定", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody HrpShiftsBo bo) {
        return toAjax(hrpShiftsService.updateByBo(bo));
    }

    /**
     * 删除班别设定
     *
     * @param ids 主键串
     */
    @SaCheckPermission("hrp:shifts:remove")
    @Log(title = "班别设定", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(hrpShiftsService.deleteWithValidByIds(List.of(ids), true));
    }
}
