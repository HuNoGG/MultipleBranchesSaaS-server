package org.dromara.hrp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.hrp.domain.HrpStoreEvents;
import org.dromara.hrp.domain.bo.HrpStoreEventsBo;
import org.dromara.hrp.domain.vo.HrpStoreEventsVo;
import org.dromara.hrp.mapper.HrpStoreEventsMapper;
import org.dromara.hrp.service.IHrpStoreEventsService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 分店特殊事件Service业务层处理
 *
 * @author Lion Li
 * @date 2025-08-23
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class HrpStoreEventsServiceImpl implements IHrpStoreEventsService
{

    private final HrpStoreEventsMapper baseMapper;

    /**
     * 查询分店特殊事件
     *
     * @param id 主键
     *
     * @return 分店特殊事件
     */
    @Override
    public HrpStoreEventsVo queryById(Long id)
    {
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询分店特殊事件列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     *
     * @return 分店特殊事件分页列表
     */
    @Override
    public TableDataInfo<HrpStoreEventsVo> queryPageList(HrpStoreEventsBo bo, PageQuery pageQuery)
    {
        LambdaQueryWrapper<HrpStoreEvents> lqw = buildQueryWrapper(bo);
        Page<HrpStoreEventsVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的分店特殊事件列表
     *
     * @param bo 查询条件
     *
     * @return 分店特殊事件列表
     */
    @Override
    public List<HrpStoreEventsVo> queryList(HrpStoreEventsBo bo)
    {
        LambdaQueryWrapper<HrpStoreEvents> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<HrpStoreEvents> buildQueryWrapper(HrpStoreEventsBo bo)
    {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<HrpStoreEvents> lqw = Wrappers.lambdaQuery();
        lqw.orderByAsc(HrpStoreEvents::getId);
        lqw.eq(bo.getStoreId() != null, HrpStoreEvents::getStoreId, bo.getStoreId());
        lqw.eq(bo.getEventDate() != null, HrpStoreEvents::getEventDate, bo.getEventDate());
        lqw.eq(StringUtils.isNotBlank(bo.getEventType()), HrpStoreEvents::getEventType, bo.getEventType());
        lqw.eq(StringUtils.isNotBlank(bo.getDescription()), HrpStoreEvents::getDescription, bo.getDescription());
        return lqw;
    }

    /**
     * 新增分店特殊事件
     *
     * @param bo 分店特殊事件
     *
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(HrpStoreEventsBo bo)
    {
        HrpStoreEvents add = MapstructUtils.convert(bo, HrpStoreEvents.class);
        validEntityBeforeSave(add);
        switch (add.getEventType())
        {
            case "all-day", "am-off", "pm-off":
                boolean flag = baseMapper.insert(add) > 0;
                if (flag)
                {
                    bo.setId(add.getId());
                }
                return flag;
            case "cancel-off":
                // 取消放假,删除对应事件
                return baseMapper.delete(Wrappers.<HrpStoreEvents>lambdaQuery().eq(HrpStoreEvents::getStoreId, bo.getStoreId())
                    .eq(HrpStoreEvents::getEventDate, bo.getEventDate())) > 0;
            default:
                return false;
        }
    }

    /**
     * 修改分店特殊事件
     *
     * @param bo 分店特殊事件
     *
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(HrpStoreEventsBo bo)
    {
        HrpStoreEvents update = MapstructUtils.convert(bo, HrpStoreEvents.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(HrpStoreEvents entity)
    {
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除分店特殊事件信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     *
     * @return 是否删除成功
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid)
    {
        if (isValid)
        {
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteByIds(ids) > 0;
    }
}
