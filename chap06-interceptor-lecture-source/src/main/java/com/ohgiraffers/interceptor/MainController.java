package com.ohgiraffers.interceptor;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {
    
    /*  설명. (/) 또는 (/main) 요청을 받아서, main.html 을 반환하는 역할  */
    @RequestMapping(value = {"/", "/main"})
    public String main() {
        return "main";
    }
}
