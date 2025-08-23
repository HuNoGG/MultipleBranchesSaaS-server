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
import org.dromara.hrp.domain.vo.HrpLeaveApplicationsVo;
import org.dromara.hrp.domain.bo.HrpLeaveApplicationsBo;
import org.dromara.hrp.service.IHrpLeaveApplicationsService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 正式请假申请
 *
 * @author Lion Li
 * @date 2025-08-23
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/hrp/leaveApplications")
public class HrpLeaveApplicationsController extends BaseController {

    private final IHrpLeaveApplicationsService hrpLeaveApplicationsService;

    /**
     * 查询正式请假申请列表
     */
    @SaCheckPermission("hrp:leaveApplications:list")
    @GetMapping("/list")
    public TableDataInfo<HrpLeaveApplicationsVo> list(HrpLeaveApplicationsBo bo, PageQuery pageQuery) {
        return hrpLeaveApplicationsService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出正式请假申请列表
     */
    @SaCheckPermission("hrp:leaveApplications:export")
    @Log(title = "正式请假申请", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HrpLeaveApplicationsBo bo, HttpServletResponse response) {
        List<HrpLeaveApplicationsVo> list = hrpLeaveApplicationsService.queryList(bo);
        ExcelUtil.exportExcel(list, "正式请假申请", HrpLeaveApplicationsVo.class, response);
    }

    /**
     * 获取正式请假申请详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("hrp:leaveApplications:query")
    @GetMapping("/{id}")
    public R<HrpLeaveApplicationsVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(hrpLeaveApplicationsService.queryById(id));
    }

    /**
     * 新增正式请假申请
     */
    @SaCheckPermission("hrp:leaveApplications:add")
    @Log(title = "正式请假申请", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody HrpLeaveApplicationsBo bo) {
        return toAjax(hrpLeaveApplicationsService.insertByBo(bo));
    }

    /**
     * 修改正式请假申请
     */
    @SaCheckPermission("hrp:leaveApplications:edit")
    @Log(title = "正式请假申请", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody HrpLeaveApplicationsBo bo) {
        return toAjax(hrpLeaveApplicationsService.updateByBo(bo));
    }

    /**
     * 删除正式请假申请
     *
     * @param ids 主键串
     */
    @SaCheckPermission("hrp:leaveApplications:remove")
    @Log(title = "正式请假申请", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(hrpLeaveApplicationsService.deleteWithValidByIds(List.of(ids), true));
    }
}
