package org.dromara.hrp.service;

import org.dromara.hrp.domain.vo.HrpAttendanceRecordsVo;
import org.dromara.hrp.domain.bo.HrpAttendanceRecordsBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 打卡记录Service接口
 *
 * @author Lion Li
 * @date 2025-08-23
 */
public interface IHrpAttendanceRecordsService {

    /**
     * 查询打卡记录
     *
     * @param id 主键
     * @return 打卡记录
     */
    HrpAttendanceRecordsVo queryById(Long id);

    /**
     * 分页查询打卡记录列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 打卡记录分页列表
     */
    TableDataInfo<HrpAttendanceRecordsVo> queryPageList(HrpAttendanceRecordsBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的打卡记录列表
     *
     * @param bo 查询条件
     * @return 打卡记录列表
     */
    List<HrpAttendanceRecordsVo> queryList(HrpAttendanceRecordsBo bo);

    /**
     * 新增打卡记录
     *
     * @param bo 打卡记录
     * @return 是否新增成功
     */
    Boolean insertByBo(HrpAttendanceRecordsBo bo);

    /**
     * 修改打卡记录
     *
     * @param bo 打卡记录
     * @return 是否修改成功
     */
    Boolean updateByBo(HrpAttendanceRecordsBo bo);

    /**
     * 校验并批量删除打卡记录信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
