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
import org.dromara.hrp.domain.vo.HrpUserAvailabilityVo;
import org.dromara.hrp.domain.bo.HrpUserAvailabilityBo;
import org.dromara.hrp.service.IHrpUserAvailabilityService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 员工可上班时段
 *
 * @author Lion Li
 * @date 2025-08-23
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/hrp/userAvailability")
public class HrpUserAvailabilityController extends BaseController {

    private final IHrpUserAvailabilityService hrpUserAvailabilityService;

    /**
     * 查询员工可上班时段列表
     */
    @SaCheckPermission("hrp:userAvailability:list")
    @GetMapping("/list")
    public TableDataInfo<HrpUserAvailabilityVo> list(HrpUserAvailabilityBo bo, PageQuery pageQuery) {
        return hrpUserAvailabilityService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出员工可上班时段列表
     */
    @SaCheckPermission("hrp:userAvailability:export")
    @Log(title = "员工可上班时段", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HrpUserAvailabilityBo bo, HttpServletResponse response) {
        List<HrpUserAvailabilityVo> list = hrpUserAvailabilityService.queryList(bo);
        ExcelUtil.exportExcel(list, "员工可上班时段", HrpUserAvailabilityVo.class, response);
    }

    /**
     * 获取员工可上班时段详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("hrp:userAvailability:query")
    @GetMapping("/{id}")
    public R<HrpUserAvailabilityVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(hrpUserAvailabilityService.queryById(id));
    }

    /**
     * 新增员工可上班时段
     */
    @SaCheckPermission("hrp:userAvailability:add")
    @Log(title = "员工可上班时段", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody HrpUserAvailabilityBo bo) {
        return toAjax(hrpUserAvailabilityService.insertByBo(bo));
    }

    /**
     * 修改员工可上班时段
     */
    @SaCheckPermission("hrp:userAvailability:edit")
    @Log(title = "员工可上班时段", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody HrpUserAvailabilityBo bo) {
        return toAjax(hrpUserAvailabilityService.updateByBo(bo));
    }

    /**
     * 删除员工可上班时段
     *
     * @param ids 主键串
     */
    @SaCheckPermission("hrp:userAvailability:remove")
    @Log(title = "员工可上班时段", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(hrpUserAvailabilityService.deleteWithValidByIds(List.of(ids), true));
    }
}
