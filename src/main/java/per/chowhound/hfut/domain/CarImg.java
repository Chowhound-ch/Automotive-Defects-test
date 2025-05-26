package per.chowhound.hfut.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @TableName car_img
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value ="car_img")
@Data
public class CarImg extends BaseEntity implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long imgId;

    private String url;

    private Long carId;

    private static final long serialVersionUID = 1L;
}