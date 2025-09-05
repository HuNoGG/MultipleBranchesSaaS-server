package org.dromara.hrp.mapper;

import org.apache.ibatis.annotations.Param;
import org.dromara.hrp.domain.HrpSchedules;
import org.dromara.hrp.domain.vo.HrpSchedulesVo;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;

import java.time.LocalDate;
import java.util.List;

/**
 * 排班Mapper接口
 *
 * @author Lion Li
 * @date 2025-08-23
 */
public interface HrpSchedulesMapper extends BaseMapperPlus<HrpSchedules, HrpSchedulesVo> {


    /**
     * 查询排班计划列表
     * @param storeId 分店ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 结果
     */
    List<HrpSchedulesVo> selectSchedulePlanList(@Param("storeId") Long storeId,
                                                @Param("startDate") String startDate,
                                                @Param("endDate") String endDate);

    /**
     * 查询排班历史列表
     * @param storeId 分店ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 结果
     */
    List<HrpSchedulesVo> selectScheduleHistoryList(@Param("storeId") Long storeId,
                                                   @Param("startDate") String startDate,
                                                   @Param("endDate") String endDate);



    void deleteDraftSchedules(@Param("storeId") Long storeId,
                              @Param("startDate") LocalDate startDate,
                              @Param("endDate") LocalDate endDate);

    void insertBatch(@Param("list") List<HrpSchedules> list);

    /**
     * 将指定日期范围内的排班状态更新为 "PUBLISHED"
     * @param storeId 分店ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 影响的行数
     */
    int publishSchedules(@Param("storeId") Long storeId,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate);

    List<HrpSchedulesVo> selectScheduleHistoryBatch(@Param("scheduleIds") List<Long> scheduleIds);

    List<HrpSchedulesVo> selectRegularScheduleList( @Param("storeId") Long storeId,
                                                    @Param("startDate") String startDate,
                                                    @Param("endDate") String endDate);

    List<HrpSchedulesVo> selectBreakCoverageScheduleList(@Param("storeId") Long storeId,
                                                         @Param("startDate") String startDate,
                                                         @Param("endDate") String endDate);
}
