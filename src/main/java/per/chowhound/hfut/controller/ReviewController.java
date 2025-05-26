package per.chowhound.hfut.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import per.chowhound.hfut.domain.vo.ReviewVo;
import per.chowhound.hfut.service.ReviewService;

import javax.annotation.Resource;

@RestController
@RequestMapping("/review")
public class ReviewController extends BaseController {
    @Resource
    private ReviewService reviewService;

    @GetMapping("/{reviewId}")
    public AjaxResult getReview(@PathVariable Long reviewId) {
        ReviewVo review = reviewService.getReviewById(reviewId);
        if (review == null) {
            return error("Review not found");
        }
        return success(review);
    }

}
