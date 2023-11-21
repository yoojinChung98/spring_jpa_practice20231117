package com.study.jpa.chap02_querymethod.repository;

import com.study.jpa.chap02_querymethod.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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
    // navtiveQuery = false => JPQL 을 사용했다는 의미.
    @Query(value = "SELECT * FROM tbl_student WHERE stu_name = :nm", nativeQuery = true)
    Student findNameWithSQL(@Param("nm") String name);

    // JPQL
    // SELECT 별칭 FROM 엔터티클래스명 AS 별칭
    // WHERE 별칭.필드명 = ?
    // SELECT st FROM Student AS st    : st-> 딱히 컬럼을 선택하지 않았으므로 모든 컬럼을 의미.
    // WHERE st.name = ?

    // native-sql
    // SELECT 컬럼명 FROM 테이블명
    // WHERE 컬럼 = ?


    // 도시 이름으로 학생 조회
//    @Query(value = "SELECT * FROM tbl_student WEHRE city =  ?1", nativeQuery = true) // ?1 = 첫번째 매개값이라는 의미.
    @Query("SELECT s FROM Student s WHERE s.city = ?1")
    List<Student> getByCityWithJPQL(String city); // 메서드명은 마음대로 지었음



    // Like 절 JPQL 로 작성하기
//    @Query("SELECT s FROM Student s WHERE s.name LIKE %?1%)
    @Query("SELECT s FROM Student s WHERE s.name LIKE %:nm%")
    List<Student> searchByNameWithJPQL(@Param("nm") String name);


    // JPQL로 수정 삭제 쿼리 쓰기
    @Modifying // JPQL을 이용할 떄, 조회가 아닌 경우 무조건 해당 아노테이션을 붙여야 함.
    @Query("DELETE FROM Student s WHERE s.name = ?1")
    void deleteByNameWithJPQL(String name);

}
