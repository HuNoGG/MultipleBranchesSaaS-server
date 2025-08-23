package org.dromara.hrp.service;

import org.dromara.hrp.domain.vo.HrpUserProfileVo;
import org.dromara.hrp.domain.bo.HrpUserProfileBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 员工档案扩展Service接口
 *
 * @author Hzy
 * @date 2025-08-23
 */
public interface IHrpUserProfileService {

    /**
     * 查询员工档案扩展
     *
     * @param userId 主键
     * @return 员工档案扩展
     */
    HrpUserProfileVo queryById(Long userId);

    /**
     * 分页查询员工档案扩展列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 员工档案扩展分页列表
     */
    TableDataInfo<HrpUserProfileVo> queryPageList(HrpUserProfileBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的员工档案扩展列表
     *
     * @param bo 查询条件
     * @return 员工档案扩展列表
     */
    List<HrpUserProfileVo> queryList(HrpUserProfileBo bo);

    /**
     * 新增员工档案扩展
     *
     * @param bo 员工档案扩展
     * @return 是否新增成功
     */
    Boolean insertByBo(HrpUserProfileBo bo);

    /**
     * 修改员工档案扩展
     *
     * @param bo 员工档案扩展
     * @return 是否修改成功
     */
    Boolean updateByBo(HrpUserProfileBo bo);

    /**
     * 校验并批量删除员工档案扩展信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    List<HrpUserProfileVo> queryListWithSkillsByStoreId(Long storeId);
}
