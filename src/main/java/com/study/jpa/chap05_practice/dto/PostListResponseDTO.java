package com.study.jpa.chap05_practice.dto;

import lombok.*;

import java.util.List;

@Setter @Getter @ToString @EqualsAndHashCode
@NoArgsConstructor @AllArgsConstructor
@Builder
public class PostListResponseDTO {

    private int count; // 총 게시물 수
    private PageResponseDTO pageInfo; // 페이지 렌더링 정보 (얘도 갖고있는 정보가 많으니 객체로 관리하려는 것)
    private List<PostDetailResponseDTO> posts; // 게시물 렌더링 정보




}
