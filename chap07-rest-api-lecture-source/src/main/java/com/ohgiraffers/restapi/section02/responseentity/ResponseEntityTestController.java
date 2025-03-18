package com.ohgiraffers.restapi.section02.responseentity;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/entity")
public class ResponseEntityTestController {

    /* 설명.
     *   RequestEntity 란?
     *   HttpRequest 를 응답하기 위해 httpEntity 를 상속받아
     *   HttpStatus, HttpHeaders, HttpBody 를 정의하여 사용하는 클래스이다.
     * */

    private List<UserDTO> users;

    public ResponseEntityTestController() {
        this.users = new ArrayList<>();
        users.add(new UserDTO(1, "user01", "pass01", "홍길동", new java.util.Date()));
        users.add(new UserDTO(2, "user02", "pass02", "홍길동2", new java.util.Date()));
        users.add(new UserDTO(3, "user03", "pass03", "홍길동3", new java.util.Date()));
    }

    /* 설명. 1. ResponseEntity 생성자를 이용한 응답    */
    @GetMapping("/users")
    public ResponseEntity<ResponseMessage> findAllUsers() {

        // 필기. ResoponseEntity
        //      : HttpStatus, HttpHeaders, HttpBody 를 정의하는 객체

        HttpHeaders headers = new HttpHeaders();    // 헤더
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("users", users);

        ResponseMessage responseMessage = new ResponseMessage(200, "조회 성공!", responseMap);

        return new ResponseEntity<>(responseMessage, headers, HttpStatus.OK);
    }

    /* 설명. 2. 빌더를 활용한 메소드 체이닝 방식으로 ResponseEntity 객체 만들기 (요즘 유행!)*/
    @GetMapping("/users/{userNo}")
    public ResponseEntity<ResponseMessage> findUserByNo(@PathVariable("userNo") int userNo) {
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        // list로 변환
        UserDTO foundUser = users.stream()
                .filter(user -> user.getNo() == userNo)
                .collect(Collectors.toList()).get(0);

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("foundUser", foundUser);

        // 필기. ResponseEntity.ok()
        // setter처럼 동작, setter 대신 메소드 체이닝 방식을 사용한 빌더 패턴
        return ResponseEntity.ok().headers(headers).body(new ResponseMessage(200, "조회 성공!", responseMap));
    }

    /* 설명. 회원 추가    */
    // 필기. <?> : 내보내는 데이터가 없을 때 <?> 로 작성
    @PostMapping("/users")
    public ResponseEntity<?> registUser(@RequestBody UserDTO newUser){
        System.out.println("newUser: " + newUser);

        // 회원번호는 자동으로 매겨주기
        int lastUserNo = users.get(users.size() - 1).getNo();   // 컬렉션에 담긴 마지막 회원의 번호
        newUser.setNo(lastUserNo + 1);
        users.add(newUser);                                     // 컬렉션에 회원 추가

        return ResponseEntity
                .created(URI.create("/entity/users/" + (lastUserNo + 1)))
                .build();
    }

    /* 설명. 회원 수정    */
    @PutMapping("/users/{userNo}")
    public ResponseEntity<?> modifyUser(@RequestBody UserDTO modifyInfo, @PathVariable int userNo) {
        System.out.println("modifyInfo = " + modifyInfo);
        System.out.println("userNo = " + userNo);

        UserDTO foundUser = users.stream().filter(user -> user.getNo() == userNo)
                .collect(Collectors.toList()).get(0);
        foundUser.setId(modifyInfo.getId());
        foundUser.setPwd(modifyInfo.getPwd());
        foundUser.setName(modifyInfo.getName());

        return ResponseEntity.created(URI.create("/entity/users/" + userNo)).build();
    }

    /* 설명. 회원 삭제    */
    @DeleteMapping("/users/{userNo}")
    public ResponseEntity<?> removeUser(@PathVariable int userNo) {

        UserDTO foundUser = users.stream().filter(user -> user.getNo() == userNo)
                .collect(Collectors.toList()).get(0);

        users.remove(foundUser);

        return ResponseEntity.noContent().build();
    }
}
