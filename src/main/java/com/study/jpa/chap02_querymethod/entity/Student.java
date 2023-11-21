package com.study.jpa.chap02_querymethod.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

// 실무적 측면에서 setter는 신중하게 선택할 것.
@Getter @Setter @ToString @EqualsAndHashCode(of = "id") // 여러개를 주고 싶을 때(of = {"id", "name"}) 이렇게 String 배열로 주면 됨.
// 원래 equals는 존재하는 모든 필드의 값을 비교한 후 해당 객체가 같은 지를 체크해줌. of 로 옵션을 주면 equals 메서드가 작동할 때 두 객체가 같다는 기준을 id로만 잡아줌
@NoArgsConstructor @AllArgsConstructor
@Builder
@Entity
@Table(name = "tbl_student")
public class Student {

    @Id
    @Column(name = "stu_id")
    @GeneratedValue(generator = "uid")
    @GenericGenerator(strategy = "uuid", name = "uid") // uuid로 값을 자동생성하려면 이렇게 두개의 아노테이션을 붙여야 함.
    private String id;

    @Column(name = "stu_name", nullable = false)
    private String name;
    private String city;
    private String major;

}
