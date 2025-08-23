package org.dromara.hrp.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.dromara.hrp.domain.HrpLeaveRequests;
import org.dromara.hrp.domain.vo.HrpLeaveRequestsVo;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;

import java.time.LocalDate;
import java.util.List;

/**
 * 期望休假/排休记录Mapper接口
 *
 * @author Lion Li
 * @date 2025-08-23
 */
public interface HrpLeaveRequestsMapper extends BaseMapperPlus<HrpLeaveRequests, HrpLeaveRequestsVo> {


    @Select("select * from hrp_leave_requests where user_id in (#{userIds}) and leave_date >= #{startDate} and leave_date <= #{endDate} ")
    List<HrpLeaveRequestsVo> selectVoListByUsersAndDate(
        @Param("userIds") List<Long> userIds,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );

}
