package com.example.traveler.service;

import com.example.traveler.model.entity.CategoryEntity;
import com.example.traveler.repository.CategoryRepository;
import lombok.AllArgsConstructor;
import com.example.traveler.model.entity.ChecklistEntity;
import com.example.traveler.model.dto.ChecklistRequest;
import com.example.traveler.repository.ChecklistRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@AllArgsConstructor
public class ChecklistService {

    private final ChecklistRepository checklistRepository;
    private final CategoryRepository categoryRepository;

    // 특정 카테고리에 새로운 항목 추가
    public ChecklistEntity addItemToCategory(Long categoryId, ChecklistRequest request) {
        CategoryEntity category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 카테고리를 찾을 수 없습니다."));

        ChecklistEntity checklistEntity = new ChecklistEntity();
        checklistEntity.setTitle(request.getTitle());
        checklistEntity.setOrder(request.getOrder());
        checklistEntity.setCompleted(request.getCompleted());
        checklistEntity.setCategory(category);

        return this.checklistRepository.save(checklistEntity);
    }

    // 특정 카테고리 내의 특정 항목 이름 수정
    public ChecklistEntity updateItemNameInCategory(Long categoryId, Long itemId, String newItemName) {
        ChecklistEntity checklistEntity = this.checklistRepository.findById(itemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 항목을 찾을 수 없습니다."));

        // 특정 항목의 이름을 업데이트합니다.
        checklistEntity.setTitle(newItemName);

        // 변경된 체크리스트를 저장하고 반환합니다.
        return this.checklistRepository.save(checklistEntity);
    }

    // 특정 카테고리 내의 특정 항목 삭제
    public void deleteItemFromCategory(Long categoryId, Long itemId) {
        // 해당 카테고리와 관련된 체크리스트 항목을 삭제합니다.
        ChecklistEntity checklistEntity = this.checklistRepository.findById(itemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 항목을 찾을 수 없습니다."));
        if (!checklistEntity.getCategory().getId().equals(categoryId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "해당 카테고리에 속한 항목이 아닙니다.");
        }

        this.checklistRepository.deleteById(itemId);
    }

    // 특정 카테고리 내의 모든 항목 조회
    public List<ChecklistEntity> getAllItemsInCategory(Long categoryId) {
        CategoryEntity category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 카테고리를 찾을 수 없습니다."));

        return category.getChecklistItems();
    }

    // 특정 카테고리 내의 항목 상태 변경
    public ChecklistEntity changeItemStatusInCategory(Long categoryId, Long itemId, boolean completed) {
        // 해당 카테고리와 관련된 체크리스트 항목을 조회합니다.
        ChecklistEntity checklistEntity = this.checklistRepository.findById(itemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 항목을 찾을 수 없습니다."));

        // 항목이 해당 카테고리에 속하는지 확인합니다.
        if (!checklistEntity.getCategory().getId().equals(categoryId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "해당 카테고리에 속한 항목이 아닙니다.");
        }

        // 항목의 상태를 변경합니다.
        checklistEntity.setCompleted(completed);

        // 변경된 체크리스트를 저장하고 반환합니다.
        return this.checklistRepository.save(checklistEntity);
    }
}



