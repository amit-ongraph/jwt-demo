package com.demo.jwtsecurity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication(scanBasePackages = "com.demo.jwtsecurity")
public class JwtDemoApplication  {

    public static void main(String[] args) throws Exception{
        SpringApplication.run(JwtDemoApplication.class, args);
    }

    @GetMapping("/")
    public String sayHello(){
        return "Hello!";
    }
}
