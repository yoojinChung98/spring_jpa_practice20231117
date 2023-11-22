package com.study.jpa.chap05_practice.api;

import com.study.jpa.chap05_practice.dto.PageDTO;
import com.study.jpa.chap05_practice.dto.PostListResponseDTO;
import com.study.jpa.chap05_practice.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        게시물 등록:     /posts            - POST
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





}
