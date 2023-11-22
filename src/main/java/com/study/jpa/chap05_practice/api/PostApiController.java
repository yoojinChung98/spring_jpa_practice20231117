package com.study.jpa.chap05_practice.api;

import com.study.jpa.chap05_practice.dto.*;
import com.study.jpa.chap05_practice.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

@Tag(name = "post API", description = "게시물 조회, 등록 및 수정, 삭제 api 입니다.") // 스웨거에서 쓰려고 작성한 아노테이션인가?
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class PostApiController {

    private final PostService postService;
    // 리소스: 게시물 (Post)
    /*
        게시물 목록 조회: /posts            - GET , param: (page, size)
        게시물 개별 조회: /posts/{id}       - GET
        게시물 등록:     /posts            - POST, payload: (writer, title, content, hashTags) 전송되는 순수한 JSON 데이터를 payload라고 칭하겠음!!
        게시물 수정:     /posts/{id}       - PUT(아예 새로운 걸로 갈아 끼는 느낌) 이나 PATCH(내부의 소소한 데이터를 수정하는 느낌), payload: (title, content, postNo)
        게시물 삭제:     /posts/{id}       - DELETE
     */


    @GetMapping
    public ResponseEntity<?> list(PageDTO pageDTO) {
    log.info("/api/v1/posts?page={}&size={}", pageDTO.getPage(), pageDTO.getSize());

    PostListResponseDTO dto = postService.getPosts(pageDTO);

    // ResponseEntity.ok() : 200이라는 상태코드를 전달하기 위해 사용
        // body 에는 dto를 담아서 보냄.
    return ResponseEntity.ok().body(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> detail(@PathVariable Long id) {
        log.info("/api/v1/posts/{} GET", id);

        try {
            PostDetailResponseDTO dto = postService.getDetail(id);
            // 정상적으로 서비스로직이 종료했을 때,
            return ResponseEntity.ok().body(dto);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "게시물 작성", description = "게시물 작성을 담당하는 메서드 입니다.") // 스웨거에서 쓰려는 아노테이션 (서머리: 요약, 디스크립션: 상세설명)_
    @Parameters({ // 각각의 파라미터를 설명하는거라고 생각하면 됨
            @Parameter(name = "writer", description = "게시물의 작성자 이름을 쓰세요!", example = "김뽀삐", required = true), // 파라미터 이름, 설명, 예시, 필수여부
            @Parameter(name = "title", description = "게시물의 제목을 쓰세요!", example = "글의 제목입니다", required = true), // 파라미터 이름, 설명, 예시, 필수여부
            @Parameter(name = "content", description = "게시물의 내용을 쓰세요!", example = "글의 내용입니당"), // 파라미터 이름, 설명, 예시, 필수여부
            @Parameter(name = "hashTags", description = "게시물의 해시태그를 쓰세요!", example = "['하하', '호호']") // 파라미터 이름, 설명, 예시, 필수여부
    }) //스웨거 파라미터가 하나면 @parameter()
    @PostMapping
    // ResponseEntity를 ㅣ용하여 다양한 응답상태를 표현할 수 있기 위해 해당 객체를 리턴타입으로 지정함
    public ResponseEntity<?> create(
            @Validated @RequestBody PostCreateDTO dto,
            BindingResult result // 검증 에러 정보를 가진 객체
    ) {
        log.info("api/v1/posts POST!! - payload:{}", dto);

        if(dto == null) { // dto 자체가 아예 전달되지 않은 경우에는 그냥 끝내버리겠음!!
            return ResponseEntity.badRequest().body("등록 게시물 정보를 전달해 주세요!");
        }

        ResponseEntity<List<FieldError>> fieldErrors = getValidatedResult(result);
        if (fieldErrors != null) return fieldErrors;

        // 위에 존재하는 if문을 모두 지나침 -> dto가 null도 아니고, 입력값 검증도 모두 통과함. -> service에게 명령.
        try {
            // 예외가 발생하지 않으면 그냥 이대로 진행하세요~
            PostDetailResponseDTO responseDTO = postService.insert(dto);
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("미안 서버 터짐 원인: " + e.getMessage()); // 에러코드 500 을 발생시킴

        }

    }




    // 게시물 수정
    @RequestMapping(method = {RequestMethod.PATCH, RequestMethod.PUT}) // 이렇게 두가지 요청방식을 하나의 메서드에 매핑할 수도 있음
    public ResponseEntity<?> update(
            @Validated @RequestBody PostModifyDTO dto,
            BindingResult result,
            HttpServletRequest request
    ) { // BindingResult는 @Validated의 결과값을 확인하기 위한 객체

        // 요청 방식에 따라 하나의 메서드에서 다른 방식을 처리하려고 할 때 다음과 같이 작성
        log.info("/api/v1/posts {} - payload: {}"
            , request.getMethod(), dto);

        ResponseEntity<List<FieldError>> fieldErrors = getValidatedResult(result);
        if(fieldErrors != null) return fieldErrors;

        PostDetailResponseDTO responseDTO = postService.modify(dto);

        return ResponseEntity.ok().body(responseDTO);
    }








    // 입력값 검증(Validation)의 결과를 처리해주는 전역 메서드
    private static ResponseEntity<List<FieldError>> getValidatedResult(BindingResult result) {
        if(result.hasErrors()) { // 입력값 검증 단계에서 문제가 있었다면 true
            List<FieldError> fieldErrors = result.getFieldErrors();// 에러가 하나면 .getFieldError(), 여러개면 getFieldErrors();
            fieldErrors.forEach(err -> {
                log.warn("invalid client data - {}", err.toString());
            });
            return ResponseEntity.badRequest().body(fieldErrors);
        }
        return null;
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        log.info("api/v1/posts/{} DELETE!!", id);

        try {
            postService.delete(id);
            return ResponseEntity.ok("DEL SUCCESS!!"); // 어차피 줄것도 없는데 걍 이렇게 던져버리겠으
        }
//        // 그냥 애초에 해시태그가 있는 게시글은 삭제를 못하게 하는 방법
//        catch (SQLIntegrityConstraintViolationException e) {
//            return ResponseEntity.internalServerError()
//                    .body("해시태그가 달린 게시물은 삭제할 수 없습니다.");
//        }
        catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }


}
