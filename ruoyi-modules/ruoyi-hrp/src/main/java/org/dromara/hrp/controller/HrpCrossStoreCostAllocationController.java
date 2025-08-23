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
import org.dromara.hrp.domain.vo.HrpCrossStoreCostAllocationVo;
import org.dromara.hrp.domain.bo.HrpCrossStoreCostAllocationBo;
import org.dromara.hrp.service.IHrpCrossStoreCostAllocationService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 跨店成本分摊设定
 *
 * @author Lion Li
 * @date 2025-08-23
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/hrp/crossStoreCostAllocation")
public class HrpCrossStoreCostAllocationController extends BaseController {

    private final IHrpCrossStoreCostAllocationService hrpCrossStoreCostAllocationService;

    /**
     * 查询跨店成本分摊设定列表
     */
    @SaCheckPermission("hrp:crossStoreCostAllocation:list")
    @GetMapping("/list")
    public TableDataInfo<HrpCrossStoreCostAllocationVo> list(HrpCrossStoreCostAllocationBo bo, PageQuery pageQuery) {
        return hrpCrossStoreCostAllocationService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出跨店成本分摊设定列表
     */
    @SaCheckPermission("hrp:crossStoreCostAllocation:export")
    @Log(title = "跨店成本分摊设定", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HrpCrossStoreCostAllocationBo bo, HttpServletResponse response) {
        List<HrpCrossStoreCostAllocationVo> list = hrpCrossStoreCostAllocationService.queryList(bo);
        ExcelUtil.exportExcel(list, "跨店成本分摊设定", HrpCrossStoreCostAllocationVo.class, response);
    }

    /**
     * 获取跨店成本分摊设定详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("hrp:crossStoreCostAllocation:query")
    @GetMapping("/{id}")
    public R<HrpCrossStoreCostAllocationVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(hrpCrossStoreCostAllocationService.queryById(id));
    }

    /**
     * 新增跨店成本分摊设定
     */
    @SaCheckPermission("hrp:crossStoreCostAllocation:add")
    @Log(title = "跨店成本分摊设定", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody HrpCrossStoreCostAllocationBo bo) {
        return toAjax(hrpCrossStoreCostAllocationService.insertByBo(bo));
    }

    /**
     * 修改跨店成本分摊设定
     */
    @SaCheckPermission("hrp:crossStoreCostAllocation:edit")
    @Log(title = "跨店成本分摊设定", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody HrpCrossStoreCostAllocationBo bo) {
        return toAjax(hrpCrossStoreCostAllocationService.updateByBo(bo));
    }

    /**
     * 删除跨店成本分摊设定
     *
     * @param ids 主键串
     */
    @SaCheckPermission("hrp:crossStoreCostAllocation:remove")
    @Log(title = "跨店成本分摊设定", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(hrpCrossStoreCostAllocationService.deleteWithValidByIds(List.of(ids), true));
    }
}
