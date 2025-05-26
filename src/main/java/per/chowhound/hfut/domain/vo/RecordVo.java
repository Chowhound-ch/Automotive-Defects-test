package per.chowhound.hfut.domain.vo;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import per.chowhound.hfut.domain.Result;

import java.io.Serial;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class RecordVo extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long recordId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long type;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private List<Result> results;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")

    private Long userId;
    private UserVo user;

    private Long model;

    private String note;
}
