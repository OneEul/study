package com.product.test;

import java.io.IOException;
import java.security.Principal;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class ProductController {
	
	private final UserService userService;
	private final ProductService productService;
	
	@GetMapping("/")
	public String goMain() {
		return "redirect:/main";
	}
	
	@GetMapping("/main")
	public String main(Model model,Principal principal){
		if(principal != null) {
			model.addAttribute("userId", principal.getName());
		}
		return "main";
	}
	
	@GetMapping("/login")
	public String signUp() {
		return "login";
	}
	
	@GetMapping("/signUp")
	public String signUp(Model model, UserForm userform) {
		model.addAttribute("userform", userform);
		return "signUp";
	}
	
	@PostMapping("/signUp")
	public String signUpPost(Model model, @Valid UserForm userform, BindingResult bindingResult, Principal principal) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("userform", userform);
            return "signUp";
        }
		if(!userform.getPassword().equals(userform.getPassword2())) {
			bindingResult.rejectValue("password2" ,"signupFailed", "비밀번호와 비밀번호 확인이 동일하지 않습니다.");
			model.addAttribute("userform", userform);
			return "signUp";
		}
		model.addAttribute("userform", userform);
		try {
			this.userService.userCreate(userform);
		} catch (DataIntegrityViolationException e) {
			bindingResult.rejectValue("userId" ,"signupFailed", "이미 등록된 사용자입니다.");
			return "signUp";
		}
		return "redirect:/successSignUp";
	}
	
	@GetMapping(value="/successSignUp", produces="text/html;charset=UTF-8")
	@ResponseBody
	public String successSignUp() {
		return "<script>alert('가입에 성공했습니다!');location.href='/login'</script>";
	}
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("excelRead")
	public String excel(@RequestParam("file") MultipartFile file, Model model) throws IOException {

	    String extension = FilenameUtils.getExtension(file.getOriginalFilename()); // 3

	    if (!extension.equals("xlsx") && !extension.equals("xls")) {
	      throw new IOException("엑셀파일만 업로드 해주세요.");
	    }

	    Workbook workbook = null;
	    
	    if (extension.equals("xlsx")) {
	    	workbook = new XSSFWorkbook(file.getInputStream());
		} else if (extension.equals("xls")) {
			workbook = new HSSFWorkbook(file.getInputStream());
		}
	    

	    Sheet worksheet = workbook.getSheetAt(0);

	    for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) { // 4

	      Row row = worksheet.getRow(i);

	      ExcelData data = new ExcelData();

	      data.setProductID(row.getCell(0).getStringCellValue());;
	      data.setProductName(row.getCell(1).getStringCellValue());
	      if(row.getCell(2) != null) {
	    	  data.setKeyword(row.getCell(2).getStringCellValue());  
	      }
	      data.setCategory(row.getCell(3).getStringCellValue());
	      data.setBrand(row.getCell(4).getStringCellValue());
	      
	      this.productService.excelCreate(data);
	    }

	    return "redirect:/";
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/productList")
	public String productList(Model model, Principal principal, @RequestParam(value="page", defaultValue="0") int page) {
		Page<ProductDTO> paging = this.productService.getPage(page);
		model.addAttribute("paging", paging);
		int StartNum = (int)Math.floor((double)paging.getNumber()/10)*10;
		int EndNum = (int)Math.ceil((double)paging.getNumber()/10)*10-1;
		if(StartNum-1 == EndNum) {
			EndNum = EndNum+10;
		}
		model.addAttribute("StartNum", StartNum);
		model.addAttribute("EndNum", EndNum);
		model.addAttribute("userId", principal.getName());
		return "productList";
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/productCreate")
	public String productCreate(Model model, ProductForm productForm, Principal principal) {
		model.addAttribute("productForm", productForm);
		model.addAttribute("userId", principal.getName());
		return "productCreate";
	}
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/productCreate")
	public String signUpPost(Model model, @Valid ProductForm productForm, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("productForm", productForm);
            return "signUp";
        }
		model.addAttribute("productForm", productForm);
		this.productService.create(productForm);
		return "redirect:/productList";
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/productUpdate/{id}")
	public String productUpdate(Model model, @PathVariable("id") String id, ProductUpdateForm producUpdateForm, Principal principal) {
		ProductDTO productDTO = this.productService.select(id);
		model.addAttribute("productDTO", productDTO);
		model.addAttribute("producUpdateForm", producUpdateForm);
		model.addAttribute("userId", principal.getName());
		return "productUpdate";
	}
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/productUpdate/{id}")
	public String productUpdatePost(Model model, @PathVariable("id") String id, ProductUpdateForm producUpdateForm) {
		//th:action의 경우 값을 안 넣으면 같은 url에 post 요청
		try {
			this.productService.update(id, producUpdateForm);
		} catch (Exception e) {
			return "redirect:/falseUpdate";
		}
		return "redirect:/successUpdate";
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping(value="/successUpdate", produces="text/html;charset=UTF-8")
	@ResponseBody
	public String successUpdate() {
		return "<script>alert('수정 완료했습니다!');location.href='/productList'</script>";
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping(value="/falseUpdate", produces="text/html;charset=UTF-8")
	@ResponseBody
	public String falseUpdate() {
		return "<script>alert('수정에 실패했습니다');location.href='/productList'</script>";
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/productDelete/{id}")
	@ResponseBody
	public String productDelete(Model model, @PathVariable("id") String id) {
		this.productService.delete(id);
		return "<script>alert('삭제 완료되었습니다');location.href='/productList'</script>";
	}
	
	@PreAuthorize("isAuthenticated()")
	@RequestMapping("/productIdSearch/{search}")
	public String productIdSearch(Model model, Principal principal, @PathVariable("search") String search, @RequestParam(value="page", defaultValue="0") int page) {
		Page<ProductDTO> paging = this.productService.getSearchId(search, page);
		model.addAttribute("paging", paging);
		int StartNum = (int)Math.floor((double)paging.getNumber()/10)*10;
		int EndNum = (int)Math.ceil((double)paging.getNumber()/10)*10-1;
		if(StartNum-1 == EndNum) {
			EndNum = EndNum+10;
		}
		model.addAttribute("StartNum", StartNum);
		model.addAttribute("EndNum", EndNum);
		model.addAttribute("userId", principal.getName());
		model.addAttribute("IdSearch", search);
		return "productList";
	}
	
	@PreAuthorize("isAuthenticated()")
	@RequestMapping("/productNameSearch/{search}")
	public String productNameSearch(Model model, Principal principal, @PathVariable("search") String search, @RequestParam(value="page", defaultValue="0") int page) {
		Page<ProductDTO> paging = this.productService.getSearchName(search, page);
		model.addAttribute("paging", paging);
		int StartNum = (int)Math.floor((double)paging.getNumber()/10)*10;
		int EndNum = (int)Math.ceil((double)paging.getNumber()/10)*10-1;
		if(StartNum-1 == EndNum) {
			EndNum = EndNum+10;
		}
		model.addAttribute("StartNum", StartNum);
		model.addAttribute("EndNum", EndNum);
		model.addAttribute("userId", principal.getName());
		model.addAttribute("NameSearch", search);
		return "productList";
	}
	
	@PreAuthorize("isAuthenticated()")
	@RequestMapping("/productKeySearch/{search}")
	public String productKeySearch(Model model, Principal principal, @PathVariable("search") String search, @RequestParam(value="page", defaultValue="0") int page) {
		Page<ProductDTO> paging = this.productService.getSearchKey(search, page);
		model.addAttribute("paging", paging);
		int StartNum = (int)Math.floor((double)paging.getNumber()/10)*10;
		int EndNum = (int)Math.ceil((double)paging.getNumber()/10)*10-1;
		if(StartNum-1 == EndNum) {
			EndNum = EndNum+10;
		}
		model.addAttribute("StartNum", StartNum);
		model.addAttribute("EndNum", EndNum);
		model.addAttribute("userId", principal.getName());
		model.addAttribute("KeySearch", search);
		return "productList";
	}
	
	@PreAuthorize("isAuthenticated()")
	@RequestMapping("/productCateSearch/{search}")
	public String productCateSearch(Model model, Principal principal, @PathVariable("search") String search, @RequestParam(value="page", defaultValue="0") int page) {
		Page<ProductDTO> paging = this.productService.getSearchCate(search, page);
		model.addAttribute("paging", paging);
		int StartNum = (int)Math.floor((double)paging.getNumber()/10)*10;
		int EndNum = (int)Math.ceil((double)paging.getNumber()/10)*10-1;
		if(StartNum-1 == EndNum) {
			EndNum = EndNum+10;
		}
		model.addAttribute("StartNum", StartNum);
		model.addAttribute("EndNum", EndNum);
		model.addAttribute("userId", principal.getName());
		model.addAttribute("CateSearch", search);
		return "productList";
	}
	
	@PreAuthorize("isAuthenticated()")
	@RequestMapping("/productBrandSearch/{search}")
	public String productBrandSearch(Model model, Principal principal, @PathVariable("search") String search, @RequestParam(value="page", defaultValue="0") int page) {
		Page<ProductDTO> paging = this.productService.getSearchBrand(search, page);
		model.addAttribute("paging", paging);
		int StartNum = (int)Math.floor((double)paging.getNumber()/10)*10;
		int EndNum = (int)Math.ceil((double)paging.getNumber()/10)*10-1;
		if(StartNum-1 == EndNum) {
			EndNum = EndNum+10;
		}
		model.addAttribute("StartNum", StartNum);
		model.addAttribute("EndNum", EndNum);
		model.addAttribute("userId", principal.getName());
		model.addAttribute("BrandSearch", search);
		return "productList";
	}
}