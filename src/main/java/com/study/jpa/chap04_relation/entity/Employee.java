package com.study.jpa.chap04_relation.entity;

import lombok.*;

import javax.persistence.*;


@Setter @Getter @ToString @EqualsAndHashCode(of = "id")
@NoArgsConstructor @AllArgsConstructor
@Builder
@Entity
@Table(name = "tbl_emp")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "emp_id")
    private Long id;

    @Column(name = "emp_name", nullable = false)
    private String name;


    @ManyToOne // 현재 엔터티입장에서 시작! N(사원들) : 1(부서) 관계이니까 Many to One
    @JoinColumn(name = "dept_id") // FK 의 참조할 컬렴명을 알리는 것.
    // 걍 아예 객체로 받아버리네? JoinColumn 에서 설정한 컬럼을 통해서 온전한 객체 정보를 받아오겠다 ! 실제 테이블에는 객체가 저장될 수 없으니꽈~
    private Department department;




}
