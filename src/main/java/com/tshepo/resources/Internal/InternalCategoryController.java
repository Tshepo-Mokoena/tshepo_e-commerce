package com.tshepo.resources.Internal;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.tshepo.exception.ItemExistException;
import com.tshepo.exception.ItemNotFoundException;
import com.tshepo.persistence.Category;
import com.tshepo.persistence.Product;
import com.tshepo.service.ICategoryService;

@RestController
@RequestMapping("/api/ts-ecommerce/internal/category")
public class InternalCategoryController {
	
	private ICategoryService categoryService;
	
	@Autowired
	private void setInternalCategoryController(ICategoryService categoryService)
	{
		this.categoryService = categoryService;		
	}
	
	@PostMapping
	public ResponseEntity<?> createNewcategory(@RequestBody @Valid Category category) 
	{
		if (!categoryService.findByName(category.getName()).isEmpty())
			throw new ItemExistException(category.getName());
		
		Category savedCategory = categoryService.newCategory(category);
		
		URI locationUri = 
				ServletUriComponentsBuilder
				.fromCurrentContextPath().path("/{categoryId}")
				.buildAndExpand(savedCategory.getCategoryId()).toUri();
			
		return new ResponseEntity<>(locationUri, HttpStatus.CREATED); 
	}
	
	@PostMapping("/update")
	public ResponseEntity<?> updatecategory(@RequestBody @Valid Category category) 
	{
		Category getCategory = categoryService.findByCategoryId(category.getCategoryId())
				.orElseThrow(() -> new ItemNotFoundException(category.getCategoryId()));
		
		Category getCategoryName = categoryService.findByName(category.getName()).get();
		
		if (getCategoryName != null)
			if(getCategory.getCategoryId() !=  getCategoryName.getCategoryId())
				throw new ItemExistException(category.getName());
		
		getCategory.setName(category.getName());
				
		categoryService.updateCategory(getCategory);
		
		return new ResponseEntity<>(HttpStatus.OK); 
	}
	
	@GetMapping("/{categoryId}")
	public ResponseEntity<?> getCategory(@PathVariable String categoryId) 
	{
		if (categoryId.isBlank())
			throw new RuntimeException("category id should not be blank");
		
		Category getCategory = categoryService.findByCategoryId(categoryId)
				.orElseThrow(() -> new ItemExistException(categoryId));
		
		return new ResponseEntity<>(getCategory,HttpStatus.OK);
	}
	
	@GetMapping
	public ResponseEntity<?> getCategories(
			@RequestParam("keyword") Optional<String>  keyword,
			@RequestParam("pageSize") Optional<Integer> pageSize,
			@RequestParam("page") Optional<Integer> page) 
	{
		return new ResponseEntity<>(categoryService.findAll(keyword.orElse(""),
				(page.orElse(0) < 1) ? 0 : page.get() - 1, pageSize.orElse(5)),HttpStatus.OK);
	}
	
	@GetMapping("/{categoryId}/products")
	public ResponseEntity<?> getCategoryProducts(@PathVariable String categoryId) 
	{
		List<Product> getProducts = categoryService.findByCategoryId(categoryId).get().getProducts();
		
		if (getProducts.isEmpty()) 
			throw new ItemNotFoundException(categoryId);
		
		return new ResponseEntity<>(categoryService.findByCategoryId(categoryId).get(), HttpStatus.OK);
	}


}
