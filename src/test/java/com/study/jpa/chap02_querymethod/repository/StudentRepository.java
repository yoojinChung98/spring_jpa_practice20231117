package com.study.jpa.chap02_querymethod.repository;

import com.study.jpa.chap02_querymethod.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, String> {

    // 컬럼명이 아닌 필드명으로 설정해야한다!(findBystuName 이라고 적으면 비작동)
    List<Student> findByName(String name);

    List<Student> findByCityAndMajor(String city, String major);

    List<Student> findByMajorContaining(String major);

    // 네이티브 쿼리 사용 (우리가 평소에 사용하는 sql을 사용할 수는 없을까?)
    // 이 경우 메서드이름은 마음대로 작성 가능!
    @Query(value = "SELECT * FROM tbl_student WHERE stu_name = :nm", nativeQuery = true)
    Student findNameWithSQL(@Param("nm") String name);
}
