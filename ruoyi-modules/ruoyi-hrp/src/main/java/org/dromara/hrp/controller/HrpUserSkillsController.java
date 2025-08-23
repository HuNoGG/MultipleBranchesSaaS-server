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
import org.dromara.hrp.domain.vo.HrpUserSkillsVo;
import org.dromara.hrp.domain.bo.HrpUserSkillsBo;
import org.dromara.hrp.service.IHrpUserSkillsService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 员工技能关联
 *
 * @author Hzy
 * @date 2025-08-23
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/hrp/userSkills")
public class HrpUserSkillsController extends BaseController {

    private final IHrpUserSkillsService hrpUserSkillsService;

    /**
     * 查询员工技能关联列表
     */
    @SaCheckPermission("hrp:userSkills:list")
    @GetMapping("/list")
    public TableDataInfo<HrpUserSkillsVo> list(HrpUserSkillsBo bo, PageQuery pageQuery) {
        return hrpUserSkillsService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出员工技能关联列表
     */
    @SaCheckPermission("hrp:userSkills:export")
    @Log(title = "员工技能关联", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HrpUserSkillsBo bo, HttpServletResponse response) {
        List<HrpUserSkillsVo> list = hrpUserSkillsService.queryList(bo);
        ExcelUtil.exportExcel(list, "员工技能关联", HrpUserSkillsVo.class, response);
    }

    /**
     * 获取员工技能关联详细信息
     *
     * @param userId 主键
     */
    @SaCheckPermission("hrp:userSkills:query")
    @GetMapping("/{userId}")
    public R<HrpUserSkillsVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long userId) {
        return R.ok(hrpUserSkillsService.queryById(userId));
    }

    /**
     * 新增员工技能关联
     */
    @SaCheckPermission("hrp:userSkills:add")
    @Log(title = "员工技能关联", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody HrpUserSkillsBo bo) {
        return toAjax(hrpUserSkillsService.insertByBo(bo));
    }

    /**
     * 修改员工技能关联
     */
    @SaCheckPermission("hrp:userSkills:edit")
    @Log(title = "员工技能关联", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody HrpUserSkillsBo bo) {
        return toAjax(hrpUserSkillsService.updateByBo(bo));
    }

    /**
     * 删除员工技能关联
     *
     * @param userIds 主键串
     */
    @SaCheckPermission("hrp:userSkills:remove")
    @Log(title = "员工技能关联", businessType = BusinessType.DELETE)
    @DeleteMapping("/{userIds}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] userIds) {
        return toAjax(hrpUserSkillsService.deleteWithValidByIds(List.of(userIds), true));
    }
}
