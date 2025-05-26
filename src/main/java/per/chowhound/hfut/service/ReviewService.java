package per.chowhound.hfut.service;

import per.chowhound.hfut.domain.Review;
import com.baomidou.mybatisplus.extension.service.IService;
import per.chowhound.hfut.domain.vo.ReviewVo;

/**
* @author hh825
* @description 针对表【review】的数据库操作Service
* @createDate 2025-04-12 10:53:42
*/
public interface ReviewService extends IService<Review> {

    ReviewVo getReviewById(Long reviewId);
}
