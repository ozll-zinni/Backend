package com.example.traveler.controller;

import com.example.traveler.config.BaseException;
import com.example.traveler.config.BaseResponse;
import com.example.traveler.model.dto.ItemRequest;
import com.example.traveler.model.dto.ItemResponse;
import com.example.traveler.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.traveler.config.BaseResponseStatus.DELETE_ITEM_FAIL;

@RestController
@RequestMapping("/checklist")
public class ItemController {

    @Autowired
    private ItemService itemService;

//    public ChecklistController(ChecklistService service) {
//        this.service = service;
//    }

    // 특정 카테고리에 새로운 항목 추가 API
    @PostMapping("/{categoryId}/item")
    public BaseResponse<ItemResponse> addItemToCategory(
            @PathVariable("categoryId") Long categoryId,
            @RequestBody ItemRequest request) throws BaseException {
        ItemResponse itemResponse = itemService.addItemToCategory(categoryId, request);
        return new BaseResponse<>(itemResponse);
    }

    // 특정 카테고리 내의 특정 항목 이름 수정 API
    @PatchMapping("/{categoryId}/item/{itemId}")
    public BaseResponse<ItemResponse> updateItemNameInCategory(
            @PathVariable("categoryId") Long categoryId,
            @PathVariable("itemId") Long itemId,
            @RequestBody ItemRequest request) throws BaseException {
        ItemResponse itemResponse = itemService.updateItemNameInCategory(categoryId,itemId, request.getTitle());
        return new BaseResponse<>(itemResponse);
    }

    // 특정 카테고리 내의 특정 항목 삭제 API
    @DeleteMapping("/{categoryId}/item/{itemId}")
    public BaseResponse<String> deleteItemFromCategory(
            @PathVariable("categoryId") Long categoryId,
            @PathVariable("itemId") Long itemId) {
        try {
            int result = itemService.deleteItemFromCategory(categoryId, itemId);
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
    public BaseResponse<List<ItemResponse>> getAllItemsInCategory(
            @PathVariable("categoryId") Long categoryId) throws BaseException {
        List<ItemResponse> itemRespons = itemService.getAllItemsInCategory(categoryId);
        return new BaseResponse<>(itemRespons);
    }

    // 특정 카테고리 내의 항목 상태를 변경하는 API
    @PatchMapping("/{categoryId}/item/{itemId}/status")
    public BaseResponse<ItemResponse> changeItemStatusInCategory(
            @PathVariable("categoryId") Long categoryId,
            @PathVariable("itemId") Long itemId,
            @RequestParam("completed") boolean completed) throws BaseException {
        ItemResponse itemResponse = itemService.changeItemStatusInCategory(categoryId, itemId, completed);
        return new BaseResponse<>(itemResponse);
    }
}

