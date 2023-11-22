package com.study.jpa.chap05_practice.api;

import com.study.jpa.chap05_practice.dto.PageDTO;
import com.study.jpa.chap05_practice.dto.PostCreateDTO;
import com.study.jpa.chap05_practice.dto.PostDetailResponseDTO;
import com.study.jpa.chap05_practice.dto.PostListResponseDTO;
import com.study.jpa.chap05_practice.service.PostService;
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

import java.util.List;

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
        게시물 수정:     /posts/{id}       - PATCH
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
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping()
    // ResponseEntity를 ㅣ용하여 다양한 응답상태를 표현할 수 있기 위해 해당 객체를 리턴타입으로 지정함
    public ResponseEntity<?> create(
            @Validated @RequestBody PostCreateDTO dto,
            BindingResult result // 검증 에러 정보를 가진 객체
    ) {
        log.info("api/v1/posts POST!! - payload:{}", dto);

        if(dto == null) { // dto 자체가 아예 전달되지 않은 경우에는 그냥 끝내버리겠음!!
            return ResponseEntity.badRequest().body("등록 게시물 정보를 전달해 주세요!");
        }

        if(result.hasErrors()) { // 입력값 검증 단계에서 문제가 있었다면 true
            List<FieldError> fieldErrors = result.getFieldErrors();// 에러가 하나면 .getFieldError(), 여러개면 getFieldErrors();
            fieldErrors.forEach(err -> {
                log.warn("invalid client data - {}", err.toString());
            });
            return ResponseEntity.badRequest().body(fieldErrors);
        }

        // 위에 존재하는 if문을 모두 지나침 -> dto가 null도 아니고, 입력값 검증도 모두 통과함. -> service에게 명령.
        try {
            // 예외가 발생하지 않으면 그냥 이대로 진행하세요~
            PostDetailResponseDTO responseDTO = postService.insert(dto);
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("미안 서버 터짐 원인: " + e.getMessage()); // 에러코드 500 을 발생시킴

        }

    }






}
