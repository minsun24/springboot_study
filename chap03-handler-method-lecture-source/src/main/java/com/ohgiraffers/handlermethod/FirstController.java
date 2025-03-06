package com.ohgiraffers.handlermethod;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import java.util.Map;

/* 설명. 이 Controller 클래스의 핸들러 메소드에서 Model에 "id"라는 키 값으로 담긴 값은 HttpSession에 추가하는 어노테이션
*   HttpSession에서 제공하는 invalidate() 가 아닌 SessionStatus가 제공하는 setComplete() 을 통해 만료시킬 수 있다.  */
@SessionAttributes("id")    // 필기. Model에 담긴 것 중에, 해당 키값은 HttpSession에 자동으로 담긴다.
@Controller
@RequestMapping("/first")   // 필기. first 로 시작하는 모든 경로를 처리하는 컨트롤러 ('/first/*')
public class FirstController {

    //    /* 설명. 핸들러 메소드에서 반환형이 없을 경우, 요청 경로를 반환한다. (view의 경로 및 이름) */
    @GetMapping("/regist")
    public void regist() {
        // 필기. "/first/regist"를 반환하는 것과 같음.
        //  뷰 파일이 있어야 정상 작동 (View Resolver 가 자동으로 해주는 작업)
    }
//    public String regist(){
//        return "/first/regist"; // 중간 경로
//        // 내부적으로 있는 View Resolver?가 접두사, 접미사(.html)를 붙여서 만들어 줌.
//        // src/main/ .... /first/regiest.html
//    }

    /* 설명. request 는 사용자의 입력값(parameter)를 담는 용도로 쓰고,
            Model은 백엔드에서 동적 페이지를 만들 때 사용하는 용도 */
    @PostMapping("regist")
    public String registMenu(HttpServletRequest request, Model model) {
        String name = request.getParameter("name");
        System.out.println("name: " + name);

        int price = Integer.parseInt(request.getParameter("price"));
        int categoryCode = Integer.parseInt(request.getParameter("categoryCode"));

        String message = name + "을(를) 신규 메뉴 목록의" + categoryCode + "번 카테고리에 " + price + "원으로 등록하였습니다! ";
        model.addAttribute("message", message);

        return "first/messagePrinter";
    }

    @GetMapping("modify")   // 컨트롤러에 맵핑된 /first에 더해져 최종경로는 "/first/modify
    public void modify() {
    }
    /* 설명.
     *   request 의 parameter로 넘어오는 값들의 key 값과 일치하는 변수명을 작성하고 @RequestParam
     *   어노테이션을 적용하면 알아서 값을 꺼내고, 해당 매개변수의 자료형에 맞게 변환까지 해준다. (물론 가능한 경우)
     *
     *  설명.
     *    1. defaultValue : 사용자의 입력 값이 없거나("") 아니면 request의 parameter 키 값과 일치하지 않는
     *                       매개변수 사용 시 매개변수가 가질 기본 default값을 작성한다.
     *    2. name : request parameter의 키 값과 다른 매개변수 명을 사용하고 싶을 때 request parameter의
     *              키 값을 작성한다.
     *
     * */


    /* 수업목표. @RequestParam  */
    @PostMapping("modify1")
    public String modify1(Model model,
                          @RequestParam(name = "name", defaultValue = "디폴트") String modifyName,
                          @RequestParam(name = "modifyPrice", defaultValue = "0") int modifyPrice) {
        /* 설명. 넘어온 parameter의 키값과 핸들러 메소드의 매개변수 이름이 같으면 생략 가능 */
        // 모델을 작성한 이유 : 페이지에게 재료 제공...?

        // 파라미터 가지고 재료를 만들기 ?
        String message = modifyName + "메뉴의 가격을 " + modifyPrice + "원으로 변경하였습니다. ";
        model.addAttribute("message", message);

        /*  필기. 아래 쿼리와 같은 로직
                update tbl_menu
                set menu_price=?where menu_name=?
         */

        return "first/messagePrinter";
    }


    @PostMapping("modify2")
    public String modify2(Model model,
                          @RequestParam Map<String, String> parameterMap) {
        String modifyName = parameterMap.get("name");
        int modifyPrice = Integer.parseInt(parameterMap.get("modifyPrice"));
        String message = modifyName + "메뉴의 가격을" + modifyPrice + "로 변경하였습니다. ";
        model.addAttribute("message", message);

        return "first/messagePrinter";
    }

