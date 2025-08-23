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
import org.dromara.hrp.domain.vo.HrpPayrollRecordsVo;
import org.dromara.hrp.domain.bo.HrpPayrollRecordsBo;
import org.dromara.hrp.service.IHrpPayrollRecordsService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 薪资单记录
 *
 * @author Lion Li
 * @date 2025-08-23
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/hrp/payrollRecords")
public class HrpPayrollRecordsController extends BaseController {

    private final IHrpPayrollRecordsService hrpPayrollRecordsService;

    /**
     * 查询薪资单记录列表
     */
    @SaCheckPermission("hrp:payrollRecords:list")
    @GetMapping("/list")
    public TableDataInfo<HrpPayrollRecordsVo> list(HrpPayrollRecordsBo bo, PageQuery pageQuery) {
        return hrpPayrollRecordsService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出薪资单记录列表
     */
    @SaCheckPermission("hrp:payrollRecords:export")
    @Log(title = "薪资单记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HrpPayrollRecordsBo bo, HttpServletResponse response) {
        List<HrpPayrollRecordsVo> list = hrpPayrollRecordsService.queryList(bo);
        ExcelUtil.exportExcel(list, "薪资单记录", HrpPayrollRecordsVo.class, response);
    }

    /**
     * 获取薪资单记录详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("hrp:payrollRecords:query")
    @GetMapping("/{id}")
    public R<HrpPayrollRecordsVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(hrpPayrollRecordsService.queryById(id));
    }

    /**
     * 新增薪资单记录
     */
    @SaCheckPermission("hrp:payrollRecords:add")
    @Log(title = "薪资单记录", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody HrpPayrollRecordsBo bo) {
        return toAjax(hrpPayrollRecordsService.insertByBo(bo));
    }

    /**
     * 修改薪资单记录
     */
    @SaCheckPermission("hrp:payrollRecords:edit")
    @Log(title = "薪资单记录", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody HrpPayrollRecordsBo bo) {
        return toAjax(hrpPayrollRecordsService.updateByBo(bo));
    }

    /**
     * 删除薪资单记录
     *
     * @param ids 主键串
     */
    @SaCheckPermission("hrp:payrollRecords:remove")
    @Log(title = "薪资单记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(hrpPayrollRecordsService.deleteWithValidByIds(List.of(ids), true));
    }
}
