package com.example.traveler.controller;

import com.example.traveler.config.BaseException;
import com.example.traveler.model.dto.CategoryRequest;
import lombok.AllArgsConstructor;
import com.example.traveler.model.entity.CategoryEntity;
import com.example.traveler.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;\
import org.springframework.http.HttpStatus;

import java.util.List;

@CrossOrigin
@AllArgsConstructor
@RestController
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService service;

    // 카테고리 생성 API
    @PostMapping
    public ResponseEntity<CategoryEntity> createCategory(@RequestBody CategoryRequest request) {
        try {
            CategoryEntity result = service.add(request);
            return ResponseEntity.ok(result);
        } catch (BaseException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // 카테고리명 수정 API
    @PatchMapping("/{id}")
    public ResponseEntity<CategoryEntity> updateCategoryName(
            @PathVariable("id") Long categoryId,
            @RequestBody CategoryRequest request) {
        try {
            CategoryEntity result = service.updateCategoryName(categoryId, request.getName());
            return ResponseEntity.ok(result);
        } catch (BaseException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // 카테고리 삭제 API
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable("id") Long categoryId) {
        try {
            service.deleteCategory(categoryId);
            return ResponseEntity.ok().build();
        } catch (BaseException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // 모든 카테고리 조회 API
    @GetMapping
    public ResponseEntity<List<CategoryEntity>> getAllCategories() {
        List<CategoryEntity> categories = service.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    // 편집완료 버튼을 누르면 변경사항을 데이터베이스에 반영하고, 임시 저장소를 비우도록 합니다.
    @PostMapping("/complete-edit")
    public ResponseEntity<?> completeEdit() {
        service.applyPendingChanges();
        return ResponseEntity.ok().build();
    }
}
