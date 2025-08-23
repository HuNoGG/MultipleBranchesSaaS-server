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
import org.dromara.hrp.domain.bo.HrpShiftsBo;
import org.dromara.hrp.domain.vo.HrpShiftsVo;
import org.dromara.hrp.domain.HrpShifts;
import org.dromara.hrp.mapper.HrpShiftsMapper;
import org.dromara.hrp.service.IHrpShiftsService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 班别设定Service业务层处理
 *
 * @author Lion Li
 * @date 2025-08-23
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class HrpShiftsServiceImpl implements IHrpShiftsService {

    private final HrpShiftsMapper baseMapper;

    /**
     * 查询班别设定
     *
     * @param id 主键
     * @return 班别设定
     */
    @Override
    public HrpShiftsVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询班别设定列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 班别设定分页列表
     */
    @Override
    public TableDataInfo<HrpShiftsVo> queryPageList(HrpShiftsBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<HrpShifts> lqw = buildQueryWrapper(bo);
        Page<HrpShiftsVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的班别设定列表
     *
     * @param bo 查询条件
     * @return 班别设定列表
     */
    @Override
    public List<HrpShiftsVo> queryList(HrpShiftsBo bo) {
        LambdaQueryWrapper<HrpShifts> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<HrpShifts> buildQueryWrapper(HrpShiftsBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<HrpShifts> lqw = Wrappers.lambdaQuery();
        lqw.orderByAsc(HrpShifts::getId);
        lqw.eq(bo.getStoreId() != null, HrpShifts::getStoreId, bo.getStoreId());
        lqw.like(StringUtils.isNotBlank(bo.getName()), HrpShifts::getName, bo.getName());
        lqw.eq(StringUtils.isNotBlank(bo.getCode()), HrpShifts::getCode, bo.getCode());
        lqw.eq(bo.getStartTime() != null, HrpShifts::getStartTime, bo.getStartTime());
        lqw.eq(bo.getEndTime() != null, HrpShifts::getEndTime, bo.getEndTime());
        lqw.eq(bo.getIsCrossDay() != null, HrpShifts::getIsCrossDay, bo.getIsCrossDay());
        lqw.eq(StringUtils.isNotBlank(bo.getColorCode()), HrpShifts::getColorCode, bo.getColorCode());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), HrpShifts::getStatus, bo.getStatus());
        return lqw;
    }

    /**
     * 新增班别设定
     *
     * @param bo 班别设定
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(HrpShiftsBo bo) {
        HrpShifts add = MapstructUtils.convert(bo, HrpShifts.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改班别设定
     *
     * @param bo 班别设定
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(HrpShiftsBo bo) {
        HrpShifts update = MapstructUtils.convert(bo, HrpShifts.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(HrpShifts entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除班别设定信息
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
