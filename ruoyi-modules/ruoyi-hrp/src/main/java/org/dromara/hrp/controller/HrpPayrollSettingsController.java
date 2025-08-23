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
import org.dromara.hrp.domain.vo.HrpPayrollSettingsVo;
import org.dromara.hrp.domain.bo.HrpPayrollSettingsBo;
import org.dromara.hrp.service.IHrpPayrollSettingsService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 薪资规则
 *
 * @author Lion Li
 * @date 2025-08-23
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/hrp/payrollSettings")
public class HrpPayrollSettingsController extends BaseController {

    private final IHrpPayrollSettingsService hrpPayrollSettingsService;

    /**
     * 查询薪资规则列表
     */
    @SaCheckPermission("hrp:payrollSettings:list")
    @GetMapping("/list")
    public TableDataInfo<HrpPayrollSettingsVo> list(HrpPayrollSettingsBo bo, PageQuery pageQuery) {
        return hrpPayrollSettingsService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出薪资规则列表
     */
    @SaCheckPermission("hrp:payrollSettings:export")
    @Log(title = "薪资规则", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HrpPayrollSettingsBo bo, HttpServletResponse response) {
        List<HrpPayrollSettingsVo> list = hrpPayrollSettingsService.queryList(bo);
        ExcelUtil.exportExcel(list, "薪资规则", HrpPayrollSettingsVo.class, response);
    }

    /**
     * 获取薪资规则详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("hrp:payrollSettings:query")
    @GetMapping("/{id}")
    public R<HrpPayrollSettingsVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(hrpPayrollSettingsService.queryById(id));
    }

    /**
     * 新增薪资规则
     */
    @SaCheckPermission("hrp:payrollSettings:add")
    @Log(title = "薪资规则", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody HrpPayrollSettingsBo bo) {
        return toAjax(hrpPayrollSettingsService.insertByBo(bo));
    }

    /**
     * 修改薪资规则
     */
    @SaCheckPermission("hrp:payrollSettings:edit")
    @Log(title = "薪资规则", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody HrpPayrollSettingsBo bo) {
        return toAjax(hrpPayrollSettingsService.updateByBo(bo));
    }

    /**
     * 删除薪资规则
     *
     * @param ids 主键串
     */
    @SaCheckPermission("hrp:payrollSettings:remove")
    @Log(title = "薪资规则", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(hrpPayrollSettingsService.deleteWithValidByIds(List.of(ids), true));
    }
}
