package com.matching.post.controller;

import com.matching.post.domain.Category;
import com.matching.post.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryRepository categoryRepository;

    @GetMapping
    public ResponseEntity<?> getCategory() {
        List<Category> categoryList = categoryRepository.findAll();

        return ResponseEntity.ok().body(categoryList);
    }
}
