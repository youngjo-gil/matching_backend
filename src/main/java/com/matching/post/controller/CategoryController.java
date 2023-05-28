package com.matching.post.controller;

import com.matching.common.dto.ResponseDto;
import com.matching.common.utils.ResponseUtil;
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
    public ResponseDto getCategory() {
        List<Category> categoryList = categoryRepository.findAll();
        return ResponseUtil.SUCCESS("카테고리 조회 완료", categoryList);
    }
}
