package com.study.jpa.chap05_practice.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Setter @Getter
@ToString @EqualsAndHashCode
@NoArgsConstructor // 객체타입 필드는 값이 안오는 경우 null이 올 수 있음 -> 기본값 세팅을 해줘야 함.
@AllArgsConstructor
@Builder
public class PostModifyDTO {

    @NotBlank
    @Size(min = 1, max = 20)
    private String title;

    private String content;

    @NotNull // Long 타입은 빈 문자열/ 공백이라는 개념이 없어서 NotBlank를 사용하면 에러가 발생! (얘는 문자열이랑 궁합이 좋음)
    // 선생님 정리: 공백이나 빈 문자열이 들어올 수 없는 타입은 NotNull로 검증.
    private Long postNo = 0L;

}
