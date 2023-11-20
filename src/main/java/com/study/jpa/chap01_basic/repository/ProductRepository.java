package com.study.jpa.chap01_basic.repository;

import com.study.jpa.chap01_basic.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    // 제너릭의 첫번째 매개값: 엔터티, 두번째 매개값: 해당 엔터티의 기본키의 데이터타입


}
