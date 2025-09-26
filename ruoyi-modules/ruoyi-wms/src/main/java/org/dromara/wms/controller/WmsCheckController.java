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
import org.dromara.wms.domain.vo.WmsCheckVo;
import org.dromara.wms.domain.bo.WmsCheckBo;
import org.dromara.wms.service.IWmsCheckService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 点货
 *
 * @author Jules
 * @date 2025-09-26
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/wms/check")
public class WmsCheckController extends BaseController {

    private final IWmsCheckService wmsCheckService;

    /**
     * 查询点货列表
     */
    @SaCheckPermission("wms:check:list")
    @GetMapping("/list")
    public TableDataInfo<WmsCheckVo> list(WmsCheckBo bo, PageQuery pageQuery) {
        return wmsCheckService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出点货列表
     */
    @SaCheckPermission("wms:check:export")
    @Log(title = "点货", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(WmsCheckBo bo, HttpServletResponse response) {
        List<WmsCheckVo> list = wmsCheckService.queryList(bo);
        ExcelUtil.exportExcel(list, "点货", WmsCheckVo.class, response);
    }

    /**
     * 获取点货详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("wms:check:query")
    @GetMapping("/{id}")
    public R<WmsCheckVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(wmsCheckService.queryById(id));
    }

    /**
     * 新增点货
     */
    @SaCheckPermission("wms:check:add")
    @Log(title = "点货", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody WmsCheckBo bo) {
        return toAjax(wmsCheckService.insertByBo(bo));
    }

    /**
     * 修改点货
     */
    @SaCheckPermission("wms:check:edit")
    @Log(title = "点货", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody WmsCheckBo bo) {
        return toAjax(wmsCheckService.updateByBo(bo));
    }

    /**
     * 删除点货
     *
     * @param ids 主键串
     */
    @SaCheckPermission("wms:check:remove")
    @Log(title = "点货", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(wmsCheckService.deleteWithValidByIds(List.of(ids), true));
    }
}