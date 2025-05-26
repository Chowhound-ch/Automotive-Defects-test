package per.chowhound.hfut;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@MapperScan("per.chowhound.hfut.mapper")
@SpringBootApplication
public class AutomotiveDefects {
    public static void main(String[] args) {
        SpringApplication.run(AutomotiveDefects.class, args);
    }
}