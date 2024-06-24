package com.example.shopmebe.rest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class MyController implements ErrorController {

    @GetMapping("/welcome")
    public String hello() {
        return "Hello World";
    }
}
