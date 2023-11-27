package com.commerce.webapp.commerceclonewebapp.repository;

import com.commerce.webapp.commerceclonewebapp.model.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {
    public Optional<Category> findById(Long id);
}
