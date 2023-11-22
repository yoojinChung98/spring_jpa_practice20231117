package com.study.jpa.chap05_practice.entity;


import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter @Getter
@ToString(exclude = {"hashTags"})
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tbl_post")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_no")
    private Long id; // 글번호

    @Column(nullable = false)
    private String writer; // 작성자

    @Column(nullable = false)
    private String title; // 제목

    private String content; // 내용

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createDate; // 작성시간

    @UpdateTimestamp
    private LocalDateTime updateDate; // 수정시간



    @OneToMany(mappedBy = "post", orphanRemoval = true) // 고아객체는 삭제되어도 ㄱㅊ하다는 옵션.
    @Builder.Default // 특정 필드를 직접 지정한 값으로 초기화 하는 것을 강제. (빌더 사용 시!)
    private List<HashTag> hashTags = new ArrayList<>();

    // 양방향 매핑에서 리스트쪽에 데이터를 추가하는 편의 메서드 생성
    public void addHashTag(HashTag hashTag) {
        this.hashTags.add(hashTag); //매개값으로 전달받은 HashTag 객체를 리스트에 추가 (주목적)
        if(this != hashTag.getPost()) { //this = addHashTag를 호출한 객체
            hashTag.setPost(this);
            // 매개값으로 전달된 HashTag 객체가 가지고 있는 Post가 이 메서드를 부르는 Post 객체와 주소값이 서로 다르다면
            // 데이터 불일치가 발생하기 때문에 HashTag의 Post의 값도 이 객체로 변경하겠다는 코드 (안전상 코드 추가)
            // 즉 서로 일치하는 객체로 맞춰주겠다는 말!
        }
    }


}
