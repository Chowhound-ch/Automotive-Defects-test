package per.chowhound.hfut.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

import java.util.Date;

/**
 * 识别结果对象 result
 * 
 * @author chowhound
 * @date 2025-03-19
 */
@Data
public class Result extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    @TableId(type = IdType.AUTO)
    private Long resultId;

    private Long fileId;
    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long recordId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String url;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long defectId;


    private Long userId;

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("resultId", getResultId())
            .append("recordId", getRecordId())
            .append("url", getUrl())
            .append("defectId", getDefectId())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("isDelete", getIsDelete())
            .toString();
    }
}
