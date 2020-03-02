package edu.sandau;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class OnlineExamApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnlineExamApplication.class, args);
    }

}
