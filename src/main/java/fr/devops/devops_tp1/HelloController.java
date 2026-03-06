package fr.devops.devops_tp1;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/sayHello")
    public String sayHello() {
        return "hello";
    }
}
