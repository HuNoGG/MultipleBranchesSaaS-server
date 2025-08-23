package org.dromara.hrp.domain.vo;

import org.dromara.hrp.domain.HrpUserStoreAccess;
import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import org.dromara.common.excel.annotation.ExcelDictFormat;
import org.dromara.common.excel.convert.ExcelDictConvert;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;



/**
 * 员工跨店权限视图对象 hrp_user_store_access
 *
 * @author Hzy
 * @date 2025-08-23
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = HrpUserStoreAccess.class)
public class HrpUserStoreAccessVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 员工 ID
     */
    @ExcelProperty(value = "员工 ID")
    private Long userId;

    /**
     * 授权支援的分店 ID
     */
    @ExcelProperty(value = "授权支援的分店 ID")
    private Long storeId;


}
