package org.dromara.hrp.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.dromara.hrp.domain.HrpShifts;
import org.dromara.hrp.domain.bo.HrpShiftsBo;
import org.dromara.hrp.domain.vo.HrpShiftsVo;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;

import java.util.List;

/**
 * 班别设定Mapper接口
 *
 * @author Lion Li
 * @date 2025-08-23
 */
public interface HrpShiftsMapper extends BaseMapperPlus<HrpShifts, HrpShiftsVo> {



    @Select("select * from hrp_shifts where store_id = #{storeId} ")
    List<HrpShiftsVo> selectVoListByStoreId(@Param("storeId") Long storeId);

    List<HrpShiftsVo> queryShiftsAndRestTime(HrpShiftsBo bo);
}
