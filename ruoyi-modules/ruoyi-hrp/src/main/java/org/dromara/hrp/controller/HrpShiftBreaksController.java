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
import org.dromara.hrp.domain.vo.HrpShiftBreaksVo;
import org.dromara.hrp.domain.bo.HrpShiftBreaksBo;
import org.dromara.hrp.service.IHrpShiftBreaksService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 班别休息时段
 *
 * @author Lion Li
 * @date 2025-08-23
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/hrp/shiftBreaks")
public class HrpShiftBreaksController extends BaseController {

    private final IHrpShiftBreaksService hrpShiftBreaksService;

    /**
     * 查询班别休息时段列表
     */
    @SaCheckPermission("hrp:shiftBreaks:list")
    @GetMapping("/list")
    public TableDataInfo<HrpShiftBreaksVo> list(HrpShiftBreaksBo bo, PageQuery pageQuery) {
        return hrpShiftBreaksService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出班别休息时段列表
     */
    @SaCheckPermission("hrp:shiftBreaks:export")
    @Log(title = "班别休息时段", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HrpShiftBreaksBo bo, HttpServletResponse response) {
        List<HrpShiftBreaksVo> list = hrpShiftBreaksService.queryList(bo);
        ExcelUtil.exportExcel(list, "班别休息时段", HrpShiftBreaksVo.class, response);
    }

    /**
     * 获取班别休息时段详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("hrp:shiftBreaks:query")
    @GetMapping("/{id}")
    public R<HrpShiftBreaksVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(hrpShiftBreaksService.queryById(id));
    }

    /**
     * 新增班别休息时段
     */
    @SaCheckPermission("hrp:shiftBreaks:add")
    @Log(title = "班别休息时段", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody HrpShiftBreaksBo bo) {
        return toAjax(hrpShiftBreaksService.insertByBo(bo));
    }

    /**
     * 修改班别休息时段
     */
    @SaCheckPermission("hrp:shiftBreaks:edit")
    @Log(title = "班别休息时段", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody HrpShiftBreaksBo bo) {
        return toAjax(hrpShiftBreaksService.updateByBo(bo));
    }

    /**
     * 删除班别休息时段
     *
     * @param ids 主键串
     */
    @SaCheckPermission("hrp:shiftBreaks:remove")
    @Log(title = "班别休息时段", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(hrpShiftBreaksService.deleteWithValidByIds(List.of(ids), true));
    }
}
