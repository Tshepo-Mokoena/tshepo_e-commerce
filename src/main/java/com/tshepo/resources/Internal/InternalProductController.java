package com.tshepo.resources.Internal;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.tshepo.exception.ItemExistException;
import com.tshepo.exception.ItemNotFoundException;
import com.tshepo.persistence.Product;
import com.tshepo.service.IProductService;

@RestController
@RequestMapping("/api/ts-ecommerce/internal/products")
public class InternalProductController{
	
	private IProductService productService;
	
	@Autowired
	private void setProductController(IProductService productService) {this.productService = productService;}
	
	@PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
	public ResponseEntity<?> addProduct(@RequestPart("product") Product product, 
			@RequestPart("image") Optional<MultipartFile> image)
	{
		if(findByName(product.getName()).isPresent()) throw new ItemExistException(product.getName());
		
		return new ResponseEntity<>(productService.addProduct(product, image) ,HttpStatus.OK);
	}
		
	@PostMapping(value = "/update", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
	public ResponseEntity<?> updateProduct(@RequestPart("product") Product product, 
			@RequestPart("image") Optional<MultipartFile> image)
	{
		Product getProductById = findByProductId(product.getProductId())
				.orElseThrow(() -> new ItemNotFoundException(product.getProductId()));
		
		Optional<Product> getProductByName = findByName(product.getName());
		
		if(getProductByName.isPresent())		
			if(!getProductById.getProductId().contains(getProductByName.get().getProductId()))
				throw new ItemExistException(product.getName());
				
		getProductById.setName(product.getName());
		getProductById.setDesc(product.getDesc());
		getProductById.setPrice(product.getPrice());
		getProductById.setQty(product.getQty());
		
		return new ResponseEntity<>(productService.updateProduct(getProductById, image), HttpStatus.OK);
	}
	
	@GetMapping
	public ResponseEntity<?> getAllProducts(@RequestParam("keyword") Optional<String>  keyword,
			@RequestParam("pageSize") Optional<Integer> pageSize,
			@RequestParam("page") Optional<Integer> page)
	{
		return new ResponseEntity<>(
				productService.allProducts(
						keyword.orElse(""),
						page.orElse(0), pageSize.orElse(5)), 
				HttpStatus.OK);
	}	
	
	@GetMapping("/{productId}")
	public ResponseEntity<?> getProduct(@PathVariable("productId") String productId)
	{
		return new ResponseEntity<>(findByProductId(productId)
				.orElseThrow(() -> new ItemNotFoundException(productId)), HttpStatus.OK);
	}
	
	@DeleteMapping("/{productId}")
	public ResponseEntity<?> deleteProduct(@PathVariable("productId") String productId)
	{
		productService.deleteProduct(findByProductId(productId).orElseThrow(() -> new ItemNotFoundException(productId)));
		return new ResponseEntity<>(HttpStatus.OK);
	}	
	
	private Optional<Product> findByName(String name) { return productService.findByName(name); }
	
	private Optional<Product> findByProductId(String productId) { return productService.findByProductId(productId); }
	
}