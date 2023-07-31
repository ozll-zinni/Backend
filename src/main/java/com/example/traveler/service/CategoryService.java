package com.example.traveler.service;

import com.example.traveler.config.BaseException;
import com.example.traveler.model.dto.CategoryRequest;
import com.example.traveler.model.dto.CategoryResponse;
import com.example.traveler.model.entity.CategoryEntity;
import com.example.traveler.repository.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.traveler.config.BaseResponseStatus.*;

@Service
@AllArgsConstructor
public class CategoryService {

    @Autowired
    private UserService userService;

    private final CategoryRepository categoryRepository;
    private final Map<Long, CategoryEntity> pendingChanges = new HashMap<>();

    // 카테고리 생성
    public CategoryEntity add(CategoryRequest request) throws BaseException {
        // 데이터베이스 연결 여부를 확인
        if (!isDatabaseConnected()) {
            throw new BaseException(DATABASE_ERROR);
        }

        if (categoryRepository.existsByName(request.getName())) {
            throw new BaseException(SAVE_CATEGORY_FAIL);
        }

        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setName(request.getName());
        return categoryRepository.save(categoryEntity);
    }

    // 카테고리 수정(patch)
    public CategoryEntity updateCategoryName(Long categoryId, String newName) throws BaseException {
        // 데이터베이스 연결 여부를 확인
        if (!isDatabaseConnected()) {
            throw new BaseException(DATABASE_ERROR);
        }

        // 기존 카테고리를 데이터베이스에서 조회
        CategoryEntity category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new BaseException(UPDATE_CATEGORYNAME_FAIL));

        // 카테고리명 변경
        category.setName(newName);

        // 변경사항을 임시로 저장
        saveToPendingChanges(categoryId, category);

        return category;
    }

    // 카테고리 삭제
    public int deleteCategory(Long categoryId) throws BaseException {
        // 데이터베이스 연결 여부를 확인
        if (!isDatabaseConnected()) {
            throw new BaseException(DATABASE_ERROR);
        }

        CategoryEntity category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new BaseException(DELETE_CATEGORY_FAIL));

        // 카테고리 삭제 (임시 저장소에 저장)
        categoryRepository.delete(category);
        removeFromPendingChanges(categoryId);
        return 0;
    }

//    // "편집완료" 버튼을 누르면 세션에 저장된 변경사항을 데이터베이스에 반영하는 메서드를 추가합니다.
//    // 이 메서드는 세션에 저장된 변경사항을 데이터베이스에 반영하고, 임시 저장소를 비워주는 역할을 합니다.
//    public void applyPendingChanges() {
//        for (Map.Entry<Long, CategoryEntity> entry : pendingChanges.entrySet()) {
//            Long categoryId = entry.getKey();
//            CategoryEntity category = entry.getValue();
//
//            // categoryId와 category를 이용하여 데이터베이스에 변경사항 반영하는 로직 구현
//            categoryRepository.save(category);
//        }
//
//        // 변경사항을 반영한 후 임시 저장소 비우기
//        pendingChanges.clear();
//    }

    // 임시 저장소에 변경사항을 저장하는 메서드
    private void saveToPendingChanges(Long categoryId, CategoryEntity category) {
        pendingChanges.put(categoryId, category);
    }

    // 임시 저장소에서 변경사항을 제거하는 메서드
    private void removeFromPendingChanges(Long categoryId) {
        pendingChanges.remove(categoryId);
    }

    // 데이터베이스 연결 여부를 확인하는 가짜 메서드 (테스트용)
    private boolean isDatabaseConnected() {
        // 여기서 데이터베이스 연결 상태를 체크하고 결과를 반환한다.
        return true; // 연결됨
        // return false; // 연결 실패
    }


    // 모든 카테고리 조회 API의 구현
    public List<CategoryResponse> getAllCategories() {
        List<CategoryEntity> categories = categoryRepository.findAll();

        List<CategoryResponse> categoryResponses = new ArrayList<>();
        for (CategoryEntity category : categories) {
            CategoryResponse response = new CategoryResponse();
            response.setName(category.getName());
            response.categoryId(category.getId().intValue());
            categoryResponses.add(response);
        }

        return categoryResponses;
    }
}
