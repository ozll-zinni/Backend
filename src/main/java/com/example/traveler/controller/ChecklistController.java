package com.example.traveler.controller;

import com.example.traveler.config.BaseException;
import com.example.traveler.config.BaseResponse;
import com.example.traveler.model.dto.ChecklistRequest;
import com.example.traveler.model.dto.ChecklistResponse;
import com.example.traveler.service.ChecklistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.traveler.config.BaseResponseStatus.DELETE_ITEM_FAIL;

@RestController
@RequestMapping("/checklist")
public class ChecklistController {

    @Autowired
    private ChecklistService checklistService;

//    public ChecklistController(ChecklistService service) {
//        this.service = service;
//    }

    // 특정 카테고리에 새로운 항목 추가 API
    @PostMapping("/{categoryId}/item")
    public BaseResponse<ChecklistResponse> addItemToCategory(
            @PathVariable("categoryId") Long categoryId,
            @RequestBody ChecklistRequest request) throws BaseException {
        ChecklistResponse checklistResponse = checklistService.addItemToCategory(categoryId, request);
        return new BaseResponse<>(checklistResponse);
    }

    // 특정 카테고리 내의 특정 항목 이름 수정 API
    @PatchMapping("/{categoryId}/item/{itemId}")
    public BaseResponse<ChecklistResponse> updateItemNameInCategory(
            @PathVariable("categoryId") Long categoryId,
            @PathVariable("itemId") Long itemId,
            @RequestBody ChecklistRequest request) throws BaseException {
        ChecklistResponse checklistResponse = checklistService.updateItemNameInCategory(categoryId,itemId, request.getTitle());
        return new BaseResponse<>(checklistResponse);
    }

    // 특정 카테고리 내의 특정 항목 삭제 API
    @DeleteMapping("/{categoryId}/item/{itemId}")
    public BaseResponse<String> deleteItemFromCategory(
            @PathVariable("categoryId") Long categoryId,
            @PathVariable("itemId") Long itemId) {
        try {
            int result = checklistService.deleteItemFromCategory(categoryId, itemId);
            if (result != 1) {
                throw new BaseException(DELETE_ITEM_FAIL);
            } else {
                return new BaseResponse<>("준비물 삭제에 성공했습니다.");
            }
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }


    // 특정 카테고리 내의 모든 항목 조회 API
    @GetMapping("/{categoryId}/items")
    public BaseResponse<List<ChecklistResponse>> getAllItemsInCategory(
            @PathVariable("categoryId") Long categoryId) throws BaseException {
        List<ChecklistResponse> checklistResponses = checklistService.getAllItemsInCategory(categoryId);
        return new BaseResponse<>(checklistResponses);
    }

    // 특정 카테고리 내의 항목 상태를 변경하는 API
    @PatchMapping("/{categoryId}/item/{itemId}/status")
    public BaseResponse<ChecklistResponse> changeItemStatusInCategory(
            @PathVariable("categoryId") Long categoryId,
            @PathVariable("itemId") Long itemId,
            @RequestParam("completed") boolean completed) throws BaseException {
        ChecklistResponse checklistResponse = checklistService.changeItemStatusInCategory(categoryId, itemId, completed);
        return new BaseResponse<>(checklistResponse);
    }
}

