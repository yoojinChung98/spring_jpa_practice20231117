package com.study.jpa.chap03_pagination.repository;

import com.study.jpa.chap02_querymethod.entity.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
//JPA 는 INSERT, UPDATE, DELETE  시에 트랜잭션을 기준으로 동작하는 경우가 많음
// 따라서 기능을 보장받기 위해 트랜직션 기능을 함께 사용하기를 권장.
// 추후 MVC 구조에서는 Service 클래스에 해당 아노테이션을 첨부하면 됨.
@Rollback(false)
class StudentPageRepositoryTest {

    @Autowired
    StudentPageRepository pageRepository;

   /* @BeforeEach
    void bulkInsert() {
        // 학생을 147명 저장
        for(int i=1; i<=147; i++) {
            Student s = Student.builder()
                    .name("김테스트"+i)
                    .city("도시시"+i)
                    .major("전공공"+i)
                    .build();
            pageRepository.save(s);
        }
    }*/


    @Test
    @DisplayName("기본 페이징 테스트")
    void testBasicPagination() {
        //given
        int pageNo = 1;
        int amount = 10;

        // 페이지 정보 생성
        // 반환되는 PageRequest 타입은 타고타고 가보면 Pageable을 구현하므로 다형성 가능.
        // 페이지 번호가 zero-based -> 0이 1페이지
        Pageable pageInfo = PageRequest.of(pageNo-1,
                amount,
//                Sort.by("name").descending() // 컬럼명이 아닌 필드명을 입력해야 한다! => 이름 내림차순 정렬

                // 정렬의 기준을 두개 주고 싶은 경우에는?
                Sort.by(
                        Sort.Order.desc("name"),
                        Sort.Order.asc("city")
                )
        );

        //when
        // pageInfo 매개값을 안주면 그냥 페이징 없이 전체정보 조회가 되는 것.
        // 페이징으로 쓰고 싶으면 그냥 pageqble 객체 하나 주면 되는 것(기본제공이라서)
        // Page<Student> 에는 페이지 정보 + Student들의 정보가 들어있음
        Page<Student> students = pageRepository.findAll(pageInfo);

        // 학생들 정보만 얻어낼 수 있음 (=페이징이 완료된 총 학생 데이터 묶음.)
        List<Student> studentList = students.getContent();

        // 총 페이지 수
        int totalPages = students.getTotalPages();

        // 총 학생 수
        long totalElements = students.getTotalElements();

        // 다음페이지 / 이전페이지가 있나?
        boolean next = students.hasNext();
        boolean prev = students.hasPrevious();


        //then
        System.out.println("\n\n\n");
        System.out.println("totalPages = " + totalPages);
        System.out.println("totalElements = " + totalElements);
        System.out.println("next = " + next);
        System.out.println("prev = " + prev);
        studentList.forEach(System.out::println);

        System.out.println("\n\n\n");
    }

    @Test
    @DisplayName("이름검색 + 페이징")
    void testSearchAndPagination() {
        //given
        int pageNo = 5;
        int size = 10;
        Pageable pageInfo = PageRequest.of(pageNo - 1, size);

        //when
        Page<Student> students = pageRepository.findByNameContaining("3", pageInfo);

        int totalPages = students.getTotalPages();
        long totalElements = students.getTotalElements();
        boolean next = students.hasNext();
        boolean prev = students.hasPrevious();

        /*
        페이징 처리 시에 버튼 알고리즘은 jpa에서 따로 제공하지 않기 때문에
        버튼 배치 알고리즘을 수행할 클래스는 여전히 필요합니다.
        제공되는 정보는 이전보다 많기 때문에, 좀 더 수월하게 처리가 가능합니다.
         */


        //then
        System.out.println("\n\n\n");
        System.out.println("totalPages = " + totalPages);
        System.out.println("totalElements = " + totalElements);
        System.out.println("prev = " + prev);
        System.out.println("next = " + next);
        students.getContent().forEach(System.out::println);
        System.out.println("\n\n\n");
    }


}