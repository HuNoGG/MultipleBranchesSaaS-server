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
import org.dromara.hrp.domain.bo.HrpShiftBreaksBo;
import org.dromara.hrp.domain.vo.HrpShiftBreaksVo;
import org.dromara.hrp.domain.HrpShiftBreaks;
import org.dromara.hrp.mapper.HrpShiftBreaksMapper;
import org.dromara.hrp.service.IHrpShiftBreaksService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 班别休息时段Service业务层处理
 *
 * @author Lion Li
 * @date 2025-08-23
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class HrpShiftBreaksServiceImpl implements IHrpShiftBreaksService {

    private final HrpShiftBreaksMapper baseMapper;

    /**
     * 查询班别休息时段
     *
     * @param id 主键
     * @return 班别休息时段
     */
    @Override
    public HrpShiftBreaksVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询班别休息时段列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 班别休息时段分页列表
     */
    @Override
    public TableDataInfo<HrpShiftBreaksVo> queryPageList(HrpShiftBreaksBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<HrpShiftBreaks> lqw = buildQueryWrapper(bo);
        Page<HrpShiftBreaksVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的班别休息时段列表
     *
     * @param bo 查询条件
     * @return 班别休息时段列表
     */
    @Override
    public List<HrpShiftBreaksVo> queryList(HrpShiftBreaksBo bo) {
        LambdaQueryWrapper<HrpShiftBreaks> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<HrpShiftBreaks> buildQueryWrapper(HrpShiftBreaksBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<HrpShiftBreaks> lqw = Wrappers.lambdaQuery();
        lqw.orderByAsc(HrpShiftBreaks::getId);
        lqw.eq(bo.getShiftId() != null, HrpShiftBreaks::getShiftId, bo.getShiftId());
        lqw.eq(bo.getBreakStartTime() != null, HrpShiftBreaks::getBreakStartTime, bo.getBreakStartTime());
        lqw.eq(bo.getBreakEndTime() != null, HrpShiftBreaks::getBreakEndTime, bo.getBreakEndTime());
        return lqw;
    }

    /**
     * 新增班别休息时段
     *
     * @param bo 班别休息时段
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(HrpShiftBreaksBo bo) {
        HrpShiftBreaks add = MapstructUtils.convert(bo, HrpShiftBreaks.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改班别休息时段
     *
     * @param bo 班别休息时段
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(HrpShiftBreaksBo bo) {
        HrpShiftBreaks update = MapstructUtils.convert(bo, HrpShiftBreaks.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(HrpShiftBreaks entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除班别休息时段信息
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
