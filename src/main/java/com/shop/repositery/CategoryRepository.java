package com.shop.repositery;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shop.models.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

	Category findByName(String slug);

	List<Category> findAllByOrderBySortingAsc();

	Category findByslug(String slug);

}
