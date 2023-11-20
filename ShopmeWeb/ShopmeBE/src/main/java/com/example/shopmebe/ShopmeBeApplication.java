package com.example.shopmebe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan({"com.shopme.common.entity", "com.shopme.common.dto", "com.shopme.common.annotations"})
public class ShopmeBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShopmeBeApplication.class, args);
    }
}
