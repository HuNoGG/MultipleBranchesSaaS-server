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
import org.dromara.hrp.domain.vo.HrpLeaveRequestsVo;
import org.dromara.hrp.domain.bo.HrpLeaveRequestsBo;
import org.dromara.hrp.service.IHrpLeaveRequestsService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 期望休假/排休记录
 *
 * @author Lion Li
 * @date 2025-08-23
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/hrp/leaveRequests")
public class HrpLeaveRequestsController extends BaseController {

    private final IHrpLeaveRequestsService hrpLeaveRequestsService;

    /**
     * 查询期望休假/排休记录列表
     */
    @SaCheckPermission("hrp:leaveRequests:list")
    @GetMapping("/list")
    public TableDataInfo<HrpLeaveRequestsVo> list(HrpLeaveRequestsBo bo, PageQuery pageQuery) {
        return hrpLeaveRequestsService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出期望休假/排休记录列表
     */
    @SaCheckPermission("hrp:leaveRequests:export")
    @Log(title = "期望休假/排休记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HrpLeaveRequestsBo bo, HttpServletResponse response) {
        List<HrpLeaveRequestsVo> list = hrpLeaveRequestsService.queryList(bo);
        ExcelUtil.exportExcel(list, "期望休假/排休记录", HrpLeaveRequestsVo.class, response);
    }

    /**
     * 获取期望休假/排休记录详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("hrp:leaveRequests:query")
    @GetMapping("/{id}")
    public R<HrpLeaveRequestsVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(hrpLeaveRequestsService.queryById(id));
    }

    /**
     * 新增期望休假/排休记录
     */
    @SaCheckPermission("hrp:leaveRequests:add")
    @Log(title = "期望休假/排休记录", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody HrpLeaveRequestsBo bo) {
        return toAjax(hrpLeaveRequestsService.insertByBo(bo));
    }

    /**
     * 修改期望休假/排休记录
     */
    @SaCheckPermission("hrp:leaveRequests:edit")
    @Log(title = "期望休假/排休记录", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody HrpLeaveRequestsBo bo) {
        return toAjax(hrpLeaveRequestsService.updateByBo(bo));
    }

    /**
     * 删除期望休假/排休记录
     *
     * @param ids 主键串
     */
    @SaCheckPermission("hrp:leaveRequests:remove")
    @Log(title = "期望休假/排休记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(hrpLeaveRequestsService.deleteWithValidByIds(List.of(ids), true));
    }
}
