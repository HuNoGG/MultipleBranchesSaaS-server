package org.dromara.hrp.service;

import org.dromara.hrp.domain.vo.HrpCrossStoreCostAllocationVo;
import org.dromara.hrp.domain.bo.HrpCrossStoreCostAllocationBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 跨店成本分摊设定Service接口
 *
 * @author Lion Li
 * @date 2025-08-23
 */
public interface IHrpCrossStoreCostAllocationService {

    /**
     * 查询跨店成本分摊设定
     *
     * @param id 主键
     * @return 跨店成本分摊设定
     */
    HrpCrossStoreCostAllocationVo queryById(Long id);

    /**
     * 分页查询跨店成本分摊设定列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 跨店成本分摊设定分页列表
     */
    TableDataInfo<HrpCrossStoreCostAllocationVo> queryPageList(HrpCrossStoreCostAllocationBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的跨店成本分摊设定列表
     *
     * @param bo 查询条件
     * @return 跨店成本分摊设定列表
     */
    List<HrpCrossStoreCostAllocationVo> queryList(HrpCrossStoreCostAllocationBo bo);

    /**
     * 新增跨店成本分摊设定
     *
     * @param bo 跨店成本分摊设定
     * @return 是否新增成功
     */
    Boolean insertByBo(HrpCrossStoreCostAllocationBo bo);

    /**
     * 修改跨店成本分摊设定
     *
     * @param bo 跨店成本分摊设定
     * @return 是否修改成功
     */
    Boolean updateByBo(HrpCrossStoreCostAllocationBo bo);

    /**
     * 校验并批量删除跨店成本分摊设定信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
