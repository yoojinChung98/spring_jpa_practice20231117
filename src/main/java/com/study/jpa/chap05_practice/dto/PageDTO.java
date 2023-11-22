package com.study.jpa.chap05_practice.dto;


import lombok.*;

@Getter @Setter
@ToString  @EqualsAndHashCode
@AllArgsConstructor
@Builder
public class PageDTO {

    private int page;
    private int size;

    public PageDTO() { // 기본생성자에 기본 필드값 초기화해놓음.
        this.page = 1;
        this.size = 10;
    }
}
