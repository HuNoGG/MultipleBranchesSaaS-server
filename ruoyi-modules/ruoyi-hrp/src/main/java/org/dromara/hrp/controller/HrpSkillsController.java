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
import org.dromara.hrp.domain.vo.HrpSkillsVo;
import org.dromara.hrp.domain.bo.HrpSkillsBo;
import org.dromara.hrp.service.IHrpSkillsService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 技能岗位
 *
 * @author Lion Li
 * @date 2025-08-23
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/hrp/skills")
public class HrpSkillsController extends BaseController {

    private final IHrpSkillsService hrpSkillsService;

    /**
     * 查询技能岗位列表
     */
    @SaCheckPermission("hrp:skills:list")
    @GetMapping("/list")
    public TableDataInfo<HrpSkillsVo> list(HrpSkillsBo bo, PageQuery pageQuery) {
        return hrpSkillsService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出技能岗位列表
     */
    @SaCheckPermission("hrp:skills:export")
    @Log(title = "技能岗位", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HrpSkillsBo bo, HttpServletResponse response) {
        List<HrpSkillsVo> list = hrpSkillsService.queryList(bo);
        ExcelUtil.exportExcel(list, "技能岗位", HrpSkillsVo.class, response);
    }

    /**
     * 获取技能岗位详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("hrp:skills:query")
    @GetMapping("/{id}")
    public R<HrpSkillsVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(hrpSkillsService.queryById(id));
    }

    /**
     * 新增技能岗位
     */
    @SaCheckPermission("hrp:skills:add")
    @Log(title = "技能岗位", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody HrpSkillsBo bo) {
        return toAjax(hrpSkillsService.insertByBo(bo));
    }

    /**
     * 修改技能岗位
     */
    @SaCheckPermission("hrp:skills:edit")
    @Log(title = "技能岗位", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody HrpSkillsBo bo) {
        return toAjax(hrpSkillsService.updateByBo(bo));
    }

    /**
     * 删除技能岗位
     *
     * @param ids 主键串
     */
    @SaCheckPermission("hrp:skills:remove")
    @Log(title = "技能岗位", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(hrpSkillsService.deleteWithValidByIds(List.of(ids), true));
    }
}
