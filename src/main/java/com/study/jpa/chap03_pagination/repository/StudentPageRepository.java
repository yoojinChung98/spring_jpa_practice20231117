package com.study.jpa.chap03_pagination.repository;

import com.study.jpa.chap02_querymethod.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentPageRepository extends JpaRepository<Student, String> {

    // 학생 조건 없이 전체 조회 페이징 (기본기능 : 즉, 따로 만들지 않아도 된다)
    Page<Student> findAll(Pageable pageable);

    // 학생의 이름에 특정 단어가 포함된 걸 조회 + 페이징
    // 마지막에 pageable 매개값만 하나 받아내면 페이징 쿼리를 사용할 수 있음(oo 맞음)
    // 여기 JpaRepository 인터페이스를 상속하는 레파지토리 인터페이스의 모든 메서드는 모두 Pageable 타입의 객체를 매개값으로 받으면 Page객체를 반환받을 수 있음
    Page<Student> findByNameContaining(String name, Pageable pageable);







}
