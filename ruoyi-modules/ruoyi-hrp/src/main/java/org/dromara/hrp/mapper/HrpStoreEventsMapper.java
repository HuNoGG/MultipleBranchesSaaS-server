package org.dromara.hrp.mapper;

import org.apache.ibatis.annotations.Param;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;
import org.dromara.hrp.domain.HrpStoreEvents;
import org.dromara.hrp.domain.vo.HrpStoreEventsVo;

import java.time.LocalDate;
import java.util.List;

/**
 * 分店特殊事件Mapper接口
 *
 * @author Lion Li
 * @date 2025-08-23
 */
public interface HrpStoreEventsMapper extends BaseMapperPlus<HrpStoreEvents, HrpStoreEventsVo>
{

    List<HrpStoreEventsVo> selectVoListByStoreAndDate(@Param("storeId") Long storeId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
