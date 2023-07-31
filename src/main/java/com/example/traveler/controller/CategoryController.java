package com.example.traveler.controller;

import com.example.traveler.config.BaseException;
import com.example.traveler.config.BaseResponse;
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
    public ResponseEntity<BaseResponse<CategoryEntity>> createCategory(@RequestBody CategoryRequest request) {
        try {
            CategoryEntity result = service.add(request);
            return ResponseEntity.ok(new BaseResponse<>(result));
        } catch (BaseException exception) {
            return ResponseEntity.badRequest().body(new BaseResponse<>(exception.getStatus()));
        }
    }

    // 카테고리명 수정 API
    @PatchMapping("/{id}")
    public ResponseEntity<BaseResponse<CategoryEntity>> updateCategoryName(
            @PathVariable("id") Long categoryId,
            @RequestBody CategoryRequest request) {
        try {
            CategoryEntity result = service.updateCategoryName(categoryId, request.getName());
            return ResponseEntity.ok(new BaseResponse<>(result));
        } catch (BaseException exception) {
            return ResponseEntity.badRequest().body(new BaseResponse<>(exception.getStatus()));
        }
    }

    // 카테고리 삭제 API
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<?>> deleteCategory(@PathVariable("id") Long categoryId) {
        try {
            service.deleteCategory(categoryId);
            return ResponseEntity.ok(new BaseResponse<>("카테고리 삭제에 성공했습니다."));
        } catch (BaseException exception) {
            return ResponseEntity.badRequest().body(new BaseResponse<>(exception.getStatus()));
        }
    }

    // 모든 카테고리 조회 API
    @GetMapping
    public ResponseEntity<BaseResponse<List<CategoryEntity>>> getAllCategories() {
        try {
            List<CategoryEntity> categories = service.getAllCategories();
            return ResponseEntity.ok(new BaseResponse<>(categories));
        } catch (BaseException exception) {
            return ResponseEntity.badRequest().body(new BaseResponse<>(exception.getStatus()));
        }
    }
}
