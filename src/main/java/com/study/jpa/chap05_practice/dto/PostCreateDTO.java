package com.study.jpa.chap05_practice.dto;

import com.study.jpa.chap05_practice.entity.Post;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Setter @Getter
@ToString @EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostCreateDTO {

    // @NotNull ->  null 을 허용하지 않음, "", " "은 허용
    // @NotEmpty -> null, ""은 허용하지 않음, " " 은 허용
    @NotBlank // -> null, "", " " 모두 허용하지 않음.
    @Size(min = 2, max = 5)
    private String writer;

    @NotBlank
    @Size(min = 1, max = 20)
    private String title;

    private String content;

    private List<String> hashTags;

    // dto를 엔터티로 변환하는 메서드
    public Post toEntity() { // 메서드명 toEntity() 는 관례
        return Post.builder()
                .writer(this.writer)
                .content(this.content)
                .title(this.title)
//                .hashTags(this.hashTags) //this.hashTags는 List<String> 이지만 Post 객체형임.
                // 애초에 읽기전용인 컬럼에 뭔가 값을 넣는다는게,,, 아예 해당 컬럼은 존재하지도 않는데?

                //따라서 해시태그는 여기서 넣는 것이 아님
                .build();
    }

}
