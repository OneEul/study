package com.product.test;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExcelData {
	
	private String productID;
	
	private String productName;
	
	private String keyword;
	
	private String category;
	
	private String brand;
	
	public ProductEntity toEntity() {
		return ProductEntity.builder().productID(productID).productName(productName).keyword(keyword).category(category).brand(brand).build();
	}
}
