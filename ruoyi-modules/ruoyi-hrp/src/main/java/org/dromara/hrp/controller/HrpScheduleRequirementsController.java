package org.dromara.hrp.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.*;
import cn.dev33.satoken.annotation.SaCheckPermission;
import org.dromara.hrp.domain.dto.AllRequirementsDto;
import org.dromara.hrp.domain.dto.DailyRequirementsDto;
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
import org.dromara.hrp.domain.vo.HrpScheduleRequirementsVo;
import org.dromara.hrp.domain.bo.HrpScheduleRequirementsBo;
import org.dromara.hrp.service.IHrpScheduleRequirementsService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 每日人力需求
 *
 * @author Lion Li
 * @date 2025-08-23
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/hrp/scheduleRequirements")
public class HrpScheduleRequirementsController extends BaseController {

    private final IHrpScheduleRequirementsService hrpScheduleRequirementsService;

    /**
     * 查询每日人力需求列表
     */
    @SaCheckPermission("hrp:scheduleRequirements:list")
    @GetMapping("/list")
    public TableDataInfo<HrpScheduleRequirementsVo> list(HrpScheduleRequirementsBo bo, PageQuery pageQuery) {
        return hrpScheduleRequirementsService.queryPageList(bo, pageQuery);
    }

    /**
     * 保存每日人力需求 (平日或假日)
     * @param dto 包含店铺、日期类型和需求矩阵的数据
     */
    @SaCheckPermission("hrp:scheduleRequirements:edit") // 复用编辑权限
    @Log(title = "保存每日人力需求", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PostMapping("/save")
    public R<Void> saveRequirements(@Validated @RequestBody DailyRequirementsDto dto) {
        hrpScheduleRequirementsService.saveRequirements(dto);
        return R.ok();
    }

    /**
     * 根据店铺ID查询所有类型（平日、假日、特殊）的每日人力需求
     * @param storeId 店铺ID
     */
    @SaCheckPermission("hrp:scheduleRequirements:list") // 复用查询权限
    @GetMapping("/all-by-store")
    public R<AllRequirementsDto> getAllRequirementsByStoreId(@RequestParam @NotNull Long storeId) {
        AllRequirementsDto result = hrpScheduleRequirementsService.getAllRequirementsByStoreId(storeId);
        return R.ok(result);
    }


    /**
     * 导出每日人力需求列表
     */
    @SaCheckPermission("hrp:scheduleRequirements:export")
    @Log(title = "每日人力需求", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HrpScheduleRequirementsBo bo, HttpServletResponse response) {
        List<HrpScheduleRequirementsVo> list = hrpScheduleRequirementsService.queryList(bo);
        ExcelUtil.exportExcel(list, "每日人力需求", HrpScheduleRequirementsVo.class, response);
    }

    /**
     * 获取每日人力需求详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("hrp:scheduleRequirements:query")
    @GetMapping("/{id}")
    public R<HrpScheduleRequirementsVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(hrpScheduleRequirementsService.queryById(id));
    }

    /**
     * 新增每日人力需求
     */
    @SaCheckPermission("hrp:scheduleRequirements:add")
    @Log(title = "每日人力需求", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody HrpScheduleRequirementsBo bo) {
        return toAjax(hrpScheduleRequirementsService.insertByBo(bo));
    }

    /**
     * 修改每日人力需求
     */
    @SaCheckPermission("hrp:scheduleRequirements:edit")
    @Log(title = "每日人力需求", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody HrpScheduleRequirementsBo bo) {
        return toAjax(hrpScheduleRequirementsService.updateByBo(bo));
    }

    /**
     * 删除每日人力需求
     *
     * @param ids 主键串
     */
    @SaCheckPermission("hrp:scheduleRequirements:remove")
    @Log(title = "每日人力需求", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(hrpScheduleRequirementsService.deleteWithValidByIds(List.of(ids), true));
    }
}
