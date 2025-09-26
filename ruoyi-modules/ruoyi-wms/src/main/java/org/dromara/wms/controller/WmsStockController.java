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
import org.dromara.wms.domain.vo.WmsStockVo;
import org.dromara.wms.domain.bo.WmsStockBo;
import org.dromara.wms.service.IWmsStockService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 库存
 *
 * @author Jules
 * @date 2025-09-26
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/wms/stock")
public class WmsStockController extends BaseController {

    private final IWmsStockService wmsStockService;

    /**
     * 查询库存列表
     */
    @SaCheckPermission("wms:stock:list")
    @GetMapping("/list")
    public TableDataInfo<WmsStockVo> list(WmsStockBo bo, PageQuery pageQuery) {
        return wmsStockService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出库存列表
     */
    @SaCheckPermission("wms:stock:export")
    @Log(title = "库存", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(WmsStockBo bo, HttpServletResponse response) {
        List<WmsStockVo> list = wmsStockService.queryList(bo);
        ExcelUtil.exportExcel(list, "库存", WmsStockVo.class, response);
    }

    /**
     * 获取库存详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("wms:stock:query")
    @GetMapping("/{id}")
    public R<WmsStockVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(wmsStockService.queryById(id));
    }

    /**
     * 新增库存
     */
    @SaCheckPermission("wms:stock:add")
    @Log(title = "库存", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody WmsStockBo bo) {
        return toAjax(wmsStockService.insertByBo(bo));
    }

    /**
     * 修改库存
     */
    @SaCheckPermission("wms:stock:edit")
    @Log(title = "库存", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody WmsStockBo bo) {
        return toAjax(wmsStockService.updateByBo(bo));
    }

    /**
     * 删除库存
     *
     * @param ids 主键串
     */
    @SaCheckPermission("wms:stock:remove")
    @Log(title = "库存", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(wmsStockService.deleteWithValidByIds(List.of(ids), true));
    }
}