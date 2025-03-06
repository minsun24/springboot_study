package com.ohgiraffers.chap01;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfiguration {

    // @Configuration 클래스는 경로 필요 없이 @Value 어노테이션으로 properties 파일에 접근 가능
    @Value("${test.value}")
    public String testValue;

    /* 설명. @Value는 시스템의 환경변수도 불러올 수 있다. */
    @Value("${username}")
    public String userName;
    //    userName: Playdata
    // 필기. 컴퓨터 환경변수도 @Value()로 가져올 수 있다.
    // cmd > $set (컴퓨터 환경변수 목록이 출력됨)

    //
    @Value("${test.age}")
    public int age;

    @Bean
    public Object properReadtTest() {
        System.out.println("testValue: " + testValue);
        System.out.println("userName: " + userName);
        System.out.println("age = " + age);
        
        return new Object();
    }
//    testValue: hello world
//    userName: Playdata




}



/*
  필기.   key, value 형태로 스프링 설정
    * DB 연결 정보
    * JPA, Mybatis
    * 챗지피티 키 등 ...
*
* */