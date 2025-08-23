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
import org.dromara.hrp.domain.vo.HrpScheduleModificationsVo;
import org.dromara.hrp.domain.bo.HrpScheduleModificationsBo;
import org.dromara.hrp.service.IHrpScheduleModificationsService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 排班修改记录
 *
 * @author Lion Li
 * @date 2025-08-23
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/hrp/scheduleModifications")
public class HrpScheduleModificationsController extends BaseController {

    private final IHrpScheduleModificationsService hrpScheduleModificationsService;

    /**
     * 查询排班修改记录列表
     */
    @SaCheckPermission("hrp:scheduleModifications:list")
    @GetMapping("/list")
    public TableDataInfo<HrpScheduleModificationsVo> list(HrpScheduleModificationsBo bo, PageQuery pageQuery) {
        return hrpScheduleModificationsService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出排班修改记录列表
     */
    @SaCheckPermission("hrp:scheduleModifications:export")
    @Log(title = "排班修改记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HrpScheduleModificationsBo bo, HttpServletResponse response) {
        List<HrpScheduleModificationsVo> list = hrpScheduleModificationsService.queryList(bo);
        ExcelUtil.exportExcel(list, "排班修改记录", HrpScheduleModificationsVo.class, response);
    }

    /**
     * 获取排班修改记录详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("hrp:scheduleModifications:query")
    @GetMapping("/{id}")
    public R<HrpScheduleModificationsVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(hrpScheduleModificationsService.queryById(id));
    }

    /**
     * 新增排班修改记录
     */
    @SaCheckPermission("hrp:scheduleModifications:add")
    @Log(title = "排班修改记录", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody HrpScheduleModificationsBo bo) {
        return toAjax(hrpScheduleModificationsService.insertByBo(bo));
    }

    /**
     * 修改排班修改记录
     */
    @SaCheckPermission("hrp:scheduleModifications:edit")
    @Log(title = "排班修改记录", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody HrpScheduleModificationsBo bo) {
        return toAjax(hrpScheduleModificationsService.updateByBo(bo));
    }

    /**
     * 删除排班修改记录
     *
     * @param ids 主键串
     */
    @SaCheckPermission("hrp:scheduleModifications:remove")
    @Log(title = "排班修改记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(hrpScheduleModificationsService.deleteWithValidByIds(List.of(ids), true));
    }
}
