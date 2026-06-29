package org.myspring.backend.controllers;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/message")
class TestController {

    @GetMapping
    String getMessage() {
        return "Hello World!";
    }
}