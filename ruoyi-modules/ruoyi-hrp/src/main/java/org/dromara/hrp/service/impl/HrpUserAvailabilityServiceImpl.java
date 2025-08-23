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
import org.dromara.hrp.domain.bo.HrpUserAvailabilityBo;
import org.dromara.hrp.domain.vo.HrpUserAvailabilityVo;
import org.dromara.hrp.domain.HrpUserAvailability;
import org.dromara.hrp.mapper.HrpUserAvailabilityMapper;
import org.dromara.hrp.service.IHrpUserAvailabilityService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 员工可上班时段Service业务层处理
 *
 * @author Lion Li
 * @date 2025-08-23
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class HrpUserAvailabilityServiceImpl implements IHrpUserAvailabilityService {

    private final HrpUserAvailabilityMapper baseMapper;

    /**
     * 查询员工可上班时段
     *
     * @param id 主键
     * @return 员工可上班时段
     */
    @Override
    public HrpUserAvailabilityVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询员工可上班时段列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 员工可上班时段分页列表
     */
    @Override
    public TableDataInfo<HrpUserAvailabilityVo> queryPageList(HrpUserAvailabilityBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<HrpUserAvailability> lqw = buildQueryWrapper(bo);
        Page<HrpUserAvailabilityVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的员工可上班时段列表
     *
     * @param bo 查询条件
     * @return 员工可上班时段列表
     */
    @Override
    public List<HrpUserAvailabilityVo> queryList(HrpUserAvailabilityBo bo) {
        LambdaQueryWrapper<HrpUserAvailability> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<HrpUserAvailability> buildQueryWrapper(HrpUserAvailabilityBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<HrpUserAvailability> lqw = Wrappers.lambdaQuery();
        lqw.orderByAsc(HrpUserAvailability::getId);
        lqw.eq(bo.getUserId() != null, HrpUserAvailability::getUserId, bo.getUserId());
        lqw.eq(bo.getDayOfWeek() != null, HrpUserAvailability::getDayOfWeek, bo.getDayOfWeek());
        lqw.eq(bo.getStartTime() != null, HrpUserAvailability::getStartTime, bo.getStartTime());
        lqw.eq(bo.getEndTime() != null, HrpUserAvailability::getEndTime, bo.getEndTime());
        return lqw;
    }

    /**
     * 新增员工可上班时段
     *
     * @param bo 员工可上班时段
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(HrpUserAvailabilityBo bo) {
        HrpUserAvailability add = MapstructUtils.convert(bo, HrpUserAvailability.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改员工可上班时段
     *
     * @param bo 员工可上班时段
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(HrpUserAvailabilityBo bo) {
        HrpUserAvailability update = MapstructUtils.convert(bo, HrpUserAvailability.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(HrpUserAvailability entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除员工可上班时段信息
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
