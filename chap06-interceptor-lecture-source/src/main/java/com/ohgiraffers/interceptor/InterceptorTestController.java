package com.ohgiraffers.interceptor;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InterceptorTestController {

    /*  설명. (/stopwatch) 요청이 들어오면 1초 대기 후 result.html 을 반환 */
    @GetMapping("stopwatch")
    public String stopwatch() throws InterruptedException {
        System.out.println("핸들러 메소드 호출함....");

        Thread.sleep(1000);

        return "result";

    }

    /*  필기.
    *    stopwatch 페이지에 가면 Interceptor가 동작하여 (인터셉터를 모든 컨트롤러 요청에 대해 등록했기 때문)
    *    - 수행 시간을 측정하고
    *    - result.html에 결과를 표시함
    * */
}
