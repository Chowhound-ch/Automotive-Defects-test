package per.chowhound.hfut.service;

import per.chowhound.hfut.domain.Car;
import com.baomidou.mybatisplus.extension.service.IService;
import per.chowhound.hfut.domain.vo.CarSearchVo;
import per.chowhound.hfut.domain.vo.CarVo;

import java.util.List;


/**
* @author hh825
* @description 针对表【car】的数据库操作Service
* @createDate 2025-04-12 10:53:09
*/
public interface CarService extends IService<Car> {

    List<CarVo> list(CarSearchVo carSearchVo);

    void saveCar(CarVo carVo);

    CarVo getCarById(Long carId);

    void setReview(CarVo car);

    List<CarVo> listByUserId(Long userId, CarSearchVo carSearchVo);

    List<CarVo> listByBuyer(Long userId, CarSearchVo carSearchVo);
}
