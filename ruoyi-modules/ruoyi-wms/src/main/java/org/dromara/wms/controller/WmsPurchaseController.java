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
import org.dromara.wms.domain.vo.WmsPurchaseVo;
import org.dromara.wms.domain.bo.WmsPurchaseBo;
import org.dromara.wms.service.IWmsPurchaseService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 进货记录
 *
 * @author Jules
 * @date 2025-09-26
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/wms/purchase")
public class WmsPurchaseController extends BaseController {

    private final IWmsPurchaseService wmsPurchaseService;

    /**
     * 查询进货记录列表
     */
    @SaCheckPermission("wms:purchase:list")
    @GetMapping("/list")
    public TableDataInfo<WmsPurchaseVo> list(WmsPurchaseBo bo, PageQuery pageQuery) {
        return wmsPurchaseService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出进货记录列表
     */
    @SaCheckPermission("wms:purchase:export")
    @Log(title = "进货记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(WmsPurchaseBo bo, HttpServletResponse response) {
        List<WmsPurchaseVo> list = wmsPurchaseService.queryList(bo);
        ExcelUtil.exportExcel(list, "进货记录", WmsPurchaseVo.class, response);
    }

    /**
     * 获取进货记录详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("wms:purchase:query")
    @GetMapping("/{id}")
    public R<WmsPurchaseVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(wmsPurchaseService.queryById(id));
    }

    /**
     * 新增进货记录
     */
    @SaCheckPermission("wms:purchase:add")
    @Log(title = "进货记录", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody WmsPurchaseBo bo) {
        return toAjax(wmsPurchaseService.insertByBo(bo));
    }

    /**
     * 修改进货记录
     */
    @SaCheckPermission("wms:purchase:edit")
    @Log(title = "进货记录", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody WmsPurchaseBo bo) {
        return toAjax(wmsPurchaseService.updateByBo(bo));
    }

    /**
     * 删除进货记录
     *
     * @param ids 主键串
     */
    @SaCheckPermission("wms:purchase:remove")
    @Log(title = "进货记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(wmsPurchaseService.deleteWithValidByIds(List.of(ids), true));
    }
}