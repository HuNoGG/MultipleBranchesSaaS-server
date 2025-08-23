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
import org.dromara.hrp.domain.vo.HrpStoreSupportRelationsVo;
import org.dromara.hrp.domain.bo.HrpStoreSupportRelationsBo;
import org.dromara.hrp.service.IHrpStoreSupportRelationsService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 分店支援关系
 *
 * @author Lion Li
 * @date 2025-08-23
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/hrp/storeSupportRelations")
public class HrpStoreSupportRelationsController extends BaseController {

    private final IHrpStoreSupportRelationsService hrpStoreSupportRelationsService;

    /**
     * 查询分店支援关系列表
     */
    @SaCheckPermission("hrp:storeSupportRelations:list")
    @GetMapping("/list")
    public TableDataInfo<HrpStoreSupportRelationsVo> list(HrpStoreSupportRelationsBo bo, PageQuery pageQuery) {
        return hrpStoreSupportRelationsService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出分店支援关系列表
     */
    @SaCheckPermission("hrp:storeSupportRelations:export")
    @Log(title = "分店支援关系", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HrpStoreSupportRelationsBo bo, HttpServletResponse response) {
        List<HrpStoreSupportRelationsVo> list = hrpStoreSupportRelationsService.queryList(bo);
        ExcelUtil.exportExcel(list, "分店支援关系", HrpStoreSupportRelationsVo.class, response);
    }

    /**
     * 获取分店支援关系详细信息
     *
     * @param requestingStoreId 主键
     */
    @SaCheckPermission("hrp:storeSupportRelations:query")
    @GetMapping("/{requestingStoreId}")
    public R<HrpStoreSupportRelationsVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long requestingStoreId) {
        return R.ok(hrpStoreSupportRelationsService.queryById(requestingStoreId));
    }

    /**
     * 新增分店支援关系
     */
    @SaCheckPermission("hrp:storeSupportRelations:add")
    @Log(title = "分店支援关系", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody HrpStoreSupportRelationsBo bo) {
        return toAjax(hrpStoreSupportRelationsService.insertByBo(bo));
    }

    /**
     * 修改分店支援关系
     */
    @SaCheckPermission("hrp:storeSupportRelations:edit")
    @Log(title = "分店支援关系", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody HrpStoreSupportRelationsBo bo) {
        return toAjax(hrpStoreSupportRelationsService.updateByBo(bo));
    }

    /**
     * 删除分店支援关系
     *
     * @param requestingStoreIds 主键串
     */
    @SaCheckPermission("hrp:storeSupportRelations:remove")
    @Log(title = "分店支援关系", businessType = BusinessType.DELETE)
    @DeleteMapping("/{requestingStoreIds}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] requestingStoreIds) {
        return toAjax(hrpStoreSupportRelationsService.deleteWithValidByIds(List.of(requestingStoreIds), true));
    }
}
