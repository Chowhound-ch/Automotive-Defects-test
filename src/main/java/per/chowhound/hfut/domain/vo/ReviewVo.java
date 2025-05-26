package per.chowhound.hfut.domain.vo;

import lombok.Data;

import java.util.Date;

@Data
public class ReviewVo  {
    private Long reviewId;

    private String licImg;

    private String insurance;

    private String cert;

    private String invoice;

    private UserVo reviewedAdmin;

    private String number;

    private Integer isReviewed;

    private Date createTime;

    private Date updateTime;
}
