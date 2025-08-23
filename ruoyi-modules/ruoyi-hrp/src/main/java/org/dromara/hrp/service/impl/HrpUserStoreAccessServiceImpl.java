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
import org.dromara.hrp.domain.bo.HrpUserStoreAccessBo;
import org.dromara.hrp.domain.vo.HrpUserStoreAccessVo;
import org.dromara.hrp.domain.HrpUserStoreAccess;
import org.dromara.hrp.mapper.HrpUserStoreAccessMapper;
import org.dromara.hrp.service.IHrpUserStoreAccessService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 员工跨店权限Service业务层处理
 *
 * @author Hzy
 * @date 2025-08-23
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class HrpUserStoreAccessServiceImpl implements IHrpUserStoreAccessService {

    private final HrpUserStoreAccessMapper baseMapper;

    /**
     * 查询员工跨店权限
     *
     * @param userId 主键
     * @return 员工跨店权限
     */
    @Override
    public HrpUserStoreAccessVo queryById(Long userId){
        return baseMapper.selectVoById(userId);
    }

    /**
     * 分页查询员工跨店权限列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 员工跨店权限分页列表
     */
    @Override
    public TableDataInfo<HrpUserStoreAccessVo> queryPageList(HrpUserStoreAccessBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<HrpUserStoreAccess> lqw = buildQueryWrapper(bo);
        Page<HrpUserStoreAccessVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的员工跨店权限列表
     *
     * @param bo 查询条件
     * @return 员工跨店权限列表
     */
    @Override
    public List<HrpUserStoreAccessVo> queryList(HrpUserStoreAccessBo bo) {
        LambdaQueryWrapper<HrpUserStoreAccess> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<HrpUserStoreAccess> buildQueryWrapper(HrpUserStoreAccessBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<HrpUserStoreAccess> lqw = Wrappers.lambdaQuery();
        lqw.orderByAsc(HrpUserStoreAccess::getUserId);
        lqw.orderByAsc(HrpUserStoreAccess::getStoreId);
        return lqw;
    }

    /**
     * 新增员工跨店权限
     *
     * @param bo 员工跨店权限
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(HrpUserStoreAccessBo bo) {
        HrpUserStoreAccess add = MapstructUtils.convert(bo, HrpUserStoreAccess.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setUserId(add.getUserId());
        }
        return flag;
    }

    /**
     * 修改员工跨店权限
     *
     * @param bo 员工跨店权限
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(HrpUserStoreAccessBo bo) {
        HrpUserStoreAccess update = MapstructUtils.convert(bo, HrpUserStoreAccess.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(HrpUserStoreAccess entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除员工跨店权限信息
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
