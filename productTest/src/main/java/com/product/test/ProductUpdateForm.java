package com.product.test;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductUpdateForm {
	
	private String productName;
	
	private String keyword;
	
	private String category;
	
	private String brand;
	
	public ProductEntity toEntity() {
		return ProductEntity.builder().productName(productName).keyword(keyword).category(category).brand(brand).build();
	}
}
