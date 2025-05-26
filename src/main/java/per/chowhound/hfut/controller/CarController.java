package per.chowhound.hfut.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.framework.web.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import per.chowhound.hfut.domain.Car;
import per.chowhound.hfut.domain.vo.CarSearchVo;
import per.chowhound.hfut.domain.vo.CarVo;
import per.chowhound.hfut.domain.vo.ReviewVo;
import per.chowhound.hfut.service.CarService;
import per.chowhound.hfut.service.ReviewService;
import per.chowhound.hfut.utils.UserUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/car")
public class CarController extends BaseController {
    @Resource
    private CarService carService;
    @Resource
    private TokenService tokenService;
    @Autowired
    private ReviewService reviewService;

    @GetMapping("/list")
    public TableDataInfo list(CarSearchVo carSearchVo) {
//        startPage();
        carSearchVo.setIsReviewed(1);
        List<CarVo> carVos = carService.list(carSearchVo);
        return getDataTable(carVos);
    }
    @GetMapping("/listAll")
    public TableDataInfo listAll(CarSearchVo carSearchVo) {
//        startPage();
        List<CarVo> carVos = carService.list(carSearchVo);
        return getDataTable(carVos);
    }
    @GetMapping("/listOrder")
    public TableDataInfo listOrder(CarSearchVo carSearchVo) {
//        startPage();
        LoginUser user = tokenService.getLoginUser(ServletUtils.getRequest());
        List<CarVo> carVos = carService.listByBuyer(user.getUserId(), carSearchVo);
        return getDataTable(carVos);
    }
    @GetMapping("/listMine")
    public TableDataInfo listMine(CarSearchVo carSearchVo) {
//        startPage();
        LoginUser user = tokenService.getLoginUser(ServletUtils.getRequest());
        List<CarVo> carVos = carService.listByUserId(user.getUserId(), carSearchVo);
        return getDataTable(carVos);
    }
    @GetMapping("/{carId}")
    public AjaxResult list(@PathVariable Long carId) {
//        startPage();
        CarVo carVo = carService.getCarById(carId);
        return success(carVo);
    }
    @PostMapping("/add")
    public AjaxResult add(@RequestBody CarVo car) {
        LoginUser user = tokenService.getLoginUser(ServletUtils.getRequest());
        car.setUser(UserUtils.getUser(user.getUser()));
        ReviewVo review = car.getReview();
        review.setReviewId(car.getReviewId());
        review.setIsReviewed(0);
        car.setIsReviewed(0);
        carService.saveCar(car);
        return success();
    }
    @PostMapping("/review")
    public AjaxResult review(@RequestBody CarVo car) {
        LoginUser user = tokenService.getLoginUser(ServletUtils.getRequest());
        car.setUser(UserUtils.getUser(user.getUser()));
        carService.setReview(car);
        return success();
    }
}
