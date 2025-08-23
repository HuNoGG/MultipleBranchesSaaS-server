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
import org.dromara.hrp.domain.vo.HrpInventoryTransfersVo;
import org.dromara.hrp.domain.bo.HrpInventoryTransfersBo;
import org.dromara.hrp.service.IHrpInventoryTransfersService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 调货记录
 *
 * @author Lion Li
 * @date 2025-08-23
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/hrp/inventoryTransfers")
public class HrpInventoryTransfersController extends BaseController {

    private final IHrpInventoryTransfersService hrpInventoryTransfersService;

    /**
     * 查询调货记录列表
     */
    @SaCheckPermission("hrp:inventoryTransfers:list")
    @GetMapping("/list")
    public TableDataInfo<HrpInventoryTransfersVo> list(HrpInventoryTransfersBo bo, PageQuery pageQuery) {
        return hrpInventoryTransfersService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出调货记录列表
     */
    @SaCheckPermission("hrp:inventoryTransfers:export")
    @Log(title = "调货记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HrpInventoryTransfersBo bo, HttpServletResponse response) {
        List<HrpInventoryTransfersVo> list = hrpInventoryTransfersService.queryList(bo);
        ExcelUtil.exportExcel(list, "调货记录", HrpInventoryTransfersVo.class, response);
    }

    /**
     * 获取调货记录详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("hrp:inventoryTransfers:query")
    @GetMapping("/{id}")
    public R<HrpInventoryTransfersVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(hrpInventoryTransfersService.queryById(id));
    }

    /**
     * 新增调货记录
     */
    @SaCheckPermission("hrp:inventoryTransfers:add")
    @Log(title = "调货记录", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody HrpInventoryTransfersBo bo) {
        return toAjax(hrpInventoryTransfersService.insertByBo(bo));
    }

    /**
     * 修改调货记录
     */
    @SaCheckPermission("hrp:inventoryTransfers:edit")
    @Log(title = "调货记录", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody HrpInventoryTransfersBo bo) {
        return toAjax(hrpInventoryTransfersService.updateByBo(bo));
    }

    /**
     * 删除调货记录
     *
     * @param ids 主键串
     */
    @SaCheckPermission("hrp:inventoryTransfers:remove")
    @Log(title = "调货记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(hrpInventoryTransfersService.deleteWithValidByIds(List.of(ids), true));
    }
}
