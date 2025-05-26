package per.chowhound.hfut.domain.vo;

import lombok.Data;
import per.chowhound.hfut.domain.CarImg;

import java.util.Date;
import java.util.List;

@Data
public class CarVo {
    private Long carId;
    private UserVo buyer;
    private String url; //fileId
    private Date createTime;
    private Date updateTime;
    private UserVo user;
    private String type;
    private Integer price;
    private Integer mileage;
    private String ver;
    private String manufacturer;
    private Integer isReviewed;
    private Long reviewId;
    private List<CarImg> imgs;


    private Long recordId;

    private ReviewVo review;

    private Integer output;

    private String notes;
    private Integer years;
}
