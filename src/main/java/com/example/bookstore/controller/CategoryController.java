package com.example.bookstore.controller;

import com.example.bookstore.entity.Category;
import com.example.bookstore.repository.CategoryRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    /**
     * Get all categories - used for dropdown in admin book creation form
     */
    @GetMapping
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    /**
     * Add a new category - used by admin settings page
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Category createCategory(@RequestBody Map<String, String> request) {
        String categoryName = request.get("categoryName");
        
        if (categoryName == null || categoryName.trim().isEmpty()) {
            throw new IllegalArgumentException("Category name is required");
        }
        
        // Check if category already exists
        if (categoryRepository.existsById(categoryName.trim())) {
            throw new IllegalArgumentException("Category '" + categoryName + "' already exists");
        }
        
        Category category = new Category(categoryName.trim());
        return categoryRepository.save(category);
    }
}
