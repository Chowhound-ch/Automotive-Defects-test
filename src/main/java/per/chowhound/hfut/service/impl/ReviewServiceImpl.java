package per.chowhound.hfut.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.utils.bean.BeanUtils;
import com.ruoyi.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import per.chowhound.hfut.domain.Review;
import per.chowhound.hfut.domain.vo.ReviewVo;
import per.chowhound.hfut.domain.vo.UserVo;
import per.chowhound.hfut.service.ReviewService;
import per.chowhound.hfut.mapper.ReviewMapper;
import org.springframework.stereotype.Service;
import per.chowhound.hfut.utils.UserUtils;

import javax.annotation.Resource;

/**
* @author hh825
* @description 针对表【review】的数据库操作Service实现
* @createDate 2025-04-12 10:53:42
*/
@Service
public class ReviewServiceImpl extends ServiceImpl<ReviewMapper, Review>
    implements ReviewService{

    @Resource
    private ISysUserService userService;

    @Override
    public ReviewVo getReviewById(Long reviewId) {
        Review review = this.getById(reviewId);
        if (review == null) {
            return null;
        }
        ReviewVo reviewVo = new ReviewVo();
        BeanUtils.copyProperties(review, reviewVo);
        if (review.getReviewedAdmin() != null) {
            UserVo user = UserUtils.getUser(userService.getById(review.getReviewedAdmin()));
            reviewVo.setReviewedAdmin(user);
        }

        return reviewVo;
    }
}




