package com.example.traveler.controller;

import com.example.traveler.model.dto.ChecklistRequest;
import com.example.traveler.model.entity.ChecklistEntity;
import com.example.traveler.service.ChecklistService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/checklist")
public class ChecklistController {
    private final ChecklistService service;

    public ChecklistController(ChecklistService service) {
        this.service = service;
    }

    // 특정 카테고리에 새로운 항목 추가 API
    @PostMapping("/{categoryId}/item")
    public ResponseEntity<ChecklistEntity> addItemToCategory(
            @PathVariable("categoryId") Long categoryId,
            @RequestBody ChecklistRequest request) {
        ChecklistEntity result = service.addItemToCategory(categoryId, request);
        return ResponseEntity.ok(result);
    }

    // 특정 카테고리 내의 특정 항목 이름 수정 API
    @PatchMapping("/{categoryId}/item/{itemId}")
    public ResponseEntity<ChecklistEntity> updateItemNameInCategory(
            @PathVariable("categoryId") Long categoryId,
            @PathVariable("itemId") Long itemId,
            @RequestBody ChecklistRequest request) {
        ChecklistEntity result = service.updateItemNameInCategory(categoryId, itemId, request.getTitle());
        return ResponseEntity.ok(result);
    }

    // 특정 카테고리 내의 특정 항목 삭제 API
    @DeleteMapping("/{categoryId}/item/{itemId}")
    public ResponseEntity<?> deleteItemFromCategory(
            @PathVariable("categoryId") Long categoryId,
            @PathVariable("itemId") Long itemId) {
        service.deleteItemFromCategory(categoryId, itemId);
        return ResponseEntity.ok().build();
    }

    // 특정 카테고리 내의 모든 항목 조회 API
    @GetMapping("/{categoryId}/items")
    public ResponseEntity<List<ChecklistEntity>> getAllItemsInCategory(
            @PathVariable("categoryId") Long categoryId) {
        List<ChecklistEntity> items = service.getAllItemsInCategory(categoryId);
        return ResponseEntity.ok(items);
    }

    // 특정 카테고리 내의 항목 상태를 변경하는 API
    @PatchMapping("/{categoryId}/item/{itemId}/status")
    public ResponseEntity<ChecklistEntity> changeItemStatusInCategory(
            @PathVariable("categoryId") Long categoryId,
            @PathVariable("itemId") Long itemId,
            @RequestParam("completed") boolean completed) {
        ChecklistEntity result = service.changeItemStatusInCategory(categoryId, itemId, completed);
        return ResponseEntity.ok(result);
    }
}

