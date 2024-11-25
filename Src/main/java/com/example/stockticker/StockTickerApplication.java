package com.example.stockticker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableScheduling
@EnableEurekaClient    
public class StockTickerApplication {
    public static void main(String[] args) {
        SpringApplication.run(StockTickerApplication.class, args);
    }
}
