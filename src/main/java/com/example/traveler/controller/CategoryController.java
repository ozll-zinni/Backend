package com.example.traveler.controller;

import com.example.traveler.config.BaseException;
import com.example.traveler.config.BaseResponse;
import com.example.traveler.model.dto.CategoryRequest;
import com.example.traveler.model.dto.CategoryResponse;
import lombok.AllArgsConstructor;
import com.example.traveler.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.traveler.config.BaseResponseStatus.DELETE_CATEGORY_FAIL;

@CrossOrigin
@AllArgsConstructor
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    // 카테고리 생성 API
    @PostMapping("")
    public BaseResponse<CategoryResponse> createCategory(@RequestBody CategoryRequest request) {
        try {
            CategoryResponse categoryResponse = categoryService.add(request);
            return new BaseResponse<>(categoryResponse);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    // 카테고리명 수정 API
    @PatchMapping("/{categoryId}")
    public BaseResponse<CategoryResponse> updateCategoryName(
            @PathVariable("categoryId") Long categoryId,
            @RequestBody CategoryRequest request) {
        try {
            CategoryResponse categoryResponse = categoryService.updateCategoryName(categoryId, request.getName());
            return new BaseResponse<>(categoryResponse);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    // 카테고리 삭제 API
    @DeleteMapping("/{categoryId}")
    public BaseResponse<String> deleteCategory(
            @PathVariable("categoryId") Long categoryId) {
        try {
            int result = categoryService.deleteCategory(categoryId);
            if (result != 1) {
                throw new BaseException(DELETE_CATEGORY_FAIL);
            } else {
                return new BaseResponse<>("카테고리 삭제에 성공했습니다.");
            }
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    // 모든 카테고리 조회 API
    @GetMapping("/category")
    public BaseResponse<List<CategoryResponse>> getAllCategories() {
        List<CategoryResponse> categoryResponses = categoryService.getAllCategories();
        return new BaseResponse<>(categoryResponses);
    }

//    // 모든 카테고리 조회 API
//    @GetMapping("")
//    public BaseResponse<List<CategoryResponse>> getAllCategories() {
//        try {
//            List<CategoryResponse> categoryResponses = categoryService.getAllCategories();
//            return new BaseResponse<>(categoryResponses);
//        } catch (BaseException exception) {
//            return new BaseResponse<>(exception.getStatus());
//        }
//    }
}
