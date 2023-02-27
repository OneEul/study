package com.product.test;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<ProductEntity, String>{
	Page<ProductEntity> findAll(Pageable pageable);
	Page<ProductEntity> findByProductIDContains(Pageable pageable, String searchWord);
	Page<ProductEntity> findByProductNameContains(Pageable pageable, String searchWord);
	Page<ProductEntity> findByKeywordContains(Pageable pageable, String searchWord);
	Page<ProductEntity> findByCategoryContains(Pageable pageable, String searchWord);
	Page<ProductEntity> findByBrandContains(Pageable pageable, String searchWord);
}
