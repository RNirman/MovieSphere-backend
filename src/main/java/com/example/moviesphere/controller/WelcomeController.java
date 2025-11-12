package com.example.moviesphere.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // Marks the class as a Spring REST Controller
public class WelcomeController {

    // Maps HTTP GET requests to the "/" path
    @GetMapping("/")
    public String welcome() {
        return "Welcome to the Spring Boot Movie Info API!";
    }

    // A simple endpoint to test functionality
    @GetMapping("/api/status")
    public String getStatus() {
        return "API Status: OK";
    }
}