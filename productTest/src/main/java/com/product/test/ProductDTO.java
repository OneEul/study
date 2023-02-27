package com.product.test;

import org.springframework.data.domain.Page;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductDTO {
	
	private String productID;
	
	private String productName;
	
	private String keyword;
	
	private String category;
	
	private String brand;
	
	@Builder
	public ProductDTO(String productID, String productName, String keyword, String category, String brand) {
		this.productID = productID;
		this.productName = productName;
		this.keyword = keyword;
		this.category = category;
		this.brand = brand;
	}
	
	public ProductDTO(ProductEntity productEntity) {
		this.productID = productEntity.getProductID();
		this.productName = productEntity.getProductName();
		this.keyword = productEntity.getKeyword();
		this.category = productEntity.getCategory();
		this.brand = productEntity.getBrand();
	}
	
	public Page<ProductDTO> toDTOList(Page<ProductEntity> pageable){
		Page<ProductDTO> productDTOList = pageable.map(m -> ProductDTO.builder().productID(m.getProductID()).productName(m.getProductName()).keyword(m.getKeyword()).category(m.getCategory()).brand(m.getBrand()).build());
		return productDTOList;
	}
}
