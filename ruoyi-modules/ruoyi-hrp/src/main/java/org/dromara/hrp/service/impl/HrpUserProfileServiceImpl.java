package org.dromara.hrp.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
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
import org.dromara.hrp.domain.HrpUserAvailability;
import org.dromara.hrp.domain.HrpUserSkills;
import org.dromara.hrp.domain.dto.UserProfileExtendedUpdateDTO;
import org.dromara.hrp.domain.vo.HrpSkillsVo;
import org.dromara.hrp.domain.vo.HrpUserAvailabilityVo;
import org.dromara.hrp.mapper.HrpUserAvailabilityMapper;
import org.dromara.hrp.mapper.HrpUserSkillsMapper;
import org.dromara.hrp.service.IHrpSkillsService;
import org.dromara.hrp.service.IHrpUserAvailabilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.dromara.hrp.domain.bo.HrpUserProfileBo;
import org.dromara.hrp.domain.vo.HrpUserProfileVo;
import org.dromara.hrp.domain.HrpUserProfile;
import org.dromara.hrp.mapper.HrpUserProfileMapper;
import org.dromara.hrp.service.IHrpUserProfileService;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * 员工档案扩展Service业务层处理
 *
 * @author Hzy
 * @date 2025-08-23
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class HrpUserProfileServiceImpl implements IHrpUserProfileService {

    private final HrpUserProfileMapper baseMapper;
    private final IHrpSkillsService hrpSkillsService;
    private final IHrpUserAvailabilityService hrpUserAvailabilityService;
    @Autowired
    private HrpUserSkillsMapper userSkillsMapper;
    @Autowired
    private HrpUserAvailabilityMapper userAvailabilityMapper;

    /**
     * 查询员工档案扩展
     *
     * @param userId 主键
     * @return 员工档案扩展
     */
    @Override
    public HrpUserProfileVo queryById(Long userId){
        return baseMapper.selectVoById(userId);
    }

    /**
     * 分页查询员工档案扩展列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 员工档案扩展分页列表
     */
    @Override
    public TableDataInfo<HrpUserProfileVo> queryPageList(HrpUserProfileBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<HrpUserProfile> lqw = buildQueryWrapper(bo);
        Page<HrpUserProfileVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的员工档案扩展列表
     *
     * @param bo 查询条件
     * @return 员工档案扩展列表
     */
    @Override
    public List<HrpUserProfileVo> queryList(HrpUserProfileBo bo) {
        LambdaQueryWrapper<HrpUserProfile> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<HrpUserProfile> buildQueryWrapper(HrpUserProfileBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<HrpUserProfile> lqw = Wrappers.lambdaQuery();
        lqw.orderByAsc(HrpUserProfile::getUserId);
        lqw.eq(StringUtils.isNotBlank(bo.getEmployeeType()), HrpUserProfile::getEmployeeType, bo.getEmployeeType());
        lqw.eq(bo.getMainStoreId() != null, HrpUserProfile::getMainStoreId, bo.getMainStoreId());
        lqw.eq(bo.getPriorityScore() != null, HrpUserProfile::getPriorityScore, bo.getPriorityScore());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), HrpUserProfile::getStatus, bo.getStatus());
        return lqw;
    }

    /**
     * 新增员工档案扩展
     *
     * @param bo 员工档案扩展
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(HrpUserProfileBo bo) {
        HrpUserProfile add = MapstructUtils.convert(bo, HrpUserProfile.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setUserId(add.getUserId());
        }
        return flag;
    }

    /**
     * 修改员工档案扩展
     *
     * @param bo 员工档案扩展
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(HrpUserProfileBo bo) {
        HrpUserProfile update = MapstructUtils.convert(bo, HrpUserProfile.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(HrpUserProfile entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除员工档案扩展信息
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

    @Override
    public List<HrpUserProfileVo> queryListWithSkillsByStoreId(Long storeId)
    {
        // 1. 根据storeId查询员工列表
        List<HrpUserProfileVo> userProfileList = baseMapper.selectVoListByStoreId(storeId);
        // 2. 遍历员工列表,查询每个员工的技能与可上班时间
        for (HrpUserProfileVo userProfile : userProfileList) {
            List<HrpSkillsVo> skillList = hrpSkillsService.queryListWithUserSkillByUserId(userProfile.getUserId());
            userProfile.setSkills(skillList);
            // 3. 查询每个员工的可上班时间
            List<HrpUserAvailabilityVo> availableTimes = hrpUserAvailabilityService.queryListWithUserAvailabilityByUserId(userProfile.getUserId());
            userProfile.setAvailableTimes(availableTimes);
        }
        return userProfileList;
    }

    @Override
    public List<HrpUserProfileVo> queryAvailableSubstitutes(Long storeId, Long skillId, LocalDate scheduleDate, Long originalUserId) {
        return baseMapper.selectAvailableSubstitutes(storeId, skillId, scheduleDate, originalUserId);
    }

    /**
     * 保存用户档案的拓展信息。
     * 这是一个事务性操作,会同时更新用户的基本档案、技能关联和可用时间。
     *
     * @param dto 包含所有待更新信息的DTO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveExtendedInfo(UserProfileExtendedUpdateDTO dto) {
        Long userId = dto.getUserId();

        // 1. 更新 HrpUserProfile 表
        HrpUserProfile userProfile = baseMapper.selectOne(Wrappers.<HrpUserProfile>lambdaQuery().eq(HrpUserProfile::getUserId, userId));
        if (userProfile == null) {
            // 如果档案不存在,可以创建一个新的,或者抛出异常,取决于业务需求
            // 这里我们选择抛出异常,因为通常用户档案应由系统用户同步创建
            throw new ServiceException("用户档案不存在: " + userId);
        }
        userProfile.setEmployeeType(dto.getEmployeeType());
        userProfile.setMainStoreId(dto.getMainStoreId());
        userProfile.setPriorityScore(dto.getPriorityScore().longValue());
        userProfile.setStatus(dto.getStatus());
        userProfile.setRemark(dto.getRemark());
        baseMapper.update(userProfile, Wrappers.<HrpUserProfile>lambdaUpdate().eq(HrpUserProfile::getUserId, userId));


        // 2. 更新 HrpUserSkills 表 (采用对比差异的方式,高效更新)
        updateUserSkills(userId, dto.getSkills());

        // 3. 更新 HrpUserAvailability 表 (采用先删后增的方式,简单可靠)
        updateUserAvailabilities(userId, dto.getAvailableTimes());
    }

    /**
     * 更新用户技能关联
     * @param userId  用户ID
     * @param newSkillIds 前端提交的最新的技能ID列表
     */
    private void updateUserSkills(Long userId, List<Long> newSkillIds) {
        // 获取当前用户在数据库中已有的技能ID列表
        List<Long> currentSkillIds = userSkillsMapper
            .selectList(new LambdaQueryWrapper<HrpUserSkills>().eq(HrpUserSkills::getUserId, userId))
            .stream()
            .map(HrpUserSkills::getSkillId)
            .collect(Collectors.toList());

        // 计算需要删除的技能 (存在于旧列表但不存在于新列表)
        List<Long> skillsToRemove = currentSkillIds.stream()
            .filter(id -> !newSkillIds.contains(id))
            .collect(Collectors.toList());

        if (!CollectionUtils.isEmpty(skillsToRemove)) {
            // TODO: // 调用Mapper进行批量删除
            userSkillsMapper.delete(new LambdaQueryWrapper<HrpUserSkills>()
                .eq(HrpUserSkills::getUserId, userId)
                .in(HrpUserSkills::getSkillId, skillsToRemove));
        }

        // 计算需要新增的技能 (存在于新列表但不存在于旧列表)
        List<HrpUserSkills> skillsToAdd = newSkillIds.stream()
            .filter(id -> !currentSkillIds.contains(id))
            .map(skillId -> {
                HrpUserSkills userSkill = new HrpUserSkills();
                userSkill.setUserId(userId);
                userSkill.setSkillId(skillId);
                return userSkill;
            })
            .collect(Collectors.toList());

        if (!CollectionUtils.isEmpty(skillsToAdd)) {
            // TODO: // 调用Mapper进行批量新增
            userSkillsMapper.insertBatch(skillsToAdd);
        }
    }

    /**
     * 更新用户可用时间
     * @param userId 用户ID
     * @param availableTimes 前端提交的最新的可用时间列表
     */
    private void updateUserAvailabilities(Long userId, List<UserProfileExtendedUpdateDTO.UserAvailabilityDTO> availableTimes) {
        // 1. TODO: 先删除该用户所有已有的可用时间记录
        userAvailabilityMapper.delete(new LambdaQueryWrapper<HrpUserAvailability>().eq(HrpUserAvailability::getUserId, userId));

        // 2. 如果新的列表不为空, 则进行批量插入
        if (!CollectionUtils.isEmpty(availableTimes)) {
            List<HrpUserAvailability> availabilitiesToAdd = availableTimes.stream().map(dto -> {
                HrpUserAvailability availability = new HrpUserAvailability();
                availability.setUserId(userId);
                // TODO: 确保 weekDayMap 能正确转换星期字符串到数字, 此处假设 dayOfWeek 为数字
                // 如果 dayOfWeek 是 "MONDAY" 等字符串, 需要转换
                availability.setDayOfWeek(dto.getDayOfWeek());
                availability.setStartTime(DateUtil.parseTime(dto.getStartTime()));
                availability.setEndTime(DateUtil.parseTime(dto.getEndTime()));
                return availability;
            }).collect(Collectors.toList());

            // TODO: // 调用Mapper进行批量新增
            if (!availabilitiesToAdd.isEmpty()) {
                userAvailabilityMapper.insertBatch(availabilitiesToAdd);
            }
        }
    }

    /**
     * 辅助方法: 将星期字符串(如"星期一")转换为数字(1-7)
     * @param dayOfWeekStr 星期字符串
     * @return 对应的数字, 1=周一, 7=周日
     */
    private Integer parseDayOfWeek(String dayOfWeekStr) {
        // 此处应根据您系统中的 `weekDayMap` 的实际定义进行转换
        // 以下是一个示例实现
        switch (dayOfWeekStr) {
            case "星期一": return 1;
            case "星期二": return 2;
            case "星期三": return 3;
            case "星期四": return 4;
            case "星期五": return 5;
            case "星期六": return 6;
            case "星期日": return 7;
            default: return 0; // 或者抛出异常
        }
    }

}
