package com.example.traveler.service;

import com.example.traveler.model.dto.CategoryRequest;
import com.example.traveler.model.entity.CategoryEntity;
import com.example.traveler.repository.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@AllArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryEntity add(CategoryRequest request) {
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setName(request.getName());
        return categoryRepository.save(categoryEntity);
    }

    public CategoryEntity updateCategoryName(Long categoryId, String newName) {
        CategoryEntity category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 카테고리를 찾을 수 없습니다."));
        category.setName(newName);
        return categoryRepository.save(category);
    }

    public void deleteCategory(Long categoryId) {
        CategoryEntity category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 카테고리를 찾을 수 없습니다."));
        categoryRepository.delete(category);
    }

    public List<CategoryEntity> getAllCategories() {
        return categoryRepository.findAll();
    }
}
