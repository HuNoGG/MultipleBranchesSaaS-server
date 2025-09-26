package org.dromara.wms.controller;

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
import org.dromara.wms.domain.vo.WmsSupplierVo;
import org.dromara.wms.domain.bo.WmsSupplierBo;
import org.dromara.wms.service.IWmsSupplierService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 供应商信息
 *
 * @author Jules
 * @date 2025-09-26
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/wms/supplier")
public class WmsSupplierController extends BaseController {

    private final IWmsSupplierService wmsSupplierService;

    /**
     * 查询供应商信息列表
     */
    @SaCheckPermission("wms:supplier:list")
    @GetMapping("/list")
    public TableDataInfo<WmsSupplierVo> list(WmsSupplierBo bo, PageQuery pageQuery) {
        return wmsSupplierService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出供应商信息列表
     */
    @SaCheckPermission("wms:supplier:export")
    @Log(title = "供应商信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(WmsSupplierBo bo, HttpServletResponse response) {
        List<WmsSupplierVo> list = wmsSupplierService.queryList(bo);
        ExcelUtil.exportExcel(list, "供应商信息", WmsSupplierVo.class, response);
    }

    /**
     * 获取供应商信息详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("wms:supplier:query")
    @GetMapping("/{id}")
    public R<WmsSupplierVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(wmsSupplierService.queryById(id));
    }

    /**
     * 新增供应商信息
     */
    @SaCheckPermission("wms:supplier:add")
    @Log(title = "供应商信息", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody WmsSupplierBo bo) {
        return toAjax(wmsSupplierService.insertByBo(bo));
    }

    /**
     * 修改供应商信息
     */
    @SaCheckPermission("wms:supplier:edit")
    @Log(title = "供应商信息", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody WmsSupplierBo bo) {
        return toAjax(wmsSupplierService.updateByBo(bo));
    }

    /**
     * 删除供应商信息
     *
     * @param ids 主键串
     */
    @SaCheckPermission("wms:supplier:remove")
    @Log(title = "供应商信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(wmsSupplierService.deleteWithValidByIds(List.of(ids), true));
    }
}