package com.example.traveler.service;

import com.example.traveler.config.BaseException;
import com.example.traveler.model.dto.CategoryRequest;
import com.example.traveler.model.entity.CategoryEntity;
import com.example.traveler.repository.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import static com.example.traveler.config.BaseResponseStatus.*;

@Service
@AllArgsConstructor

public class CategoryService {

    private final CategoryRepository categoryRepository;
    private Map<Long, List<ChecklistItem>> pendingChanges = new HashMap<>();

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository
    }

    // 카테고리 생성
//    public CategoryEntity add(CategoryRequest request, Long userId) throws BaseException {
//        // 사용자 인증 등의 로직을 통해 userId가 유효한지 확인
//        // 예를 들어, userId가 로그인되어 있는지, 해당 여행 유저와 로그인 유저가 동일인인지 등을 체크
//
//        // 사용자 인증 실패 시 예외 처리
//        if (!isValidUser(userId)) {
//            throw new BaseException(HttpStatus.FORBIDDEN, "사용자 인증에 실패했습니다.");
//        }
//
//        // 데이터베이스 연결 실패 시 예외 처리
//        if (!isDatabaseConnected()) {
//            throw new BaseException(DATABASE_ERROR);
//        }
//
//        if (categoryRepository.existsByName(request.getName())) {
//            throw new BaseException(UPDATE_FAIL_CATEGORYNAME);
//        }
//
//        CategoryEntity categoryEntity = new CategoryEntity();
//        categoryEntity.setName(request.getName());
//        return categoryRepository.save(categoryEntity);
//    }

    // 카테고리 수정(patch)
    public CategoryEntity updateCategoryName(Long categoryId, String newName) throws BaseException {
        // 데이터베이스 연결 여부를 확인
        if (!isDatabaseConnected()) {
            throw new BaseException(DATABASE_ERROR);
        }

        // 기존 카테고리를 데이터베이스에서 조회
        CategoryEntity category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new BaseException(UPDATE_FAIL_CATEGORYNAME));

        // 카테고리명 변경
        category.setName(newName);

        // 변경사항을 임시로 저장
        saveToPendingChanges(categoryId, category);

        return category;
    }

    public void applyPendingChanges() {
        // pendingChanges 맵에 저장된 변경사항을 데이터베이스에 반영하는 로직 구현
        for (Map.Entry<Long, CategoryEntity> entry : pendingChanges.entrySet()) {
            Long categoryId = entry.getKey();
            CategoryEntity category = entry.getValue();
            categoryRepository.save(category);
        }

        // 변경사항을 반영한 후 임시 저장소 비우기
        pendingChanges.clear();
    }

    private void saveToPendingChanges(Long categoryId, CategoryEntity category) {
        // 변경사항을 임시 저장소에 저장
        pendingChanges.put(categoryId, category);
    }

    // 카테고리 삭제
    public void deleteCategory(Long categoryId) throws BaseException{
        CategoryEntity category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new BaseException(DELETE_FAIL_CATEGORY));

        // 카테고리 삭제 (임시 저장소에 저장)
        categoryRepository.delete(category);
        removeFromPendingChanges(categoryId);
    }

    // "편집완료" 버튼을 누르면 세션에 저장된 변경사항을 데이터베이스에 반영하는 메서드를 추가합니다.
    // 이 메서드는 세션에 저장된 변경사항을 데이터베이스에 반영하고, 임시 저장소를 비워주는 역할을 합니다.
    public void applyPendingChanges() {
        for (Map.Entry<Long, List<ChecklistItem>> entry : pendingChanges.entrySet()) {
            Long categoryId = entry.getKey();
            List<ChecklistItem> checklistItems = entry.getValue();

            // categoryId와 checklistItems를 이용하여 데이터베이스에 변경사항 반영하는 로직 구현
        }

        pendingChanges.clear();
    }

    // 임시 저장소에서 변경사항을 제거하는 메서드
    private void removeFromPendingChanges(Long categoryId) {
        pendingChanges.remove(categoryId);
    }
}
