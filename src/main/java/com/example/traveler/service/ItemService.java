package com.example.traveler.service;

import com.example.traveler.model.entity.ChecklistEntity;
import com.example.traveler.repository.ChecklistRepository;
import lombok.AllArgsConstructor;
import com.example.traveler.model.entity.ItemEntity;
import com.example.traveler.model.dto.ItemRequest;
import com.example.traveler.repository.ItemRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@AllArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final ChecklistRepository checklistRepository;

    // 특정 카테고리에 새로운 항목 추가
    public ItemEntity addItemToCategory(Long categoryId, ItemRequest request) {
        ChecklistEntity category = checklistRepository.findById(categoryId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 카테고리를 찾을 수 없습니다."));

        ItemEntity itemEntity = new ItemEntity();
        itemEntity.setTitle(request.getTitle());
        itemEntity.setOrder(request.getOrder());
        itemEntity.setCompleted(request.getCompleted());
        itemEntity.setCategory(category);

        return this.itemRepository.save(itemEntity);
    }

    // 특정 카테고리 내의 특정 항목 이름 수정
    public ItemEntity updateItemNameInCategory(Long categoryId, Long itemId, String newItemName) {
        ItemEntity itemEntity = this.itemRepository.findById(itemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 항목을 찾을 수 없습니다."));

        // 특정 항목의 이름을 업데이트합니다.
        itemEntity.setTitle(newItemName);

        // 변경된 체크리스트를 저장하고 반환합니다.
        return this.itemRepository.save(itemEntity);
    }

    // 특정 카테고리 내의 특정 항목 삭제
    public int deleteItemFromCategory(Long categoryId, Long itemId) {
        // 해당 카테고리와 관련된 체크리스트 항목을 삭제합니다.
        ItemEntity itemEntity = this.itemRepository.findById(itemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 항목을 찾을 수 없습니다."));
        if (!itemEntity.getCategory().getId().equals(categoryId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "해당 카테고리에 속한 항목이 아닙니다.");
        }

        this.itemRepository.deleteById(itemId);
        return 0;
    }

    // 특정 카테고리 내의 모든 항목 조회
    public List<ItemEntity> getAllItemsInCategory(Long categoryId) {
        ChecklistEntity category = checklistRepository.findById(categoryId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 카테고리를 찾을 수 없습니다."));

        return category.getChecklistItems();
    }

    // 특정 카테고리 내의 항목 상태 변경
    public ItemEntity changeItemStatusInCategory(Long categoryId, Long itemId, boolean completed) {
        // 해당 카테고리와 관련된 체크리스트 항목을 조회합니다.
        ItemEntity itemEntity = this.itemRepository.findById(itemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 항목을 찾을 수 없습니다."));

        // 항목이 해당 카테고리에 속하는지 확인합니다.
        if (!itemEntity.getCategory().getId().equals(categoryId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "해당 카테고리에 속한 항목이 아닙니다.");
        }

        // 항목의 상태를 변경합니다.
        itemEntity.setCompleted(completed);

        // 변경된 체크리스트를 저장하고 반환합니다.
        return this.itemRepository.save(itemEntity);
    }
}



