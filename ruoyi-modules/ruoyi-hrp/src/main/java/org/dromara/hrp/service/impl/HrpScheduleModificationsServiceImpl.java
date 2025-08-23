package org.dromara.hrp.service.impl;

import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.dromara.hrp.domain.bo.HrpScheduleModificationsBo;
import org.dromara.hrp.domain.vo.HrpScheduleModificationsVo;
import org.dromara.hrp.domain.HrpScheduleModifications;
import org.dromara.hrp.mapper.HrpScheduleModificationsMapper;
import org.dromara.hrp.service.IHrpScheduleModificationsService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 排班修改记录Service业务层处理
 *
 * @author Lion Li
 * @date 2025-08-23
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class HrpScheduleModificationsServiceImpl implements IHrpScheduleModificationsService {

    private final HrpScheduleModificationsMapper baseMapper;

    /**
     * 查询排班修改记录
     *
     * @param id 主键
     * @return 排班修改记录
     */
    @Override
    public HrpScheduleModificationsVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询排班修改记录列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 排班修改记录分页列表
     */
    @Override
    public TableDataInfo<HrpScheduleModificationsVo> queryPageList(HrpScheduleModificationsBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<HrpScheduleModifications> lqw = buildQueryWrapper(bo);
        Page<HrpScheduleModificationsVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的排班修改记录列表
     *
     * @param bo 查询条件
     * @return 排班修改记录列表
     */
    @Override
    public List<HrpScheduleModificationsVo> queryList(HrpScheduleModificationsBo bo) {
        LambdaQueryWrapper<HrpScheduleModifications> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<HrpScheduleModifications> buildQueryWrapper(HrpScheduleModificationsBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<HrpScheduleModifications> lqw = Wrappers.lambdaQuery();
        lqw.orderByAsc(HrpScheduleModifications::getId);
        lqw.eq(bo.getScheduleId() != null, HrpScheduleModifications::getScheduleId, bo.getScheduleId());
        lqw.eq(bo.getOriginalUserId() != null, HrpScheduleModifications::getOriginalUserId, bo.getOriginalUserId());
        lqw.eq(bo.getNewUserId() != null, HrpScheduleModifications::getNewUserId, bo.getNewUserId());
        lqw.eq(StringUtils.isNotBlank(bo.getChangeType()), HrpScheduleModifications::getChangeType, bo.getChangeType());
        lqw.eq(bo.getChangedBy() != null, HrpScheduleModifications::getChangedBy, bo.getChangedBy());
        lqw.eq(bo.getChangeTime() != null, HrpScheduleModifications::getChangeTime, bo.getChangeTime());
        return lqw;
    }

    /**
     * 新增排班修改记录
     *
     * @param bo 排班修改记录
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(HrpScheduleModificationsBo bo) {
        HrpScheduleModifications add = MapstructUtils.convert(bo, HrpScheduleModifications.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改排班修改记录
     *
     * @param bo 排班修改记录
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(HrpScheduleModificationsBo bo) {
        HrpScheduleModifications update = MapstructUtils.convert(bo, HrpScheduleModifications.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(HrpScheduleModifications entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除排班修改记录信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteByIds(ids) > 0;
    }
}
