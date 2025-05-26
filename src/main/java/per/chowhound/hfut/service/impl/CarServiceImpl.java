package per.chowhound.hfut.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.bean.BeanUtils;
import com.ruoyi.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import per.chowhound.hfut.domain.*;
import per.chowhound.hfut.domain.vo.CarSearchVo;
import per.chowhound.hfut.domain.vo.CarVo;
import per.chowhound.hfut.domain.vo.ReviewVo;
import per.chowhound.hfut.domain.vo.UserVo;
import per.chowhound.hfut.service.CarImgService;
import per.chowhound.hfut.service.CarService;
import per.chowhound.hfut.mapper.CarMapper;
import org.springframework.stereotype.Service;
import per.chowhound.hfut.service.IFileService;
import per.chowhound.hfut.service.ReviewService;
import per.chowhound.hfut.utils.UserUtils;

import javax.annotation.Resource;
import java.beans.Transient;
import java.util.*;
import java.util.stream.Collectors;


/**
* @author hh825
* @description 针对表【car】的数据库操作Service实现
* @createDate 2025-04-12 10:53:09
*/
@Service
public class CarServiceImpl extends ServiceImpl<CarMapper, Car>
    implements CarService{
    public static final String ALL = "全部";
    @Resource
    private ISysUserService userService;
    @Resource
    private IFileService fileService;
    @Resource
    private ReviewService reviewService;
    @Resource
    private CarImgService imgService;
    @Autowired
    private CarImgService carImgService;

    @Override
    public List<CarVo> list(CarSearchVo carSearchVo) {
        return this.listByUserId(null, carSearchVo);
    }

    @Override
    public void saveCar(CarVo carVo) {
        Car car = new Car();
        BeanUtils.copyProperties(carVo, car);
        car.setUserId(carVo.getUser().getUserId());
        Review review = new Review();
        BeanUtils.copyProperties(carVo.getReview(), review);
        reviewService.saveOrUpdate(review);
        car.setReviewId(review.getReviewId());

        this.saveOrUpdate(car);
        List<CarImg> imgs = carVo.getImgs();
        imgs.forEach(item -> item.setCarId(car.getCarId()));
        imgService.saveOrUpdateBatch(imgs);
    }

    @Override
    public CarVo getCarById(Long carId) {
        Car car = this.getById(carId);
        CarVo carVo = new CarVo();
        BeanUtils.copyProperties(car, carVo);
        SysUser user = userService.getById(car.getUserId());
        carVo.setUser(UserUtils.getUser(user));

        LambdaQueryWrapper<CarImg> mapper = new LambdaQueryWrapper<>();
        mapper.eq(CarImg::getCarId, car.getCarId());
        List<CarImg> list = carImgService.list(mapper);
        carVo.setImgs(list);

        ReviewVo review = reviewService.getReviewById(car.getReviewId());
        carVo.setReview(review);

        return carVo;
    }

    @Transient
    @Override
    public void setReview(CarVo car) {
        LambdaUpdateWrapper<Car> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Car::getCarId, car.getCarId())
                .set(Objects.equals(car.getIsReviewed(), 4), Car::getBuyer, car.getUser().getUserId())
                .set(Car::getIsReviewed, car.getIsReviewed());
        this.update(new Car(), wrapper);

        LambdaUpdateWrapper<Review> reviewWrapper = new LambdaUpdateWrapper<>();
        reviewWrapper.eq(Review::getReviewId, car.getRecordId())
                .set(Review::getIsReviewed, car.getIsReviewed())
                .set(Review::getReviewedAdmin, car.getUser().getUserId());
        reviewService.update(new Review(), reviewWrapper);

    }

    @Override
    public List<CarVo> listByUserId(Long userId, CarSearchVo carSearchVo) {
        QueryWrapper<Car> mapper = new QueryWrapper<>();
        mapper.eq(carSearchVo.getIsReviewed() != null, "is_reviewed", carSearchVo.getIsReviewed())
                .eq(carSearchVo.getCarId() != null, "car_id", carSearchVo.getCarId())
                .eq(StrUtil.isNotBlank(carSearchVo.getManufacture()) && !ALL.equals(carSearchVo.getManufacture()), "manufacturer", carSearchVo.getManufacture())
                .eq(StrUtil.isNotBlank(carSearchVo.getVer()) && !ALL.equals(carSearchVo.getVer()), "ver", carSearchVo.getVer())
                .eq(userId != null, "user_id", userId)
                .between(carSearchVo.getPriceMin() != null && carSearchVo.getPriceMax() != null,
                        "price", carSearchVo.getPriceMin(), carSearchVo.getPriceMax())
                .between(carSearchVo.getMileageMin() != null &&  carSearchVo.getMileageMax() != null,
                        "mileage", carSearchVo.getMileageMin(), carSearchVo.getMileageMax())
                .ge(carSearchVo.getCreateTime() != null, "createTime", carSearchVo.getCreateTime())
                .orderBy(StrUtil.isNotBlank(carSearchVo.getOrder()) && StrUtil.isNotBlank(carSearchVo.getProp()),
                        "ascending".equals(carSearchVo.getOrder()), carSearchVo.getProp());

        Set<Long> userIds = new HashSet<>();
        Set<Long> carIds = new HashSet<>();
        List<Car> carList = list(mapper);
        if (carList.isEmpty()) {
            return Collections.emptyList();
        }
        carList.forEach(car-> {
            userIds.add(car.getUserId());
            carIds.add(car.getCarId());
        });
        Map<Long, SysUser> userMap;
        if (!userIds.isEmpty()) {
            userMap = userService.listByIds(userIds).stream().collect(Collectors.toMap(SysUser::getUserId, user -> user));
        } else {
            userMap = Collections.emptyMap();
        }

        return carList.stream().map(car -> {
            CarVo carVo = new CarVo();
            BeanUtils.copyProperties(car, carVo);
            SysUser sysUser = userMap.get(car.getUserId());
            UserVo userVo = new UserVo();
            BeanUtils.copyProperties(sysUser, userVo);
            carVo.setUser(userVo);
//            carVo.setImgs(imgMap.get(carVo.getCarId()));
            return carVo;
        }).toList();
    }

    @Override
    public List<CarVo> listByBuyer(Long userId, CarSearchVo carSearchVo) {
        QueryWrapper<Car> mapper = new QueryWrapper<>();
        mapper.eq(carSearchVo.getIsReviewed() != null, "is_reviewed", carSearchVo.getIsReviewed())
                .eq(StrUtil.isNotBlank(carSearchVo.getManufacture()) && !ALL.equals(carSearchVo.getManufacture()), "manufacturer", carSearchVo.getManufacture())
                .eq(StrUtil.isNotBlank(carSearchVo.getVer()) && !ALL.equals(carSearchVo.getVer()), "ver", carSearchVo.getVer())
                .eq(userId != null, "buyer", userId)
                .eq("is_reviewed", 4)
                .between(carSearchVo.getPriceMin() != null && carSearchVo.getPriceMax() != null,
                        "price", carSearchVo.getPriceMin(), carSearchVo.getPriceMax())
                .between(carSearchVo.getMileageMin() != null &&  carSearchVo.getMileageMax() != null,
                        "mileage", carSearchVo.getMileageMin(), carSearchVo.getMileageMax())
                .ge(carSearchVo.getCreateTime() != null, "createTime", carSearchVo.getCreateTime())
                .orderBy(StrUtil.isNotBlank(carSearchVo.getOrder()) && StrUtil.isNotBlank(carSearchVo.getProp()),
                        "ascending".equals(carSearchVo.getOrder()), carSearchVo.getProp());

        Set<Long> userIds = new HashSet<>();
        Set<Long> carIds = new HashSet<>();
        List<Car> carList = list(mapper);
        if (carList.isEmpty()) {
            return Collections.emptyList();
        }
        carList.forEach(car-> {
            userIds.add(car.getUserId());
            userIds.add(car.getBuyer());
            carIds.add(car.getCarId());
        });
        Map<Long, SysUser> userMap;
        if (!userIds.isEmpty()) {
            userMap = userService.listByIds(userIds).stream().collect(Collectors.toMap(SysUser::getUserId, user -> user));
        } else {
            userMap = Collections.emptyMap();
        }

        return carList.stream().map(car -> {
            CarVo carVo = new CarVo();
            BeanUtils.copyProperties(car, carVo);
            SysUser sysUser = userMap.get(car.getUserId());
            UserVo userVo = new UserVo();
            BeanUtils.copyProperties(sysUser, userVo);
            carVo.setUser(userVo);
            if (car.getBuyer() != null) {
                SysUser sysUserBuyer = userMap.get(car.getBuyer());
                UserVo buyerVo = new UserVo();
                BeanUtils.copyProperties(sysUserBuyer, buyerVo);
                carVo.setBuyer(buyerVo);
            }
//            carVo.setImgs(imgMap.get(carVo.getCarId()));
            return carVo;
        }).toList();
    }


}




