package com.kevin.managementsystem.repository;

import com.kevin.managementsystem.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}