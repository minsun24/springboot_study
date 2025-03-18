package com.ohgiraffers.restapi.section01.response;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/* 설명. @RestController 어노테이션을 통해
        Controller의 모든 핸들러 메소드에 @ResopnseBody 를 적용한 효과 */
/* 설명. 이제 더이상 view resolver 가 처리하지 않는다. 반환형이 Model&View 형태이지 않아도 됨
 *       (어떤 반환 값이든 반환됨) */
@RestController     // view resolver를 거치지 않고, java에서 보낸 것을 그대로 전달
@RequestMapping("/response")
public class ResponseRestController {

    /*  필기. API 테스트
     *       1. postman
     *       2. http 파일(intellij ultimate 버전)
     *       3. swagger
     * */

    /* 목차. 기본형 타입 내보내기  */
    @GetMapping("/hello")
    public String helloWorld() {
        return "Hello Word!";
    }

    @GetMapping("/random")
    public int getRandomNumber() {
        return (int) (Math.random() * 10) + 1;
    }

    /* 목차. 사용자정의형 타입 내보내기 */
    @GetMapping("/message")
    public Message getMessage() {
        return new Message(200, "메시지를 응답합니다.");
    }

    /*  목차. List<Map> 형태 반환 */
    @GetMapping("/test")
    public List<Map<String, Object>> getTest() {
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("test1", new Message(200, "성공1"));
        map.put("test2", new Message(200, "성공2"));
        map.put("nextPageLink", "http://localhost:8080/hello");     // RESTFULL =>  HATEOAS 조건 충족
        list.add(map);

        return list;
    }

    @GetMapping("/map")
    public Map<Integer, String> getMap() {
        List<Message> messageList = new ArrayList<>();
        messageList.add(new Message(200, "정상응답"));
        messageList.add(new Message(404, "페이지 찾을 수 없음"));
        messageList.add(new Message(500, "서버 에러- 개발자 잘못"));

        /* 설명. List -> Map 으로 바꿔 JSON Obejcect 형태로 응답하기 위한 stream 적용    */
        /* 필기.  Message => httpStatusCode : message >>  Map 형태로 변환해서 전달 */
        return messageList.stream()
                .collect(Collectors.toMap(Message::getHttpStatusCode, Message::getMessage));
    }

    /* 목차. 이미지  응답하기  */
    /* 설명. produce 는 response header에 Content-Type으로 MIME 타입을 설정하는 것이다. */
    @GetMapping(value = "/image/{filename}", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getImage(@PathVariable String filename) throws IOException {

        Path path = Paths.get("C:\\uploadFiles\\img", filename + ".jpg");   // 확장자 .jpg로 고정한 상태
        Resource resource = new UrlResource(path.toUri());



//        byte[] imageBytes = resource.getInputStream().readAllBytes();   // 바이트 배열

//        return imageBytes;
//        return ResponseEntity.ok()
//                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename)
//                .body(imageBytes);
        return Files.readAllBytes(path);
        /*  브라우저에서도 이미지 확인 가능   */
    }
}


