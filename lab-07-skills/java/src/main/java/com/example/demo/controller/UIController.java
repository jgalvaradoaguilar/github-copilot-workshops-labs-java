package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UIController {

    @GetMapping("/")
    public String homePage() {
        return "forward:/index.html";
    }

    @GetMapping("/employees")
    public String employeesPage() {
        return "forward:/index.html";
    }
}
