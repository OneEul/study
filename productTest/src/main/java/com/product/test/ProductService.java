package com.product.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.nhncorp.lucy.security.xss.XssPreventer;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ProductService {
	
	private final ProductRepository productRepository;
	
	public String create(ProductForm productForm) {
		//xss 필터 적용
		productForm.setProductName(XssPreventer.escape(productForm.getProductName()));
		productForm.setKeyword(XssPreventer.escape(productForm.getKeyword()));
		productForm.setCategory(XssPreventer.escape(productForm.getCategory()));
		productForm.setBrand(XssPreventer.escape(productForm.getBrand()));
		//
		return this.productRepository.save(productForm.toEntity()).getProductID();
	}
	
	public void excelCreate(ExcelData exceldata) {
		this.productRepository.save(exceldata.toEntity());
	}
	
	public ProductDTO select(String id) {
		Optional<ProductEntity> pro = this.productRepository.findById(id);
		return new ProductDTO(pro.get());
	}
	
	public void update(String id, ProductUpdateForm productUpdateForm) {
		Optional<ProductEntity> pro = this.productRepository.findById(id);
		ProductEntity pren = pro.get();
		productUpdateForm.setProductName(XssPreventer.escape(productUpdateForm.getProductName()));
		productUpdateForm.setKeyword(XssPreventer.escape(productUpdateForm.getKeyword()));
		productUpdateForm.setCategory(XssPreventer.escape(productUpdateForm.getCategory()));
		productUpdateForm.setBrand(XssPreventer.escape(productUpdateForm.getBrand()));
		pren.update(productUpdateForm);
		this.productRepository.save(pren);
	}
	
	public void delete(String id) {
		Optional<ProductEntity> pro = this.productRepository.findById(id);
		ProductEntity pren = pro.get();
		this.productRepository.delete(pren);
	}
	
	public Page<ProductDTO> getPage(int page){
		List<Sort.Order> sorts = new ArrayList<>();
		sorts.add(Sort.Order.asc("productID"));
		Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
		Page<ProductEntity> oriPage = this.productRepository.findAll(pageable);
		Page<ProductDTO> paging = new ProductDTO().toDTOList(oriPage);
		return paging;
	}
	
	public Page<ProductDTO> getSearchId(String searchWord, int page){
		List<Sort.Order> sorts = new ArrayList<>();
		sorts.add(Sort.Order.asc("productID"));
		Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
		Page<ProductEntity> oriPage = this.productRepository.findByProductIDContains(pageable, searchWord);
		Page<ProductDTO> paging = new ProductDTO().toDTOList(oriPage);
		return paging;
	}
	
	public Page<ProductDTO> getSearchName(String searchWord, int page){
		List<Sort.Order> sorts = new ArrayList<>();
		sorts.add(Sort.Order.asc("productID"));
		Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
		Page<ProductEntity> oriPage = this.productRepository.findByProductNameContains(pageable, searchWord);
		Page<ProductDTO> paging = new ProductDTO().toDTOList(oriPage);
		return paging;
	}
	
	public Page<ProductDTO> getSearchKey(String searchWord, int page){
		List<Sort.Order> sorts = new ArrayList<>();
		sorts.add(Sort.Order.asc("productID"));
		Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
		Page<ProductEntity> oriPage = this.productRepository.findByKeywordContains(pageable, searchWord);
		Page<ProductDTO> paging = new ProductDTO().toDTOList(oriPage);
		return paging;
	}
	
	public Page<ProductDTO> getSearchCate(String searchWord, int page){
		List<Sort.Order> sorts = new ArrayList<>();
		sorts.add(Sort.Order.asc("productID"));
		Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
		Page<ProductEntity> oriPage = this.productRepository.findByCategoryContains(pageable, searchWord);
		Page<ProductDTO> paging = new ProductDTO().toDTOList(oriPage);
		return paging;
	}
	
	public Page<ProductDTO> getSearchBrand(String searchWord, int page){
		List<Sort.Order> sorts = new ArrayList<>();
		sorts.add(Sort.Order.asc("productID"));
		Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
		Page<ProductEntity> oriPage = this.productRepository.findByBrandContains(pageable, searchWord);
		Page<ProductDTO> paging = new ProductDTO().toDTOList(oriPage);
		return paging;
	}
}
