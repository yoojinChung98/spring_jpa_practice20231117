package com.study.jpa.chap05_practice.service;

import com.study.jpa.chap05_practice.dto.PageDTO;
import com.study.jpa.chap05_practice.dto.PageResponseDTO;
import com.study.jpa.chap05_practice.dto.PostDetailResponseDTO;
import com.study.jpa.chap05_practice.dto.PostListResponseDTO;
import com.study.jpa.chap05_practice.entity.Post;
import com.study.jpa.chap05_practice.repository.HashTagRepository;
import com.study.jpa.chap05_practice.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional // JPA 레파지토리는 트랜잭션 단위로 동작하므로 반드시 해당 아노테이션이 필요함.
public class PostService {

    private final PostRepository postRepository;
    private final HashTagRepository hashTagRepository;


    public PostListResponseDTO getPosts(PageDTO dto) {

        // Pageable 객체 생성
        Pageable pageable = PageRequest.of(
                dto.getPage() - 1, //pageno는 제로베이스라서 -1 필수!
                dto.getSize(),
                Sort.by("createDate").descending()
        );

        // 데이터베이스에서 게시물 목록 조회
        Page<Post> posts = postRepository.findAll(pageable);


        // 게시물 정보만 꺼내기
        List<Post> postList = posts.getContent();

        // 리스트를 하나 더 선언한 이유 -> postList를 PostDetailResponseDTO 리스트로 바꿔야 하기 때문
        // 게시물 정보를 DTO의 형태에 맞게 변환 (stream을 이용하여 객체마다 일괄 처리)
        List<PostDetailResponseDTO> detailList
                = postList.stream()
                .map(PostDetailResponseDTO::new)
                .collect(Collectors.toList());



        // DB에서 조회한 정보를 JSON 형태에 맞는 DTO로 변환 -> PostListResponseDTO 요 형태가 JSON이 될 것임
        return PostListResponseDTO.builder()
                .count(detailList.size()) // 총 게시물 수가 아니라 조회된 게시물의 개수
                .pageInfo(new PageResponseDTO(posts)) // 페이지 정보가 담긴 객체를 DTO에게 전달해서 그쪽에서 값을 처리하도록 할 것.
                .posts(detailList)
                .build();
    }
}
