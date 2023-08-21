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
@CrossOrigin(origins = "http://localhost:3000")
public class ItemController {

    @Autowired
    private ItemService itemService;


    // 새로운 item을 특정 checklist에 저장
    @PostMapping("/{cId}/items")
    public BaseResponse<ItemResponse> saveItem(@RequestHeader("Authorization") String accessToken, @PathVariable("cId") int cId, @RequestBody ItemRequest itemRequest) {
        try {
            ItemResponse itemResponse = itemService.saveItem(accessToken, cId, itemRequest);
            return new BaseResponse<>(itemResponse);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    // 모든 item 정보 조회
    @GetMapping("")
    public BaseResponse<List<ItemResponse>> getAllItem() {
        List<ItemResponse> itemResponses = itemService.getAllItem();
        return new BaseResponse<>(itemResponses);
    }

    // 특정 아이템 정보 조회
    @GetMapping("/checklist/{iId}")
    public BaseResponse<ItemResponse> getItem(@PathVariable("iId") int iId) {
        try {
            ItemResponse itemResponse = itemService.getItem(iId);
            return new BaseResponse<>(itemResponse);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    // 특정 checklist에 포함된 모든 item 정보조회
    @GetMapping("/{cId}/items")
    public BaseResponse<List<ItemResponse>> getallItemByChecklist(@PathVariable("cId") int cId) {
        try {
            List<ItemResponse> itemResponses = itemService.getallItemByChecklist(cId);
            return new BaseResponse<>(itemResponses);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }


    @PatchMapping("/{cId}/items/{iId}") // 경로 변수 iId에 PathVariable 어노테이션을 추가해주세요
    public BaseResponse<ItemResponse> patchItem(@RequestHeader("Authorization") String accessToken, @PathVariable("cId") int cId, @PathVariable("iId") int iId, @RequestBody ItemRequest itemRequest) {
        try {
            ItemResponse itemResponse = itemService.patchItem(accessToken, cId, iId, itemRequest); // accessToke -> accessToken으로 수정
            return new BaseResponse<>(itemResponse);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    // 특정 checklist에 포함된 item 삭제
    @DeleteMapping("/{cId}/items/{iId}")
    public BaseResponse<String> deleteitem(@RequestHeader("Authorization") String accessToken, @PathVariable("cid") int cId, @PathVariable("iId") int iId) {
        try {
            int result = itemService.deleteItem(accessToken, cId, iId);
            if (result != 1) {
                throw new BaseException(DELETE_ITEM_FAIL);
            } else {
                return new BaseResponse<>("준비물 삭제에 성공했습니다.");
            }
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }
}



