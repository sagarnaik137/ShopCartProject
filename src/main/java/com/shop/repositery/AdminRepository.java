package com.shop.repositery;

import org.springframework.data.jpa.repository.JpaRepository;
import com.shop.models.Admin;

public interface AdminRepository extends JpaRepository<Admin, Integer> {

	Admin findByUsername(String username);
}
