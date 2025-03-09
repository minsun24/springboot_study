package com.ohgiraffers.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/*  필기. WebMvcController를 구현하는 WebConfiguration (설정용 Bean) 클래스 */

/*  설명.   WebMvcConfigurer
*          - Interceptor 를 등록(추가)
*          - 정적(static) 리소스의 호출 경로를 등록하는 기능
* */
@Configuration
public class WebConfiguration implements WebMvcConfigurer {
    private StopwatchInterceptor stopwatchInterceptor ; // 필기. Bean으로 등록되어있는 클래스를 의존성 주입 받아

    /*  필기. 특정 요청에 대해 인터셉터가 동작하지 않도록 설정하는 기능을 해줌.
    *           -> 특정 요청은 주로 정적 리소스(resources/static)
    * */
    // 필기. 생성자 주입
    @Autowired
    public WebConfiguration(StopwatchInterceptor stopwatchInterceptor) {
        this.stopwatchInterceptor = stopwatchInterceptor;
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(stopwatchInterceptor)      // 필기. 인터셉터로 등록한다.
                .excludePathPatterns("/css/**");    // 필기. 해당 경로 요청은 인터셉터가 동작하지 않도록 해줌

        /*  필기. 인터셉터 등록 이후 출력 결과    */
//        preHandle 호출함 ... (핸들러 메소드 이전)
//        핸들러 메소드 호출함....
//        postHandle 호출함... (핸들러 메소드 이후)

    }

    /*  설명.  정적 리소스 요청 경로 등록*/
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/css/**")
                .addResourceLocations("classpath:/static/css/") // classpath: 는 resources 폴더를 의미
                .setCachePeriod(100);   // 캐싱

        /*  필기.
                resources/static/css/ 아래 해당하는 요청 ("/css/**") 을 맵핑

        *    */
    }
}


/*  필기.
*       WebMvcConfigurer 인터페이스를 구현한 Configuration 클래스
*       - 인터셉터 등록 *** (등록 전에는 POJO)
*       - 정적 리소스 위치를 스프링 부트에게 알려줄 때
* */