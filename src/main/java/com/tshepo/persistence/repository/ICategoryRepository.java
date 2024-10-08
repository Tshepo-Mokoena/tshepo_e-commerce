package com.tshepo.persistence.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.tshepo.persistence.Category;

@Repository
@Transactional(readOnly = true)
public interface ICategoryRepository extends PagingAndSortingRepository<Category, Long>{
	
	Optional<Category> findByName(String name);
	
	Optional<Category> findByCategoryId(String categoryId);

	Page<Category> findByNameContaining(String keyword, Pageable pageandSize);

}