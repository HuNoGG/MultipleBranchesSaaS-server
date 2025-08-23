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
import org.dromara.hrp.domain.bo.HrpAttendanceExceptionsBo;
import org.dromara.hrp.domain.vo.HrpAttendanceExceptionsVo;
import org.dromara.hrp.domain.HrpAttendanceExceptions;
import org.dromara.hrp.mapper.HrpAttendanceExceptionsMapper;
import org.dromara.hrp.service.IHrpAttendanceExceptionsService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 考勤异常Service业务层处理
 *
 * @author Lion Li
 * @date 2025-08-23
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class HrpAttendanceExceptionsServiceImpl implements IHrpAttendanceExceptionsService {

    private final HrpAttendanceExceptionsMapper baseMapper;

    /**
     * 查询考勤异常
     *
     * @param id 主键
     * @return 考勤异常
     */
    @Override
    public HrpAttendanceExceptionsVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询考勤异常列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 考勤异常分页列表
     */
    @Override
    public TableDataInfo<HrpAttendanceExceptionsVo> queryPageList(HrpAttendanceExceptionsBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<HrpAttendanceExceptions> lqw = buildQueryWrapper(bo);
        Page<HrpAttendanceExceptionsVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的考勤异常列表
     *
     * @param bo 查询条件
     * @return 考勤异常列表
     */
    @Override
    public List<HrpAttendanceExceptionsVo> queryList(HrpAttendanceExceptionsBo bo) {
        LambdaQueryWrapper<HrpAttendanceExceptions> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<HrpAttendanceExceptions> buildQueryWrapper(HrpAttendanceExceptionsBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<HrpAttendanceExceptions> lqw = Wrappers.lambdaQuery();
        lqw.orderByAsc(HrpAttendanceExceptions::getId);
        lqw.eq(bo.getUserId() != null, HrpAttendanceExceptions::getUserId, bo.getUserId());
        lqw.eq(bo.getExceptionDate() != null, HrpAttendanceExceptions::getExceptionDate, bo.getExceptionDate());
        lqw.eq(StringUtils.isNotBlank(bo.getExceptionType()), HrpAttendanceExceptions::getExceptionType, bo.getExceptionType());
        lqw.eq(bo.getOriginalClockIn() != null, HrpAttendanceExceptions::getOriginalClockIn, bo.getOriginalClockIn());
        lqw.eq(bo.getOriginalClockOut() != null, HrpAttendanceExceptions::getOriginalClockOut, bo.getOriginalClockOut());
        lqw.eq(bo.getCorrectedClockIn() != null, HrpAttendanceExceptions::getCorrectedClockIn, bo.getCorrectedClockIn());
        lqw.eq(bo.getCorrectedClockOut() != null, HrpAttendanceExceptions::getCorrectedClockOut, bo.getCorrectedClockOut());
        lqw.eq(StringUtils.isNotBlank(bo.getApprovalStatus()), HrpAttendanceExceptions::getApprovalStatus, bo.getApprovalStatus());
        lqw.eq(bo.getApprovedBy() != null, HrpAttendanceExceptions::getApprovedBy, bo.getApprovedBy());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), HrpAttendanceExceptions::getStatus, bo.getStatus());
        return lqw;
    }

    /**
     * 新增考勤异常
     *
     * @param bo 考勤异常
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(HrpAttendanceExceptionsBo bo) {
        HrpAttendanceExceptions add = MapstructUtils.convert(bo, HrpAttendanceExceptions.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改考勤异常
     *
     * @param bo 考勤异常
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(HrpAttendanceExceptionsBo bo) {
        HrpAttendanceExceptions update = MapstructUtils.convert(bo, HrpAttendanceExceptions.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(HrpAttendanceExceptions entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除考勤异常信息
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
