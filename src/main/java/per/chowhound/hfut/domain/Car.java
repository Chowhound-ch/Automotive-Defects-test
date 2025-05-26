package per.chowhound.hfut.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * @TableName car
 */
@TableName(value ="car")
@Data
public class Car extends BaseEntity implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long carId;

    private String url;

    private Long recordId;

    private Long userId;

    private String type;

    private String ver;

    private String manufacturer;

    private Integer price;

    private Long buyer;
    private Integer output;

    private String notes;

    private Integer years;

    private Integer mileage;

    private Long reviewId;

    // 0未审核，1已审核，2审核失败，3已下架
    private Integer isReviewed;

    private static final long serialVersionUID = 1L;
}