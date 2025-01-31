package com.example.demo.controller;

import com.example.demo.dto.CategoryDTO;
import com.example.demo.model.Category;
import com.example.demo.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public ResponseEntity<Category> addCategory(@RequestBody CategoryDTO categoryDTO) {
        Category category = categoryService.addCategory(categoryDTO);
        return ResponseEntity.status(201).body(category);
    }
    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        System.out.println("hello from category api");
        List<Category> categories = categoryService.getAllCategories();
        System.out.println("hello from category api");
        System.out.println(categories);
        return ResponseEntity.ok(categories);
    }
}


