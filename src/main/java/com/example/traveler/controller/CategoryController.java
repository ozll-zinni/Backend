package com.example.traveler.controller;

import com.example.traveler.model.dto.CategoryRequest;
import lombok.AllArgsConstructor;
import com.example.traveler.model.entity.CategoryEntity;
import com.example.traveler.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@AllArgsConstructor
@RestController
@RequestMapping("/category")
public class CategoryController {
    private final CategoryService service;

    // 카테고리 생성 API
    @PostMapping
    public ResponseEntity<CategoryEntity> createCategory(@RequestBody CategoryRequest request){
        CategoryEntity result = service.add(request);
        return ResponseEntity.ok(result);
    }

    // 카테고리명 수정 API
    @PatchMapping("/{id}")
    public ResponseEntity<CategoryEntity> updateCategoryName(
            @PathVariable("id") Long categoryId,
            @RequestBody CategoryRequest request) {
        CategoryEntity result = service.updateCategoryName(categoryId, request.getName());
        return ResponseEntity.ok(result);
    }

    // 카테고리 삭제 API
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable("id") Long categoryId) {
        service.deleteCategory(categoryId);
        return ResponseEntity.ok().build();
    }

    // 모든 카테고리 조회 API
    @GetMapping
    public ResponseEntity<List<CategoryEntity>> getAllCategories() {
        List<CategoryEntity> categories = service.getAllCategories();
        return ResponseEntity.ok(categories);
    }
}
