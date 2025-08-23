package org.dromara.hrp.service;

import org.dromara.hrp.domain.vo.HrpUserStoreAccessVo;
import org.dromara.hrp.domain.bo.HrpUserStoreAccessBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 员工跨店权限Service接口
 *
 * @author Hzy
 * @date 2025-08-23
 */
public interface IHrpUserStoreAccessService {

    /**
     * 查询员工跨店权限
     *
     * @param userId 主键
     * @return 员工跨店权限
     */
    HrpUserStoreAccessVo queryById(Long userId);

    /**
     * 分页查询员工跨店权限列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 员工跨店权限分页列表
     */
    TableDataInfo<HrpUserStoreAccessVo> queryPageList(HrpUserStoreAccessBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的员工跨店权限列表
     *
     * @param bo 查询条件
     * @return 员工跨店权限列表
     */
    List<HrpUserStoreAccessVo> queryList(HrpUserStoreAccessBo bo);

    /**
     * 新增员工跨店权限
     *
     * @param bo 员工跨店权限
     * @return 是否新增成功
     */
    Boolean insertByBo(HrpUserStoreAccessBo bo);

    /**
     * 修改员工跨店权限
     *
     * @param bo 员工跨店权限
     * @return 是否修改成功
     */
    Boolean updateByBo(HrpUserStoreAccessBo bo);

    /**
     * 校验并批量删除员工跨店权限信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
