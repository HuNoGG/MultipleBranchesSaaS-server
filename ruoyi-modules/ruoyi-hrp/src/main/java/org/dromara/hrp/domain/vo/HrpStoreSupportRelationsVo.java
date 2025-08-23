package org.dromara.hrp.domain.vo;

import org.dromara.hrp.domain.HrpStoreSupportRelations;
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
 * 分店支援关系视图对象 hrp_store_support_relations
 *
 * @author Lion Li
 * @date 2025-08-23
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = HrpStoreSupportRelations.class)
public class HrpStoreSupportRelationsVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 请求支援的分店ID
     */
    @ExcelProperty(value = "请求支援的分店ID")
    private Long requestingStoreId;

    /**
     * 可提供支援的分店 ID
     */
    @ExcelProperty(value = "可提供支援的分店 ID")
    private Long supportingStoreId;


}
