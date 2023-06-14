package com.dimash.spring.restApp.controller;

import org.springframework.web.bind.annotation.*;

@RestController // Controller + ResponseBody
@RequestMapping("/api")
public class RestFirstController {

//    @ResponseBody // Spring поймет что это представление не лежит в файле html
    @GetMapping("/sayHello")          // вернет просто String
    public String sayHello() {
        return "Hello World";
    }
}
