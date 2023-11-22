package com.study.jpa.chap05_practice.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.study.jpa.chap05_practice.entity.HashTag;
import com.study.jpa.chap05_practice.entity.Post;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Setter @Getter @ToString @EqualsAndHashCode
@NoArgsConstructor @AllArgsConstructor
@Builder
public class PostDetailResponseDTO {

    private String writer;
    private String title;
    private String content;
    private List<String> hashTags; // 생각해보니까 HashTag 객체에서 이름만 필요하지, id랑 post 필드는 필요없음 : <HashTag> -> <String>으로 변경

    // JSON을 이용할 때만, @JsonFormat 아노테이션을 이용해서 LocalDataTime 의 형식을 더 쉽게 변형할 수 있음
    @JsonFormat(pattern = "yyyy/MM/dd")
    private LocalDateTime regDate;


    // 엔터티를 DTO로 변환하는 생성자
    public PostDetailResponseDTO(Post post) {
        this.writer = post.getWriter();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.regDate = post.getCreateDate();
        this.hashTags = post.getHashTags().stream()
                .map(HashTag::getTagName)
                .collect(Collectors.toList());
    }



}
