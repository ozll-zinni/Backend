package com.example.traveler.service;

import com.example.traveler.config.BaseException;
import com.example.traveler.model.dto.ChecklistRequest;
import com.example.traveler.model.dto.ChecklistResponse;
import com.example.traveler.model.entity.CategoryEntity;
import com.example.traveler.model.entity.ChecklistEntity;
import com.example.traveler.repository.CategoryRepository;
import com.example.traveler.repository.ChecklistRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.traveler.config.BaseResponseStatus.*;

@Service
@AllArgsConstructor
public class ChecklistService {

    private final ChecklistRepository checklistRepository;
    private final CategoryRepository categoryRepository;

    // 특정 카테고리에 새로운 항목 추가
    public ChecklistResponse addItemToCategory(Long categoryId, ChecklistRequest request) throws BaseException {
        CategoryEntity category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new BaseException(CATEGORY_NOT_FOUND));

        ChecklistEntity checklistEntity = new ChecklistEntity();
        checklistEntity.setTitle(request.getTitle());
        checklistEntity.setOrder(request.getOrder());
        checklistEntity.setCompleted(request.isCompleted());
        checklistEntity.setCategory(category);

        ChecklistEntity savedChecklistEntity = checklistRepository.save(checklistEntity);
        return mapChecklistEntityToResponse(savedChecklistEntity);
    }

    // 특정 카테고리 내의 특정 항목 이름 수정
    public ChecklistResponse updateItemNameInCategory(Long categoryId, Long itemId, String newItemName) throws BaseException {
        CategoryEntity category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new BaseException(CATEGORY_NOT_FOUND));

        ChecklistEntity checklistEntity = (ChecklistEntity) checklistRepository.findByCategoryAndId(category, itemId)
                .orElseThrow(() -> new BaseException(ITEM_NOT_FOUND));

        checklistEntity.setTitle(newItemName);

        ChecklistEntity updatedChecklistEntity = checklistRepository.save(checklistEntity);
        return mapChecklistEntityToResponse(updatedChecklistEntity);
    }

    // 특정 카테고리 내의 특정 항목 삭제
    public int deleteItemFromCategory(Long categoryId, Long itemId) throws BaseException {
        CategoryEntity category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new BaseException(CATEGORY_NOT_FOUND));

        ChecklistEntity checklistEntity = (ChecklistEntity) checklistRepository.findByCategoryAndId(category, itemId)
                .orElseThrow(() -> new BaseException(ITEM_NOT_FOUND));

        checklistRepository.delete(checklistEntity);
        return 0;
    }

    // 특정 카테고리 내의 모든 항목 조회
    public List<ChecklistResponse> getAllItemsInCategory(Long categoryId) throws BaseException {
        CategoryEntity category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new BaseException(CATEGORY_NOT_FOUND));

        List<ChecklistEntity> checklistEntities = checklistRepository.findByCategoryOrderByOrderAsc(category);
        return checklistEntities.stream()
                .map(this::mapChecklistEntityToResponse)
                .collect(Collectors.toList());
    }

    // 특정 카테고리 내의 항목 상태 변경
    public ChecklistResponse changeItemStatusInCategory(Long categoryId, Long itemId, boolean completed) throws BaseException {
        CategoryEntity category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new BaseException(CATEGORY_NOT_FOUND));

        ChecklistEntity checklistEntity = (ChecklistEntity) checklistRepository.findByCategoryAndId(category, itemId)
                .orElseThrow(() -> new BaseException(ITEM_NOT_FOUND));

        checklistEntity.setCompleted(completed);

        ChecklistEntity updatedChecklistEntity = checklistRepository.save(checklistEntity);
        return mapChecklistEntityToResponse(updatedChecklistEntity);
    }

    // ChecklistEntity를 ChecklistResponse로 변환하는 도우미 메서드
    private ChecklistResponse mapChecklistEntityToResponse(ChecklistEntity checklistEntity) {
        ChecklistResponse response = new ChecklistResponse();
        response.setId(checklistEntity.getId());
        response.setTitle(checklistEntity.getTitle());
        response.setOrder(checklistEntity.getOrder());
        response.setCompleted(checklistEntity.isCompleted());
        return response;
    }
}
