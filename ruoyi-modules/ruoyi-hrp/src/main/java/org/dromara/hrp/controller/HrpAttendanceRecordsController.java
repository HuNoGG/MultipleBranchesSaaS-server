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
import org.dromara.hrp.domain.vo.HrpAttendanceRecordsVo;
import org.dromara.hrp.domain.bo.HrpAttendanceRecordsBo;
import org.dromara.hrp.service.IHrpAttendanceRecordsService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 打卡记录
 *
 * @author Lion Li
 * @date 2025-08-23
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/hrp/attendanceRecords")
public class HrpAttendanceRecordsController extends BaseController {

    private final IHrpAttendanceRecordsService hrpAttendanceRecordsService;

    /**
     * 查询打卡记录列表
     */
    @SaCheckPermission("hrp:attendanceRecords:list")
    @GetMapping("/list")
    public TableDataInfo<HrpAttendanceRecordsVo> list(HrpAttendanceRecordsBo bo, PageQuery pageQuery) {
        return hrpAttendanceRecordsService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出打卡记录列表
     */
    @SaCheckPermission("hrp:attendanceRecords:export")
    @Log(title = "打卡记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HrpAttendanceRecordsBo bo, HttpServletResponse response) {
        List<HrpAttendanceRecordsVo> list = hrpAttendanceRecordsService.queryList(bo);
        ExcelUtil.exportExcel(list, "打卡记录", HrpAttendanceRecordsVo.class, response);
    }

    /**
     * 获取打卡记录详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("hrp:attendanceRecords:query")
    @GetMapping("/{id}")
    public R<HrpAttendanceRecordsVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(hrpAttendanceRecordsService.queryById(id));
    }

    /**
     * 新增打卡记录
     */
    @SaCheckPermission("hrp:attendanceRecords:add")
    @Log(title = "打卡记录", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody HrpAttendanceRecordsBo bo) {
        return toAjax(hrpAttendanceRecordsService.insertByBo(bo));
    }

    /**
     * 修改打卡记录
     */
    @SaCheckPermission("hrp:attendanceRecords:edit")
    @Log(title = "打卡记录", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody HrpAttendanceRecordsBo bo) {
        return toAjax(hrpAttendanceRecordsService.updateByBo(bo));
    }

    /**
     * 删除打卡记录
     *
     * @param ids 主键串
     */
    @SaCheckPermission("hrp:attendanceRecords:remove")
    @Log(title = "打卡记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(hrpAttendanceRecordsService.deleteWithValidByIds(List.of(ids), true));
    }
}
