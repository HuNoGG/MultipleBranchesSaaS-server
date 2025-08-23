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
import org.dromara.hrp.domain.bo.HrpLeaveApplicationsBo;
import org.dromara.hrp.domain.vo.HrpLeaveApplicationsVo;
import org.dromara.hrp.domain.HrpLeaveApplications;
import org.dromara.hrp.mapper.HrpLeaveApplicationsMapper;
import org.dromara.hrp.service.IHrpLeaveApplicationsService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 正式请假申请Service业务层处理
 *
 * @author Lion Li
 * @date 2025-08-23
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class HrpLeaveApplicationsServiceImpl implements IHrpLeaveApplicationsService {

    private final HrpLeaveApplicationsMapper baseMapper;

    /**
     * 查询正式请假申请
     *
     * @param id 主键
     * @return 正式请假申请
     */
    @Override
    public HrpLeaveApplicationsVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询正式请假申请列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 正式请假申请分页列表
     */
    @Override
    public TableDataInfo<HrpLeaveApplicationsVo> queryPageList(HrpLeaveApplicationsBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<HrpLeaveApplications> lqw = buildQueryWrapper(bo);
        Page<HrpLeaveApplicationsVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的正式请假申请列表
     *
     * @param bo 查询条件
     * @return 正式请假申请列表
     */
    @Override
    public List<HrpLeaveApplicationsVo> queryList(HrpLeaveApplicationsBo bo) {
        LambdaQueryWrapper<HrpLeaveApplications> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<HrpLeaveApplications> buildQueryWrapper(HrpLeaveApplicationsBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<HrpLeaveApplications> lqw = Wrappers.lambdaQuery();
        lqw.orderByAsc(HrpLeaveApplications::getId);
        lqw.eq(bo.getUserId() != null, HrpLeaveApplications::getUserId, bo.getUserId());
        lqw.eq(StringUtils.isNotBlank(bo.getLeaveType()), HrpLeaveApplications::getLeaveType, bo.getLeaveType());
        lqw.eq(bo.getStartTime() != null, HrpLeaveApplications::getStartTime, bo.getStartTime());
        lqw.eq(bo.getEndTime() != null, HrpLeaveApplications::getEndTime, bo.getEndTime());
        lqw.eq(bo.getLeaveDays() != null, HrpLeaveApplications::getLeaveDays, bo.getLeaveDays());
        lqw.eq(StringUtils.isNotBlank(bo.getReason()), HrpLeaveApplications::getReason, bo.getReason());
        lqw.eq(StringUtils.isNotBlank(bo.getAttachmentUrl()), HrpLeaveApplications::getAttachmentUrl, bo.getAttachmentUrl());
        lqw.eq(StringUtils.isNotBlank(bo.getApprovalStatus()), HrpLeaveApplications::getApprovalStatus, bo.getApprovalStatus());
        lqw.eq(bo.getApprovedBy() != null, HrpLeaveApplications::getApprovedBy, bo.getApprovedBy());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), HrpLeaveApplications::getStatus, bo.getStatus());
        return lqw;
    }

    /**
     * 新增正式请假申请
     *
     * @param bo 正式请假申请
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(HrpLeaveApplicationsBo bo) {
        HrpLeaveApplications add = MapstructUtils.convert(bo, HrpLeaveApplications.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改正式请假申请
     *
     * @param bo 正式请假申请
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(HrpLeaveApplicationsBo bo) {
        HrpLeaveApplications update = MapstructUtils.convert(bo, HrpLeaveApplications.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(HrpLeaveApplications entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除正式请假申请信息
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
