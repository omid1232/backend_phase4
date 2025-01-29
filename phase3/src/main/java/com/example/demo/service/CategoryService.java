package com.example.demo.service;

import com.example.demo.dto.CategoryDTO;
import com.example.demo.model.Category;
import com.example.demo.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    /**
     * Cache all categories
     */
    @Cacheable(value = "allCategories")
    public List<Category> getAllCategories() {
        System.out.println("Fetching all categories from database");
        return categoryRepository.findAll();
    }

    /**
     * When adding a new category, remove cached category list
     */
    @CacheEvict(value = "allCategories", allEntries = true)
    public Category addCategory(CategoryDTO categoryDTO) {
        System.out.println("Evicting category cache and adding new category");

        // Check if the category name already exists
        if (categoryRepository.existsByName(categoryDTO.getName())) {
            throw new IllegalArgumentException("Category already exists");
        }

        // Create and save the new category
        Category category = new Category();
        category.setName(categoryDTO.getName());
        return categoryRepository.save(category);
    }
}
