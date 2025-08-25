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
import org.dromara.hrp.domain.vo.HrpSkillsVo;
import org.dromara.hrp.domain.vo.HrpUserAvailabilityVo;
import org.dromara.hrp.service.IHrpSkillsService;
import org.dromara.hrp.service.IHrpUserAvailabilityService;
import org.springframework.stereotype.Service;
import org.dromara.hrp.domain.bo.HrpUserProfileBo;
import org.dromara.hrp.domain.vo.HrpUserProfileVo;
import org.dromara.hrp.domain.HrpUserProfile;
import org.dromara.hrp.mapper.HrpUserProfileMapper;
import org.dromara.hrp.service.IHrpUserProfileService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Collection;

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
}
