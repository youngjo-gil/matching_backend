package com.matching.search.controller;


import com.matching.common.dto.ResponseDto;
import com.matching.common.utils.ResponseUtil;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/search")
public class SearchController {
    @GetMapping
    public ResponseDto getSearchList(
        @RequestParam Long pageNum,
        @RequestParam Long category,
        @RequestParam String keyword
    ) {
        System.out.println("Page Number: " + pageNum);
        System.out.println("category: " + category);
        System.out.println("keyword: " + keyword);
        return ResponseUtil.SUCCESS("카테고리 조회 완료", true);
    }
}
