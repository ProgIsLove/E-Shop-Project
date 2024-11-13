package com.example.shopmefe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan({"com.shopme.common.entity"})
@EnableJpaRepositories(basePackages = "com.example.shopmebe.setting.country")
public class ShopmeFeApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShopmeFeApplication.class, args);
    }

}
