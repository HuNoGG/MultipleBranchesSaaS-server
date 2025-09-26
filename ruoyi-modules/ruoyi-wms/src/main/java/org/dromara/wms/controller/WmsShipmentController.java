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
import org.dromara.wms.domain.vo.WmsShipmentVo;
import org.dromara.wms.domain.bo.WmsShipmentBo;
import org.dromara.wms.service.IWmsShipmentService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 出货记录
 *
 * @author Jules
 * @date 2025-09-26
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/wms/shipment")
public class WmsShipmentController extends BaseController {

    private final IWmsShipmentService wmsShipmentService;

    /**
     * 查询出货记录列表
     */
    @SaCheckPermission("wms:shipment:list")
    @GetMapping("/list")
    public TableDataInfo<WmsShipmentVo> list(WmsShipmentBo bo, PageQuery pageQuery) {
        return wmsShipmentService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出出货记录列表
     */
    @SaCheckPermission("wms:shipment:export")
    @Log(title = "出货记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(WmsShipmentBo bo, HttpServletResponse response) {
        List<WmsShipmentVo> list = wmsShipmentService.queryList(bo);
        ExcelUtil.exportExcel(list, "出货记录", WmsShipmentVo.class, response);
    }

    /**
     * 获取出货记录详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("wms:shipment:query")
    @GetMapping("/{id}")
    public R<WmsShipmentVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(wmsShipmentService.queryById(id));
    }

    /**
     * 新增出货记录
     */
    @SaCheckPermission("wms:shipment:add")
    @Log(title = "出货记录", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody WmsShipmentBo bo) {
        return toAjax(wmsShipmentService.insertByBo(bo));
    }

    /**
     * 修改出货记录
     */
    @SaCheckPermission("wms:shipment:edit")
    @Log(title = "出货记录", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody WmsShipmentBo bo) {
        return toAjax(wmsShipmentService.updateByBo(bo));
    }

    /**
     * 删除出货记录
     *
     * @param ids 主键串
     */
    @SaCheckPermission("wms:shipment:remove")
    @Log(title = "出货记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(wmsShipmentService.deleteWithValidByIds(List.of(ids), true));
    }
}