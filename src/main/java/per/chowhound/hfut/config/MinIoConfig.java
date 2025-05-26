package per.chowhound.hfut.config;


import io.minio.MinioClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinIoConfig {


    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint("http://nas.com:9001") // 地址为服务器ip+端口号
                .credentials("O84xYuJLPOfowUzdrk3z", "8DTax5rnvDaiWiojowiEhYRgbdkrgNjLj8Xbz0d6")
                .build();
    }


}
