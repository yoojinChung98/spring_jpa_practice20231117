package com.study.jpa.chap05_practice.entity;

import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@ToString(exclude = {"post"})
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tbl_hash_tag")
public class HashTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_no")
    private Long id;

    private String tagName; // 해시태그 이름

    // 양방향이라서 반대편에도 대응되는 옵션을 다 줘야 함.
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL) // CascadeType의 모든 것을 감지하게 하려면 All을 주면 됨.
    @JoinColumn(name = "post_no")
    private Post post;



}
