package com.example.shopmebe;

import com.example.shopmebe.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EntityScan({"com.shopme.common.entity", "com.example.shopmebe.user"})
public class ShopmeBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShopmeBeApplication.class, args);
    }

}
