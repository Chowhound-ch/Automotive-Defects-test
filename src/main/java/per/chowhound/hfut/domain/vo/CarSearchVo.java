package per.chowhound.hfut.domain.vo;

import lombok.Data;

import java.util.Date;

@Data
public class CarSearchVo {
    /*
        manufacture: '全部',
        ver: '全部',
        price_min:0,
        price_max: 100,
        mileage_min:0,
        mileage_max:20,
        create_time: '',
        keyword: ''*/
    private Long carId;
    private String manufacture;
    private String ver;
    private Integer priceMin;
    private Integer priceMax;
    private Integer mileageMin;
    private Integer mileageMax;
    private Date createTime;
    private String keyword;
    private Integer isReviewed;
    private String prop;
    private String order;
}
