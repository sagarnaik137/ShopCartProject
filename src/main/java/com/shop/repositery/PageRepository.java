package com.shop.repositery;

import org.springframework.data.jpa.repository.JpaRepository;
import com.shop.models.Page;
import java.util.List;

public interface PageRepository extends JpaRepository<Page, Integer>{

	Page findBySlug(String slug);

	Page findBySlugAndIdNot(String slug, int id);
	
	List<Page> findAllByOrderBySortingAsc();

}