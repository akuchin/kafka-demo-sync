package dev.kuca.kafkaspring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class KafkaSpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(KafkaSpringApplication.class, args);
    }

}
