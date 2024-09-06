package ssafy.ddada;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class DdadaApplication {

    public static void main(String[] args) {
        SpringApplication.run(DdadaApplication.class, args);
    }

}
