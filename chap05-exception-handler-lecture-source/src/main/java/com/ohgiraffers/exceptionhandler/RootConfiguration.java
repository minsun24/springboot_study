package com.ohgiraffers.exceptionhandler;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import java.util.Properties;

@Configuration
public class RootConfiguration {

    @Bean
    public SimpleMappingExceptionResolver simpleMappingExceptionResolver() {
        SimpleMappingExceptionResolver exceptionResolver = new SimpleMappingExceptionResolver();
        // 간단하게 Exception을 맵핑해주는 해결사 (?)

        Properties props = new Properties();
        // 필기. Exception 맵핑 ...
        props.setProperty("java.lang.NullPointerException", "error/nullPointer");
        props.setProperty("MemberRegisteredException", "error/memberRegisteredException");


        /* 설명. 전체 예외 관련되어 설정하기  */
        /* 설명. 1. 예외에 따른 페이지 설정한 것 활용*/
        exceptionResolver.setExceptionMappings(props);

        /* 설명. 2. 그 외 나머지 예외에 대한 default 페이지 설정 */
        exceptionResolver.setDefaultErrorView("error/default");

        /* 설명. 3. 예외 객체를 담기 위한 Attribute key값 설정(화면에서 예외 메시지 확인 시 사용할 key값) */
        exceptionResolver.setExceptionAttribute("exceptionMessage");

        return exceptionResolver;
        
    }
}
