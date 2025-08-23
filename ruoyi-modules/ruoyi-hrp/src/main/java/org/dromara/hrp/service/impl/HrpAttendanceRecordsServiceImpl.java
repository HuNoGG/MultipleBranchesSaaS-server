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
import org.dromara.hrp.domain.bo.HrpAttendanceRecordsBo;
import org.dromara.hrp.domain.vo.HrpAttendanceRecordsVo;
import org.dromara.hrp.domain.HrpAttendanceRecords;
import org.dromara.hrp.mapper.HrpAttendanceRecordsMapper;
import org.dromara.hrp.service.IHrpAttendanceRecordsService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 打卡记录Service业务层处理
 *
 * @author Lion Li
 * @date 2025-08-23
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class HrpAttendanceRecordsServiceImpl implements IHrpAttendanceRecordsService {

    private final HrpAttendanceRecordsMapper baseMapper;

    /**
     * 查询打卡记录
     *
     * @param id 主键
     * @return 打卡记录
     */
    @Override
    public HrpAttendanceRecordsVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询打卡记录列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 打卡记录分页列表
     */
    @Override
    public TableDataInfo<HrpAttendanceRecordsVo> queryPageList(HrpAttendanceRecordsBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<HrpAttendanceRecords> lqw = buildQueryWrapper(bo);
        Page<HrpAttendanceRecordsVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的打卡记录列表
     *
     * @param bo 查询条件
     * @return 打卡记录列表
     */
    @Override
    public List<HrpAttendanceRecordsVo> queryList(HrpAttendanceRecordsBo bo) {
        LambdaQueryWrapper<HrpAttendanceRecords> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<HrpAttendanceRecords> buildQueryWrapper(HrpAttendanceRecordsBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<HrpAttendanceRecords> lqw = Wrappers.lambdaQuery();
        lqw.orderByAsc(HrpAttendanceRecords::getId);
        lqw.eq(bo.getUserId() != null, HrpAttendanceRecords::getUserId, bo.getUserId());
        lqw.eq(bo.getScheduleId() != null, HrpAttendanceRecords::getScheduleId, bo.getScheduleId());
        lqw.eq(bo.getClockInTime() != null, HrpAttendanceRecords::getClockInTime, bo.getClockInTime());
        lqw.eq(bo.getClockOutTime() != null, HrpAttendanceRecords::getClockOutTime, bo.getClockOutTime());
        lqw.eq(bo.getRecordDate() != null, HrpAttendanceRecords::getRecordDate, bo.getRecordDate());
        return lqw;
    }

    /**
     * 新增打卡记录
     *
     * @param bo 打卡记录
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(HrpAttendanceRecordsBo bo) {
        HrpAttendanceRecords add = MapstructUtils.convert(bo, HrpAttendanceRecords.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改打卡记录
     *
     * @param bo 打卡记录
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(HrpAttendanceRecordsBo bo) {
        HrpAttendanceRecords update = MapstructUtils.convert(bo, HrpAttendanceRecords.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(HrpAttendanceRecords entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除打卡记录信息
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
