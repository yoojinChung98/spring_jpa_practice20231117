package com.study.jpa.chap01_basic.repository;

import com.study.jpa.chap01_basic.entity.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.study.jpa.chap01_basic.entity.Product.Category.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional // 테스트 완료 후 롤백. (테스트 완료 후 원상복구 해놓아라! 의 의미)
@Rollback(false) // 기본값은 true, false: 롤백을 실행하지 않겠다는 의미.
class ProductRepositoryTest {

    @Autowired
    ProductRepository productRepository;

    @BeforeEach // 테스트 메서드 전에 실행되는 메서드
        // 특정 테스트 메서드를 돌릴 때, BeforeEach 가 먼저 진행되므로, 테이블 삭제후 재생성이 되더라도 항상 더미데이터를 생성해주고 시작할 것
    void insertDummyData() {
        //given
        Product p1 = Product.builder()
                .name("아이폰")
                .category(ELECTRONIC)
                .price(1000000)
                .build();
        Product p2 = Product.builder()
                .name("탕수육")
                .category(FOOD)
                .price(20000)
                .build();
        Product p3 = Product.builder()
                .name("구두")
                .category(FASHION)
                .price(300000)
                .build();
        Product p4 = Product.builder()
                .name("쓰레기")
                .category(FOOD)
                .build();

        //when
        // JpaRepositroy가 기본적인 메서드들을 제공해주기 때문에 (import 한 것 중에 import org.springframework.data.jpa.repository.JpaRepository;)
        // 그래서 우리가 해당 인터페이스에 직접 입력한 추상메서드가 없어도 기본적인 메서드들은 사용할 수 있음.
        productRepository.save(p1);
        productRepository.save(p2);
        productRepository.save(p3);
        productRepository.save(p4);

        //then
    }


//    @Test
//    @DisplayName("상품 4개를 데이터베이스에 저장해야 한다")
//    void testSave() {
//        //given
//        Product p1 = Product.builder()
//                .name("아이폰")
//                .category(ELECTRONIC)
//                .price(1000000)
//                .build();
//        Product p2 = Product.builder()
//                .name("탕수육")
//                .category(FOOD)
//                .price(20000)
//                .build();
//        Product p3 = Product.builder()
//                .name("구두")
//                .category(FASHION)
//                .price(300000)
//                .build();
//        Product p4 = Product.builder()
//                .name("쓰레기")
//                .category(FOOD)
//                .build();
//
//        //when
//        // JpaRepositroy가 기본적인 메서드들을 제공해주기 때문에 (import 한 것 중에 import org.springframework.data.jpa.repository.JpaRepository;)
//        // 그래서 우리가 해당 인터페이스에 직접 입력한 추상메서드가 없어도 기본적인 메서드들은 사용할 수 있음.
//        productRepository.save(p1);
//        productRepository.save(p2);
//        productRepository.save(p3);
//        productRepository.save(p3);
//
//        //then
//    }

    @Test
    @DisplayName("5번째 상품을 데이터베이스에 저장해야 한다.")
    void testSave() {
        //given
        Product p5 = Product.builder()
                .name("정장")
                .category(FASHION)
                .price(100000)
                .build();
        //when
        Product saved = productRepository.save(p5);
        // save() 의 반환은 void 가 아닌 방금 인서트를 완료한 Product 객체이다.
        // 만약 null 이 온다면 인서트에 문제가 발생해서 중간에 멈춘 것.

        //then
        assertNotNull(saved);
    }

    @Test
    @DisplayName("id가 2번인 데이터를 삭제해야 한다.")
    void testRemove() {
        //given
        long id = 2L;

        //when
        productRepository.deleteById(id);

        //then
    }

    @Test
    @DisplayName("상품 전체조회를 하면 상품의 개수가 4개여야 한다.")
    void testFindAll() {
        //given

        //when
        List<Product> products = productRepository.findAll();

        //then
        products.forEach(System.out::println);

        assertEquals(4, products.size());
    }

    @Test
    @DisplayName("3번 상품을 조회하면 상품명이 구두일 것이다.")
    void testFindOne() {
        //given
        Long id = 3L;
        //when
        Optional<Product> product = productRepository.findById(id);

        //then
        product.ifPresent(p -> {
            assertEquals("구두", p.getName());
        });

        Product foundProduct = product.get();
        assertNotNull(foundProduct);

        System.out.println("foundProduct = " + foundProduct);
    }

    @Test
    @DisplayName("2번 상품의 이름과 가격을 변경해야 한다.")
    void testModify() {
        //given
        long id = 2L;
        String newName = "짜장면";
        int newPrice = 6000;
        //when
        //jpa에서는 update는 따로 메서드로 제공하지 않고,
        // 조회한 후 setter 메서드로 변경하고. save를 호출해야 update가 나감.
        Optional<Product> product = productRepository.findById(id);
        product.ifPresent(p -> {
            p.setName(newName);
            p.setPrice(newPrice);

            productRepository.save(p);
        });

        //then
        assertTrue(product.isPresent());

        Product p = product.get();
        assertEquals("짜장면", p.getName());
    }



}