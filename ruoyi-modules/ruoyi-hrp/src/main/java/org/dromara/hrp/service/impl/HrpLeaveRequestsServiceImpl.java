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
import org.dromara.hrp.domain.bo.HrpLeaveRequestsBo;
import org.dromara.hrp.domain.vo.HrpLeaveRequestsVo;
import org.dromara.hrp.domain.HrpLeaveRequests;
import org.dromara.hrp.mapper.HrpLeaveRequestsMapper;
import org.dromara.hrp.service.IHrpLeaveRequestsService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 期望休假/排休记录Service业务层处理
 *
 * @author Lion Li
 * @date 2025-08-23
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class HrpLeaveRequestsServiceImpl implements IHrpLeaveRequestsService {

    private final HrpLeaveRequestsMapper baseMapper;

    /**
     * 查询期望休假/排休记录
     *
     * @param id 主键
     * @return 期望休假/排休记录
     */
    @Override
    public HrpLeaveRequestsVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询期望休假/排休记录列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 期望休假/排休记录分页列表
     */
    @Override
    public TableDataInfo<HrpLeaveRequestsVo> queryPageList(HrpLeaveRequestsBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<HrpLeaveRequests> lqw = buildQueryWrapper(bo);
        Page<HrpLeaveRequestsVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的期望休假/排休记录列表
     *
     * @param bo 查询条件
     * @return 期望休假/排休记录列表
     */
    @Override
    public List<HrpLeaveRequestsVo> queryList(HrpLeaveRequestsBo bo) {
        LambdaQueryWrapper<HrpLeaveRequests> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<HrpLeaveRequests> buildQueryWrapper(HrpLeaveRequestsBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<HrpLeaveRequests> lqw = Wrappers.lambdaQuery();
        lqw.orderByAsc(HrpLeaveRequests::getId);
        lqw.eq(bo.getUserId() != null, HrpLeaveRequests::getUserId, bo.getUserId());
        lqw.eq(bo.getLeaveDate() != null, HrpLeaveRequests::getLeaveDate, bo.getLeaveDate());
        lqw.eq(StringUtils.isNotBlank(bo.getApprovalStatus()), HrpLeaveRequests::getApprovalStatus, bo.getApprovalStatus());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), HrpLeaveRequests::getStatus, bo.getStatus());
        return lqw;
    }

    /**
     * 新增期望休假/排休记录
     *
     * @param bo 期望休假/排休记录
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(HrpLeaveRequestsBo bo) {
        HrpLeaveRequests add = MapstructUtils.convert(bo, HrpLeaveRequests.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改期望休假/排休记录
     *
     * @param bo 期望休假/排休记录
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(HrpLeaveRequestsBo bo) {
        HrpLeaveRequests update = MapstructUtils.convert(bo, HrpLeaveRequests.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(HrpLeaveRequests entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除期望休假/排休记录信息
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
