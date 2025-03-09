package com.ohgiraffers.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

/*  설명.  Interceptor 인터셉터
*       1. Bean 을 다룰 수 있다. (필터와의 차이점 - 필터는 bean을 가지고 전후처리를 할 수 없다.)
*       2. 핸들러 메소드 직전/직후에 동작하여 전후 처리를 한다.
*           (forward 및 redirect 일지라도 매번 핸들러 메소드 실행 시 동작. ex) 다른 컨트롤러로 갈 때도 무조건 동작)
*       3. 이후에 진행될 핸들러 메소드의 동작 여부를 제어할 수 있다. (preHandle의 반환값이 false일 때 -> 동작x,true일 때만 동작)
*           -
*           <인터셉터를 사용하는 경우(목적)>
*           : 로그인 체크(스프링 secutiry 사용 - 로그인 실행 시 내부 인터셉터가 작동하여 서비스 계층의 메소드를 실행 -> 컨트롤러를 건너뛰고 실행. 이때 사용됨), 권한 체크,
*             프로그램 실행 시간 계산 작업 로그 처리(로그를 따로 저장-엑셀 -> 저장된 파일로 로그 분석),
*             업로드 파일 사이즈 처리(리사이즈-> 작은 용량으로 축소. 프론트와의 협업?필요한 경우),
*             로케일(지역 - 요청한 사람의 국적,지역 파악 가능) 설정 등
* */

//@Configuration  // 필기. Component 계열이므로, Bean으로 등록됨
@Component
public class StopwatchInterceptor implements HandlerInterceptor {
    // 필기. 서비스 계층 주입 - 생성자 주입
    InterceptorTestService testService;

    @Autowired
    public StopwatchInterceptor(InterceptorTestService testService) {
        this.testService = testService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("preHandle 호출함 ... (핸들러 메소드 이전) ");

        /*  설명.  Service bean에 있는 메소드 호출    */
        testService.test();

        HttpSession session = request.getSession(); // 서버 세션 -사숑자 전용 사물함 생성

        /*  설명.  requestHeader 에서 지역 추출 */
        Locale locale = request.getLocale();
        System.out.println("locale = " + locale);
        if("ko_KR".equals(locale.toString())) System.out.println("한국인이시군요");

        long startTime = System.currentTimeMillis();
        request.setAttribute("startTime", startTime);

        
        return true;

        /*  설명.  핸들러 인터셉터는 bean을 활용할 수 있으며, 이후 핸들러 메소드 동작 여부를 반환형으로 제어할 수 있다. */
//        return false;
//        preHandle 호출함 ... (핸들러 메소드 이전)
//        ...
        /*  필기. prehandle만 호출됨. 핸들러 메소드 동작 X, 핸들러 메소드 이후인 postHandle 메서드도 동작X
              -> preHandle 메소드 false 반환 시, 컨트롤러로 가지 못하게 막는 것과 같음.
                (1차 방어선-필터, 2차 방어선-인터셉터)
              -> 좋지 않은 흐름.
        *     */
    }
    /* 필기. preHandle의 리턴타입이 boolean형
            핸들러 메소드 동작 전에 호출되는 메소드
            false값을 리턴하면 핸들러 메소드가 동작하지 않음.
            preHandle의 반환값에 따라 핸들러 메소드의 동작 유무가 정해짐.
            -
            request, response 객체가 파라미터로 전달받음 - 누가, 어떤 IP로 ... 로그 남길 수 있음.

    */



    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        System.out.println("postHandle 호출함... (핸들러 메소드 이후)");

        long startTime = (Long) request.getAttribute("startTime"); // Object 반환하므로 다운캐스팅(Long) 필요
        long endTime = System.currentTimeMillis();

        /*  필기. postHandle 이후에 ViewResolver로 감 -> VeiwResolver는 ModelAndView를  받게 됨.
         *    */
        // Model and view에 start, end 타입을 뺀 interval인터벌 타임을 계산해주면 - 마지막으로 보여줄 페이지에서 활용 가능
        /*  설명.  postHandle의 ModelAndView는 Handler Method가 반환한 ModelAndView에 해당된다.  */
        modelAndView.addObject("interval", endTime - startTime);    // 그 페이지에 재료 더 챙겨 가...?


    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        System.out.println("postHandler 호출함 ... (핸들러 메소드 이후)");;
    }
}

/*  필기. 스프링 부트에 설정해주어야 함.
*       - Configuration에서 등록하기 전에는 - 그냥 POJO(일반 클래스)
*       - 빈(Bean)이 되기 위해서는 ?
*           -> 인터셉터도 Configuration 종류이기 때문에, StopwatchInterceptor 클래스 상단에 @Configuration 설정해주고
*           -> 그렇게 되면 @Component 계열이기 때문에, Bean으로 등록됨.
*           -> 의존성 주입 받을 수 있음. (WebConfiguration 클래스에서 인터셉터가 필드로 사용될 수 있음)
*               - 의존성 주입 방법(생성자,
* */