package com.shop.repositery;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shop.models.User;

public interface UserRepository extends JpaRepository<User, Integer> {

	User findByUsername(String username);
}
