package org.dromara.hrp.service.impl;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.dromara.common.core.exception.ServiceException;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hrp.domain.HrpSchedules;
import org.dromara.hrp.domain.HrpShifts;
import org.dromara.hrp.domain.HrpSkills;
import org.dromara.hrp.mapper.HrpSchedulesMapper;
import org.dromara.hrp.mapper.HrpShiftsMapper;
import org.dromara.hrp.mapper.HrpSkillsMapper;
import org.springframework.stereotype.Service;
import org.dromara.hrp.domain.bo.HrpScheduleModificationsBo;
import org.dromara.hrp.domain.vo.HrpScheduleModificationsVo;
import org.dromara.hrp.domain.HrpScheduleModifications;
import org.dromara.hrp.mapper.HrpScheduleModificationsMapper;
import org.dromara.hrp.service.IHrpScheduleModificationsService;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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
    private final HrpSchedulesMapper schedulesMapper;
    private final HrpShiftsMapper shiftsMapper;
    private final HrpSkillsMapper skillsMapper;

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

    /**
     * 批量新增排班修改记录 (核心逻辑)
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insertBatchByBo(List<HrpScheduleModificationsBo> boList) {
        if (boList == null || boList.isEmpty()) {
            return true;
        }

        for (HrpScheduleModificationsBo bo : boList) {
            // 1. 解析remark中的JSON数据
            JSONObject remarkJson = JSONUtil.parseObj(bo.getRemark());
            String position = remarkJson.getStr("position");
            JSONArray timeRange = remarkJson.getJSONArray("timeRange");
            String startTimeStr = timeRange.get(0).toString();
            String endTimeStr = timeRange.get(1).toString();

            // 2. 根据信息查找对应的班次ID和技能ID
            Long shiftId = findShiftId(bo.getStoreId(), startTimeStr, endTimeStr);
            Long skillId = findSkillId(Long.valueOf(position));

            // 3. 创建一个新的 HrpSchedules 记录
            HrpSchedules newSchedule = new HrpSchedules();
            newSchedule.setStoreId(bo.getStoreId());
            newSchedule.setScheduleDate(bo.getScheduleDate());
            newSchedule.setUserId(bo.getNewUserId());
            newSchedule.setShiftId(shiftId);
            newSchedule.setSkillId(skillId);
            newSchedule.setStatus("PUBLISHED"); // 增补的班次直接视为已发布
            schedulesMapper.insert(newSchedule);

            // 4. 创建 HrpScheduleModifications 记录并关联新的排班ID
            HrpScheduleModifications modification = MapstructUtils.convert(bo, HrpScheduleModifications.class);
            modification.setScheduleId(newSchedule.getId()); // 关联新创建的排班ID
            modification.setOriginalUserId(bo.getNewUserId()); // 增补时，原员工和新员工是同一个人
            baseMapper.insert(modification);
        }
        return true;
    }

    private Long findShiftId(Long storeId, String startTime, String endTime) {
        // 在实际项目中，为了性能，可以考虑缓存班次信息
        List<HrpShifts> shifts = shiftsMapper.selectList(
            new LambdaQueryWrapper<HrpShifts>()
                .eq(HrpShifts::getStoreId, storeId)
        );
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        for (HrpShifts shift : shifts) {
            String dbStartTime = shift.getStartTime().toInstant().atZone(ZoneId.systemDefault()).toLocalTime().format(formatter);
            String dbEndTime = shift.getEndTime().toInstant().atZone(ZoneId.systemDefault()).toLocalTime().format(formatter);
            if (startTime.equals(dbStartTime) && endTime.equals(dbEndTime)) {
                return shift.getId();
            }
        }
        throw new ServiceException("未找到匹配的班次: " + startTime + "-" + endTime);
    }

    private Long findSkillId(Long skillId) {
        HrpSkills skill = skillsMapper.selectOne(
            new LambdaQueryWrapper<HrpSkills>()
                .eq(HrpSkills::getId, skillId)
        );
        if (skill == null) {
            throw new ServiceException("未找到名为 '" + skillId + "' 的技能/岗位");
        }
        return skill.getId();
    }

}
