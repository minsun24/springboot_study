package com.ohgiraffers.exceptionhandler;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/*")
public class ExceptionHandlerController {

    @GetMapping("simple-null")
    public String simpleNullPointerExceptionTest() {
        if (true) {
            throw new NullPointerException();   // 구현되어 있는 예외 - Unchecked Exception
        }

        return "/";
    }


    @GetMapping("simple-user")
    public String userExceptionTest() throws MemberRegistException {
        if (true) {
            // 수동적으로 예외 처리 해줘야 함 Checked Exception
            throw new MemberRegistException("당신은 안돼요!");
        }

        return "/";
    }

    @GetMapping("annotation-null")
    public String nullPointerExceptionHandler(){
        String str = null;
        str.charAt(0);
        return "/";
    }

//    @ExceptionHandler(NullPointerException.class)
//    public String nullPointerExceptionHandler(){
//        System.out.println("이 Controller에서 NullPointer 예외 발생 시 여기 는");
//
//        return "error/default";
//    }


    @GetMapping("annotation-user")
    public String userExceptionHandler(){
        System.out.println("이 COontroller에서 MemberRegist 예외 발생 시 여기 오는 지 확인");
        return "error/default";
    }
}



/*
* 1. */