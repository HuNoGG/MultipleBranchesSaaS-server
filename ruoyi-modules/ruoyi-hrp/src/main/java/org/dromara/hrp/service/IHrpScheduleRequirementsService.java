package org.dromara.hrp.service;

import jakarta.validation.constraints.NotNull;
import org.dromara.hrp.domain.dto.AllRequirementsDto;
import org.dromara.hrp.domain.dto.DailyRequirementsDto;
import org.dromara.hrp.domain.vo.HrpScheduleRequirementsVo;
import org.dromara.hrp.domain.bo.HrpScheduleRequirementsBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 每日人力需求Service接口
 *
 * @author Lion Li
 * @date 2025-08-23
 */
public interface IHrpScheduleRequirementsService {

    /**
     * 查询每日人力需求
     *
     * @param id 主键
     * @return 每日人力需求
     */
    HrpScheduleRequirementsVo queryById(Long id);

    /**
     * 分页查询每日人力需求列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 每日人力需求分页列表
     */
    TableDataInfo<HrpScheduleRequirementsVo> queryPageList(HrpScheduleRequirementsBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的每日人力需求列表
     *
     * @param bo 查询条件
     * @return 每日人力需求列表
     */
    List<HrpScheduleRequirementsVo> queryList(HrpScheduleRequirementsBo bo);

    /**
     * 新增每日人力需求
     *
     * @param bo 每日人力需求
     * @return 是否新增成功
     */
    Boolean insertByBo(HrpScheduleRequirementsBo bo);

    /**
     * 修改每日人力需求
     *
     * @param bo 每日人力需求
     * @return 是否修改成功
     */
    Boolean updateByBo(HrpScheduleRequirementsBo bo);

    /**
     * 校验并批量删除每日人力需求信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 保存每日人力需求
     * @param dto 包含需求配置的数据
     */
    void saveRequirements(DailyRequirementsDto dto);
    /**
     * 根据店铺ID获取所有类型的人力需求
     * @param storeId 店铺ID
     * @return 包含所有类型需求的DTO
     */
    AllRequirementsDto getAllRequirementsByStoreId(@NotNull Long storeId);
}
