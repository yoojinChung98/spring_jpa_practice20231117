package com.study.jpa.chap01_basic.entity;

import jdk.jfr.Category;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter @Setter @ToString @EqualsAndHashCode
@NoArgsConstructor @AllArgsConstructor
@Builder
@Entity
@Table(name = "tbl_product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY ) // id라는 컬럼을 발동 시킬 때의 전략 설정 (mysql엔 시퀀스 없음)
    @Column(name = "prod_id") // 필드명과 컬럼명을 다르게 하고 싶을 경우
    private long id;
    @Column(name = "prod_name", nullable = false, length = 30)
    private String name;

    private int price = 0; //디폴트 값은 그냥 필드에 값을 먹이면 됨.

    @Enumerated(EnumType.STRING)
    private Category category;

    @CreationTimestamp // = Default sysdate / current_timestamp
    @Column(updatable = false) // 수정불가 옵션.
    private LocalDateTime createDate;

    @UpdateTimestamp // 업데이트 될 때 날짜 값이 자동으로 들어감.
    private LocalDateTime updateDate;

    public enum Category {
        FOOD, FASHION, ELECTRONIC
    }

}
