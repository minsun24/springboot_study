package com.ohgiraffers.reqeustmapping;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
public class MethodMappingTestController {

    /* 필기. @RequestMapping 어노테이션 이란?
        요청을 맵핑해주는 메서드임을 명시
        "/menu/regist" 경로와 registMenu() 메서드를 맵핑해주고 있음.
     */
    @RequestMapping(value = "/menu/regist", method = RequestMethod.GET)    // GET 요청 뿐 아니라 다른 요청도 받음
    public String registMenu(Model model) { // Model은 SSR에서 쓰는 것으로, 응답할 페이지의 재료가 된다.

        /* 설명. 여기서, 파라미터인 Model은 컨트롤러와 뷰 사이에서 데이터를 전달하는 객체.
        *       백엔드에서 데이터를 담아서 Thymleaf같은 뷰 템플릿에 전달하는 역할을 한다.
        *       -
        *       model.addAttribute("키", "값") 로 데이터를 추가하면
        *       -> 뷰에서 th:text="${키}"로 접근하여 해당 데이터를 사용할 수 있다.
        * */
        System.out.println("/menu/regist 경로의 GET 요청 받아보기");

        model.addAttribute("message", "신규 메뉴 등록용 핸들러 메소드 호출함");

        /* 설명. 핸들러 메소드에서 반환한 String은 응답 값이 아닌 view(html 파일 이름)이 된다. */
        return "mappingResult"; // view(페이지)의 이름, "mappingResult"라는 뷰를 반환하는 것
        // DispatcherServlet : 앞뒤에 prefix,
    }

    @RequestMapping(value = "/menu/modify", method = RequestMethod.POST)
    public String modifyMenu(Model model) {
        model.addAttribute("message", "POST 방식의 메뉴 수정용 핸들러 메소드 호출함...");
        return "mappingResult";
    }

    @GetMapping("/menu/delete")
    public String getDeleteMenu(Model model) {
        model.addAttribute("message", "GET 방식의 메뉴 삭제용 핸들러 메소드 호출함...");

        return "mappingResult";
    }

    @PostMapping("/menu/delete")
    public String postDeleteMenu(Model model) {
        model.addAttribute("message", "POST 방식의 메뉴 삭제용 핸들러 메소드 호출");

        return "mappingResult";
    }
}
