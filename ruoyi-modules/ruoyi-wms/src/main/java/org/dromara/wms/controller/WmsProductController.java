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
import org.dromara.wms.domain.vo.WmsProductVo;
import org.dromara.wms.domain.bo.WmsProductBo;
import org.dromara.wms.service.IWmsProductService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 商品信息
 *
 * @author Jules
 * @date 2025-09-26
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/wms/product")
public class WmsProductController extends BaseController {

    private final IWmsProductService wmsProductService;

    /**
     * 查询商品信息列表
     */
    @SaCheckPermission("wms:product:list")
    @GetMapping("/list")
    public TableDataInfo<WmsProductVo> list(WmsProductBo bo, PageQuery pageQuery) {
        return wmsProductService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出商品信息列表
     */
    @SaCheckPermission("wms:product:export")
    @Log(title = "商品信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(WmsProductBo bo, HttpServletResponse response) {
        List<WmsProductVo> list = wmsProductService.queryList(bo);
        ExcelUtil.exportExcel(list, "商品信息", WmsProductVo.class, response);
    }

    /**
     * 获取商品信息详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("wms:product:query")
    @GetMapping("/{id}")
    public R<WmsProductVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(wmsProductService.queryById(id));
    }

    /**
     * 新增商品信息
     */
    @SaCheckPermission("wms:product:add")
    @Log(title = "商品信息", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody WmsProductBo bo) {
        return toAjax(wmsProductService.insertByBo(bo));
    }

    /**
     * 修改商品信息
     */
    @SaCheckPermission("wms:product:edit")
    @Log(title = "商品信息", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody WmsProductBo bo) {
        return toAjax(wmsProductService.updateByBo(bo));
    }

    /**
     * 删除商品信息
     *
     * @param ids 主键串
     */
    @SaCheckPermission("wms:product:remove")
    @Log(title = "商品信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(wmsProductService.deleteWithValidByIds(List.of(ids), true));
    }
}