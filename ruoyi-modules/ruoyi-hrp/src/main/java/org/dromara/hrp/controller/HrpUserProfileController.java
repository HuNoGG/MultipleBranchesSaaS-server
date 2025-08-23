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
import org.dromara.hrp.domain.vo.HrpUserProfileVo;
import org.dromara.hrp.domain.bo.HrpUserProfileBo;
import org.dromara.hrp.service.IHrpUserProfileService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 员工档案扩展
 *
 * @author Hzy
 * @date 2025-08-23
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/hrp/userProfile")
public class HrpUserProfileController extends BaseController {

    private final IHrpUserProfileService hrpUserProfileService;

    /**
     * 查询员工档案扩展列表
     */
    @SaCheckPermission("hrp:userProfile:list")
    @GetMapping("/list")
    public TableDataInfo<HrpUserProfileVo> list(HrpUserProfileBo bo, PageQuery pageQuery) {
        return hrpUserProfileService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出员工档案扩展列表
     */
    @SaCheckPermission("hrp:userProfile:export")
    @Log(title = "员工档案扩展", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HrpUserProfileBo bo, HttpServletResponse response) {
        List<HrpUserProfileVo> list = hrpUserProfileService.queryList(bo);
        ExcelUtil.exportExcel(list, "员工档案扩展", HrpUserProfileVo.class, response);
    }

    /**
     * 获取员工档案扩展详细信息
     *
     * @param userId 主键
     */
    @SaCheckPermission("hrp:userProfile:query")
    @GetMapping("/{userId}")
    public R<HrpUserProfileVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long userId) {
        return R.ok(hrpUserProfileService.queryById(userId));
    }

    /**
     * 新增员工档案扩展
     */
    @SaCheckPermission("hrp:userProfile:add")
    @Log(title = "员工档案扩展", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody HrpUserProfileBo bo) {
        return toAjax(hrpUserProfileService.insertByBo(bo));
    }

    /**
     * 修改员工档案扩展
     */
    @SaCheckPermission("hrp:userProfile:edit")
    @Log(title = "员工档案扩展", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody HrpUserProfileBo bo) {
        return toAjax(hrpUserProfileService.updateByBo(bo));
    }

    /**
     * 删除员工档案扩展
     *
     * @param userIds 主键串
     */
    @SaCheckPermission("hrp:userProfile:remove")
    @Log(title = "员工档案扩展", businessType = BusinessType.DELETE)
    @DeleteMapping("/{userIds}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] userIds) {
        return toAjax(hrpUserProfileService.deleteWithValidByIds(List.of(userIds), true));
    }
}
