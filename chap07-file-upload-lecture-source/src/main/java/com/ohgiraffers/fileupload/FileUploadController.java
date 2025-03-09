package com.ohgiraffers.fileupload;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Controller
public class FileUploadController {

    @Value("${filePath}")
    private String filePath;

    /* 수업목표. Single File Upload
               파일 한 개를 업로드하는 방법  */

    /*  설명.  multipart/form-data 로 넘어오는 값은 MultipartFile로 받아내야 한다.  */
    @PostMapping("single-file")
    public String singleFile(@RequestParam MultipartFile singleFile,
                             @RequestParam String singleFileDescription,
                             RedirectAttributes rttr) {

//        System.out.println("singleFile = " + singleFile);
//        System.out.println("singleFileDescription = " + singleFileDescription);     // 파일 설명
//        output ) 필기. 객체로 들어오는 것을 확인할 수 있다.
//        singleFile = org.springframework.web.multipart.support.StandardMultipartHttpServletRequest$StandardMultipartFile@1df71d61
//        singleFileDescription = 소녀 사진
//        singleFile = org.springframework.web.multipart.support.StandardMultipartHttpServletRequest$StandardMultipartFile@5bd23775
//        singleFileDescription = 소년 사진
//        singleFile = org.springframework.web.multipart.support.StandardMultipartHttpServletRequest$StandardMultipartFile@47402cf6
//        singleFileDescription = 최우식 사진

        /*  설명.  1. 저장될 파일의 경로 설정 후 파일 저장   */
        /*  설명.  2. 파일의 이름 리네임  */
        /*  설명.  3. DB로 보낼 데이터 만들기 (Map<String(컬럼), String(데이터)>, List<Map<String, String>>) */
        /*
         *   originFileName (업로드된 파일원본 이름)
         *   renameFileName (리네임한 파일)
         *   -> DB에 올릴 때는 리네임한 파일로 올리고, 사용자가 다운받을 때는 원래 올린 원본 이름으로 다운받게 해준다.
         *   filePath    (파일 경로)
         *   fileDescription (파일 디스크립션)
         *   thumbnail 유무
         * */

        /*  설명.  사용자가 넘긴 파일의 원본 이름을 확인하고 rename 해보자.
         *       (자바의 UUID 클래스를 이용한 무작위 문자열 형태로 생성)
         * */
        String originFileName = singleFile.getOriginalFilename();   // 사용자가 올린 파일 원본 이름
        System.out.println("originFileName = " + originFileName);

        String ext = originFileName.substring(originFileName.lastIndexOf("."));     // 확장자 분리하기
        System.out.println("ext = " + ext);

        String savedName = UUID.randomUUID().toString().replace("-", "") + ext;   // 랜덤한 난수 생성 후 String으로 변환
        System.out.println("savedName = " + savedName);
//        originFileName = choi.png
//        ext = .png
//        savedName = a1f8ac4cbac14eebb4b60c4e0733910f.png  // 실제 DB에 저장되는 파일

        /*  설명. 우리가 지정한 경로로 파일을 저장 */
        try {
            // 설명. 1. (로컬 폴더) 특정 경로에 리네임된 파일 이름으로 저장
            singleFile.transferTo(new File(filePath + "/img/single/" + savedName));

            /*  설명.  DB로 보낼 데이터 Map으로 가공 처리 */
            Map<String, String> file = new HashMap<>();
            file.put("originalFile", originFileName);
            file.put("savedName", savedName);
            file.put("filePath", filePath);
            file.put("singleFileDescription", singleFileDescription);


            /*  설명. 2. 이후 service 계층을 통해 DB에 업로드된 파일의 내용을 저장하고 옴 (지금 작성할 내용은 X)*/
            // Service의 메서드로 전달 -> result 1일 경우 <성공>
//            int result = fileUploadService.registFile(file); // insert into tbl_file values(?,?,?,?)
            /*  필기. 1(로컬 저장), 2(DB 저장) 작업이 모두 성공적으로 마무리되면 끝!
                    Spring + Mybatis 를 배운 후 구현 가능
             */

            // 데이터 뷰로 전달
            rttr.addFlashAttribute("message", originFileName + "파일 업로드 성공!");
            // result 페이지에서 데이터 사용 가능
            rttr.addFlashAttribute("img", "/img/single/" + savedName);
            // 필기. 경로 - root (/) 의 경로를 yml 파일에 설정해두었기 때문에 가능.
            rttr.addFlashAttribute("singleFileDescription", singleFileDescription);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return "redirect:/result";      // result 페이지로 이동 요청

        /*  필기.
                >>  "result"를 리턴하는 경우
                    파일이 업로드되면 result 페이지로 forwarding 됨.
                    (포워딩하면 브라우저가 가리키는 URL은 "single-file"이므로 새고로침 시 single-file 서블릿으로 요청이 들어감)
        *           그런데, 파일 업로드는 INSERT 작업
        *           포워딩하면, 새로고침할 때마다 파일이 계속 업로드될 것 (???)
                -
        *       >> 따라서, "redirect:/result" 로 변경
                    redirect 는 페이지의 이름이 아니기 때문에, "/result" 요청을 하게 하고,
                    /result 요청에 대한 메서드를 생성해주어야 함.
        * */

    }


    /* 수업목표. Multi File Upload
               파일 여러 개를 한번에 업로드하는 방법  */
    @GetMapping("result")
    public void result() {
    }
    // 파일 업로드 후 브라우저가 가리키는 URL 은 "/result" => 따라서 새로고침을 해도 INSERT 가 일어나지 않음

    @PostMapping(value = "multi-file")
    public String multiFileUpload(@RequestParam List<MultipartFile> multiFiles,
                                  @RequestParam String multiFileDescription,
                                  RedirectAttributes rttr) {

        /*  설명.  DB에 보낼 값을 담기 위한 컬렉션    */
        List<Map<String, String>> files = new ArrayList<>();

        /*  설명. 화면에서 각 파일마다 img 태그의 src 속성으로 적용하기 위한 문자열을 담은 컬렉션 */
        List<String> imgSrcs = new ArrayList<>();



        try {
            /*  필기. 어디서든 에러가 발생하면 catch 로 잡기    -> try block 범위 잡는 것도 고민 필요 */
            for (int i = 0; i < multiFiles.size(); i++) {

                /*  설명.  각 파일마다 리네임 */
                String originFileName = multiFiles.get(i).getOriginalFilename();
                String ext = originFileName.substring(originFileName.lastIndexOf(".")); // 확장자 제거
                String savedName = UUID.randomUUID().toString().replace("-", "") + ext;

                /*  설명.  로컬 경로에 파일 저장   */
                multiFiles.get(i).transferTo(new File(filePath + "/img/multi/" + savedName));

                /*  설명. DB 에 보낼 값 설정 (하나의 파일은 Map<String, String> 에 담는다.)
                *        -> 레코드 하나 만드는 것 */
                Map<String, String> file = new HashMap<>();
                file.put("originalFile", originFileName);
                file.put("savedName", savedName);
                file.put("filePath", "/img/multi");
                file.put("multiFileDescription", multiFileDescription);

                files.add(file);    // Map을 List에 추가
                imgSrcs.add("/img/multi/" + savedName); // 화면에 전달할 img src 속성
            }   // for end

            /*  설명. singleFile 업로드 때와 마찬가지로 DB를 다녀옴 (지금 작성할 내용은 x)  */
//            fileUploadService.registFile(files);

            /*  설명.  여기까지 성공했다면, 파일 저장 및 DB INSERT까지 모두 완성되었으니 화면의 재료 작성    */
            rttr.addFlashAttribute("message", "다중 파일 업로드 성공 ! ");
            rttr.addFlashAttribute("imgs", imgSrcs);   // src 경로가 여러 개 전달되어야 하므로,

        }   catch(IOException e) { // 로컬에 저장하는 과정에서 문제 발생 시 처리되는 예외
            // 로컬 경로에 저장하는 과정에서 문제가 발생하면 -> DB 저장 로직은 실행되지 않음
            // files(DB에 담을 레코드 리스트)에는 로컬에 먼저 저장이 완료된 데이터들만 들어있음
            // 로컬에 데이터를 저장하던 과정에 문제가 생겨서, 로컬에 저장되지 못했을 경우,
            // 레코드 리스트에 담겨 있던 나머지 앞선 데이터들을 지워주어야 함!


            /*  필기. try문 실행 중 예외가 발생했을 경우 실행하는 부분   */
            /*  설명.  transferTo로 파일을 경로에 저장하다가 예외가 발생하면 앞에 이미 성공해서 저장된 파일들을 찾아서 지움  */
            // files(레코드 리스트) 를 순회하며
            for(int i=0; i<files.size(); i++) {
                Map<String, String> file = files.get(i);
                new File(filePath + "/img/multi" + file.get("saveName")).delete();
            }

            rttr.addFlashAttribute("messgae", "파일 업로드 실패 ㅜㅜ");

        }

        return "redirect:/result";
    }
}
