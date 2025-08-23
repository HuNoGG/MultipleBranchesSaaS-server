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
import org.dromara.hrp.domain.vo.HrpUserStoreAccessVo;
import org.dromara.hrp.domain.bo.HrpUserStoreAccessBo;
import org.dromara.hrp.service.IHrpUserStoreAccessService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 员工跨店权限
 *
 * @author Hzy
 * @date 2025-08-23
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/hrp/userStoreAccess")
public class HrpUserStoreAccessController extends BaseController {

    private final IHrpUserStoreAccessService hrpUserStoreAccessService;

    /**
     * 查询员工跨店权限列表
     */
    @SaCheckPermission("hrp:userStoreAccess:list")
    @GetMapping("/list")
    public TableDataInfo<HrpUserStoreAccessVo> list(HrpUserStoreAccessBo bo, PageQuery pageQuery) {
        return hrpUserStoreAccessService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出员工跨店权限列表
     */
    @SaCheckPermission("hrp:userStoreAccess:export")
    @Log(title = "员工跨店权限", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HrpUserStoreAccessBo bo, HttpServletResponse response) {
        List<HrpUserStoreAccessVo> list = hrpUserStoreAccessService.queryList(bo);
        ExcelUtil.exportExcel(list, "员工跨店权限", HrpUserStoreAccessVo.class, response);
    }

    /**
     * 获取员工跨店权限详细信息
     *
     * @param userId 主键
     */
    @SaCheckPermission("hrp:userStoreAccess:query")
    @GetMapping("/{userId}")
    public R<HrpUserStoreAccessVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long userId) {
        return R.ok(hrpUserStoreAccessService.queryById(userId));
    }

    /**
     * 新增员工跨店权限
     */
    @SaCheckPermission("hrp:userStoreAccess:add")
    @Log(title = "员工跨店权限", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody HrpUserStoreAccessBo bo) {
        return toAjax(hrpUserStoreAccessService.insertByBo(bo));
    }

    /**
     * 修改员工跨店权限
     */
    @SaCheckPermission("hrp:userStoreAccess:edit")
    @Log(title = "员工跨店权限", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody HrpUserStoreAccessBo bo) {
        return toAjax(hrpUserStoreAccessService.updateByBo(bo));
    }

    /**
     * 删除员工跨店权限
     *
     * @param userIds 主键串
     */
    @SaCheckPermission("hrp:userStoreAccess:remove")
    @Log(title = "员工跨店权限", businessType = BusinessType.DELETE)
    @DeleteMapping("/{userIds}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] userIds) {
        return toAjax(hrpUserStoreAccessService.deleteWithValidByIds(List.of(userIds), true));
    }
}
