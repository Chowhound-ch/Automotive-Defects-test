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
 * @TableName review
 */
@TableName(value ="review")
@Data
public class Review extends BaseEntity implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long reviewId;

    private String licImg;

    private String insurance;

    private String cert;

    private String invoice;

    private Long reviewedAdmin;

    private String number;
    private Integer isReviewed;

    private static final long serialVersionUID = 1L;
}