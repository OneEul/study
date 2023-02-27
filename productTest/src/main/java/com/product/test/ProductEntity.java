package com.product.test;

import java.text.SimpleDateFormat;
import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class ProductEntity {
	
	@Id
	private String productID;
	
	private String productName;
	
	private String keyword;
	
	private String category;
	
	private String brand;
	
	@PrePersist
	public void generateId() {
		SimpleDateFormat date = new SimpleDateFormat("yyyyMMddHHmmss");
		this.productID = "pr_"+date.format(new Date());
	}
	
	@Builder
	public ProductEntity(String productID, String productName, String keyword, String category, String brand) {
		this.productID = productID;
		this.productName = productName;
		this.keyword = keyword;
		this.category = category;
		this.brand = brand;
	}
	
	public void update(ProductUpdateForm productUpdateForm) {
		if(!productUpdateForm.getProductName().equals("")) {
			this.productName = productUpdateForm.getProductName();
		}
		if(!productUpdateForm.getKeyword().equals("")) {
			this.keyword = productUpdateForm.getKeyword();
		}
		if(!productUpdateForm.getCategory().equals("")) {
			this.category = productUpdateForm.getCategory();
		}
		if(!productUpdateForm.getBrand().equals("")) {
			this.brand = productUpdateForm.getBrand();
		}
	}
}
