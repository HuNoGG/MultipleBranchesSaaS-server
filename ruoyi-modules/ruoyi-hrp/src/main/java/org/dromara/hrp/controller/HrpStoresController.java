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
import org.dromara.hrp.domain.vo.HrpStoresVo;
import org.dromara.hrp.domain.bo.HrpStoresBo;
import org.dromara.hrp.service.IHrpStoresService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 分店
 *
 * @author Lion Li
 * @date 2025-08-23
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/hrp/stores")
public class HrpStoresController extends BaseController {

    private final IHrpStoresService hrpStoresService;

    /**
     * 查询分店列表
     */
    @SaCheckPermission("hrp:stores:list")
    @GetMapping("/list")
    public TableDataInfo<HrpStoresVo> list(HrpStoresBo bo, PageQuery pageQuery) {
        return hrpStoresService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出分店列表
     */
    @SaCheckPermission("hrp:stores:export")
    @Log(title = "分店", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HrpStoresBo bo, HttpServletResponse response) {
        List<HrpStoresVo> list = hrpStoresService.queryList(bo);
        ExcelUtil.exportExcel(list, "分店", HrpStoresVo.class, response);
    }

    /**
     * 获取分店详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("hrp:stores:query")
    @GetMapping("/{id}")
    public R<HrpStoresVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(hrpStoresService.queryById(id));
    }

    /**
     * 新增分店
     */
    @SaCheckPermission("hrp:stores:add")
    @Log(title = "分店", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody HrpStoresBo bo) {
        return toAjax(hrpStoresService.insertByBo(bo));
    }

    /**
     * 修改分店
     */
    @SaCheckPermission("hrp:stores:edit")
    @Log(title = "分店", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody HrpStoresBo bo) {
        return toAjax(hrpStoresService.updateByBo(bo));
    }

    /**
     * 删除分店
     *
     * @param ids 主键串
     */
    @SaCheckPermission("hrp:stores:remove")
    @Log(title = "分店", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(hrpStoresService.deleteWithValidByIds(List.of(ids), true));
    }
}
