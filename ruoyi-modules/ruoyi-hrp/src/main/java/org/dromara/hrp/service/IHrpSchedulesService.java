package org.dromara.hrp.service;

import org.dromara.hrp.domain.dto.ScheduleGenerateDto;
import org.dromara.hrp.domain.dto.ScheduleGenerationResult;
import org.dromara.hrp.domain.dto.SchedulePlanDto;
import org.dromara.hrp.domain.dto.WeeklyScheduleDto;
import org.dromara.hrp.domain.vo.HrpSchedulesVo;
import org.dromara.hrp.domain.bo.HrpSchedulesBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 排班Service接口
 *
 * @author Lion Li
 * @date 2025-08-23
 */
public interface IHrpSchedulesService {



    /**
     * 查询排班计划
     */
    SchedulePlanDto getSchedulePlan(Long storeId, String startDate, String endDate);

    /**
     * 查询排班历史
     */
    SchedulePlanDto getScheduleHistory(Long storeId, String startDate, String endDate);

    /**
     * 获取周度排班数据（包含状态）
     */
    WeeklyScheduleDto getWeeklySchedule(Long storeId, String startDate, String endDate);

    /**
     * 发布指定周的排班
     */
    Boolean publishSchedule(Long storeId, String startDate, String endDate);

    /**
     * 智能生成排班计划
     */
    ScheduleGenerationResult generateSchedule(ScheduleGenerateDto dto);

    /**
     * 查询排班
     *
     * @param id 主键
     * @return 排班
     */
    HrpSchedulesVo queryById(Long id);

    /**
     * 分页查询排班列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 排班分页列表
     */
    TableDataInfo<HrpSchedulesVo> queryPageList(HrpSchedulesBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的排班列表
     *
     * @param bo 查询条件
     * @return 排班列表
     */
    List<HrpSchedulesVo> queryList(HrpSchedulesBo bo);

    /**
     * 新增排班
     *
     * @param bo 排班
     * @return 是否新增成功
     */
    Boolean insertByBo(HrpSchedulesBo bo);

    /**
     * 修改排班
     *
     * @param bo 排班
     * @return 是否修改成功
     */
    Boolean updateByBo(HrpSchedulesBo bo);

    /**
     * 校验并批量删除排班信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
