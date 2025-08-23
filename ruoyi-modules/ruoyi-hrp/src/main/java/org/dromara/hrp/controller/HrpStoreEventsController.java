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
import org.dromara.hrp.domain.vo.HrpStoreEventsVo;
import org.dromara.hrp.domain.bo.HrpStoreEventsBo;
import org.dromara.hrp.service.IHrpStoreEventsService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 分店特殊事件
 *
 * @author Lion Li
 * @date 2025-08-23
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/hrp/storeEvents")
public class HrpStoreEventsController extends BaseController {

    private final IHrpStoreEventsService hrpStoreEventsService;

    /**
     * 查询分店特殊事件列表
     */
    @SaCheckPermission("hrp:storeEvents:list")
    @GetMapping("/list")
    public TableDataInfo<HrpStoreEventsVo> list(HrpStoreEventsBo bo, PageQuery pageQuery) {
        return hrpStoreEventsService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出分店特殊事件列表
     */
    @SaCheckPermission("hrp:storeEvents:export")
    @Log(title = "分店特殊事件", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HrpStoreEventsBo bo, HttpServletResponse response) {
        List<HrpStoreEventsVo> list = hrpStoreEventsService.queryList(bo);
        ExcelUtil.exportExcel(list, "分店特殊事件", HrpStoreEventsVo.class, response);
    }

    /**
     * 获取分店特殊事件详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("hrp:storeEvents:query")
    @GetMapping("/{id}")
    public R<HrpStoreEventsVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(hrpStoreEventsService.queryById(id));
    }

    /**
     * 新增分店特殊事件
     */
    @SaCheckPermission("hrp:storeEvents:add")
    @Log(title = "分店特殊事件", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody HrpStoreEventsBo bo) {
        return toAjax(hrpStoreEventsService.insertByBo(bo));
    }

    /**
     * 修改分店特殊事件
     */
    @SaCheckPermission("hrp:storeEvents:edit")
    @Log(title = "分店特殊事件", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody HrpStoreEventsBo bo) {
        return toAjax(hrpStoreEventsService.updateByBo(bo));
    }

    /**
     * 删除分店特殊事件
     *
     * @param ids 主键串
     */
    @SaCheckPermission("hrp:storeEvents:remove")
    @Log(title = "分店特殊事件", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(hrpStoreEventsService.deleteWithValidByIds(List.of(ids), true));
    }
}
