package com.shop.repositery;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.shop.models.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {

	Product findBySlug(String slug);

	Product findBySlugAndIdNot(String slug, int id);

	Page<Product> findAll(Pageable pageable);

	List<Product> findAllByCategoryId(String string, PageRequest pageable);

	long countByCategoryId(String string);
}