    @GetMapping("search") // 필기. '/'가 없으면 알아서 붙여줌 - 어노테이션 내장 기능
    public void searchMenu() {
    }
    /* 필기. 커맨드 객체 -> Bean이 아님.
            커맨드 객체는 반드시 setter가 있어야 한다. (setter 필수)
    */
    /* 설명.
     *   핸들러 메소드에 우리가 작성한 클래스(bean X)를 매개변수로 작성하면 스프링이 객체를 만들어주고 (기본생성자)
     *   setter로 값도 주입해 준다. 이러한 클래스의 객체를 "커맨드 객체"라고 부른다.
     *   (커맨드 객체는 기본 생성자와 setter가 필수이다.)
     *   -
     *   설명.
     *    @ModelAttribute 어노테이션을 활용하면 커맨드 객체를 모델에 attribute로 담아주며
     *    이후 view의 재료로 사용할 수 있다.
     *    (키 값 작성 유무에 따라 화면에서 활용하는 방법이 다르다)
     *
     * */


    @PostMapping("search")
    public String searchMenu(@ModelAttribute(name = "menu") MenuDTO menu) {
        System.out.println("menu: " + menu);
        // MenuDTO 기본생성자로 인스턴스가 생성되어 toString에 의해서 각 필드 값이 setter함수로 정의됨...?

        return "first/searchResult";
    }
//    menu: MenuDTO{name='햄버거,9000', price=0, categoryCode=1, orderableStatus='Y'}


    /*  필기.
    *    로그인은 조회(Read)
    * */
    @GetMapping("login")
    public void login(){}   // 로그인 페이지(login.html)로 이동

    @PostMapping("login")
    public String sessionTest1(String id, String pwd, HttpSession session){  //@RequestParam 방식으로 파라미터를 받는 것(생략 가능, input의 name과 같은 이름으로 받으면 된다.)
        System.out.println("id = " + id);
        System.out.println("pwd = " + pwd);
//        id = minsun
//        pwd = qlalfqjsgh
        // 잘 받아와서, 서버에서 출력되는 것을 확인할 수 있다.

        /* 설명. 로그인 성공을 가정(회원 조회 이후) HttpSession에 로그인 성공한 회원 정보 저장하기 */
        /* 필기. HttpSession 복습:  JSESSIONID 를 키로 해서, 서버에 사물함을 하나씩 두는 것 (기본 30분, 만료시간 설정 필요) */
        session.setAttribute("id", id);
        session.setAttribute("pwd", pwd);
        // 실제 로그인 시, 개별 값(id,pwd)이 아닌 로그인 성공한 "객체"를 담아둔다.
//        ex. session.setAttribute("loginUser", newMemberDTO(id, pwd));

        return "first/loginResult";     // 로그인 결과 페이지(loginResult.html)로 이동
    }

    /* 설명. Model에 담은 값 중에 일부를 HttpSession에 자동으로 담도록 어노테이션 활용  */
    @PostMapping("login2")
    public String sessionTest2(Model model, String id) {
        model.addAttribute("id", id);

        /* 필기. 세션에 담는 대신, Model 에 담아서 전달하기
                사용하는 클래스 상단에 @SessionAttributes("key" 값들) 어노테이션 작성해주어야 함.
         */
        return "first/loginResult";
    }


    @GetMapping("logout1")
    public String logoutTest1(HttpSession session){

        // HttpSession 을 삭제한 후 메인 화면으로 나가기
        session.invalidate();

        return "first/loginResult"; // 세션 데이터를 사용하는 html 페이지는 어떻게 될까?
//        null님 환영합니다.
        // 세션이 사라져서, null 로 출력된다. (캐시 때문에, 제대로 삭제되지 않을 수 있음)
    }

    @GetMapping("logout2")
    public String logoutTest2(SessionStatus sessionStatus) {

        sessionStatus.setComplete();    // 세션 만료시키기 .
        return "first/loginResult";
    }

    @GetMapping("body")
    public void body(){}

    @PostMapping("body")
    public void body(@RequestBody String body,
                       @RequestHeader("content-type") String contentType,
                       @CookieValue(value="JSESSIONID") String sessionId){
        System.out.println("body = " + body);
        System.out.println("contentType = " + contentType);
        System.out.println("sessionId = " + sessionId);

//        body = name=%ED%8F%AC%EC%BC%80&price=12222&categoryCode=1&orderableStatus=Y
//        contentType = application/x-www-form-urlencoded
//        sessionId = A5B3758E8927327FE3C22F9E7EABF9C4

    }

}

/*  스프링에서 사용하는 html은 완벽한(?) html이 아님.
    - 자바의...
*   자바에서 사용하는 데이터들을 사용할 수 있음.
* */