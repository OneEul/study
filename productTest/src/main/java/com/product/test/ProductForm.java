package com.product.test;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductForm {
	
	@NotEmpty(message = "상품명은 필수항목입니다.")
	private String productName;
	
	private String keyword;
	
	@NotEmpty(message = "카테고리는 필수항목입니다.")
	private String category;
	
	@NotEmpty(message = "브랜드는 필수항목입니다.")
	private String brand;
	
	public ProductEntity toEntity() {
		return ProductEntity.builder().productName(productName).keyword(keyword).category(category).brand(brand).build();
	}
}
