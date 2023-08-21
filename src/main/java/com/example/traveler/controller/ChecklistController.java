package com.example.traveler.controller;

import com.example.traveler.config.BaseException;
import com.example.traveler.config.BaseResponse;
import com.example.traveler.model.dto.ChecklistRequest;
import com.example.traveler.model.dto.ChecklistResponse;
import com.example.traveler.model.dto.TitleChangeRequest;
import com.example.traveler.service.ChecklistService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;


import java.util.List;

import static com.example.traveler.config.BaseResponseStatus.DELETE_CATEGORY_FAIL;

@RestController
@RequestMapping("/checklist")
@CrossOrigin(origins = "http://localhost:3000")
public class ChecklistController {
    @Autowired
    private ChecklistService checklistService;
    @Autowired
    public ChecklistController(ChecklistService checklistService) {
        this.checklistService = checklistService;
    }

    @GetMapping("/travel/{tId}")
    public BaseResponse<List<ChecklistResponse>> getAllChecklistsByTravel(@PathVariable Integer tId) {
        try {
            List<ChecklistResponse> checklistResponses = checklistService.getAllChecklistsByTravel(tId);
            return new BaseResponse<>(checklistResponses);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());

        }
    }
    // 새로운 체크리스트 정보 저장
    @PostMapping("/{tId}")
    public BaseResponse<ChecklistResponse> saveChecklist(@RequestHeader("Authorization") String accessToken, @PathVariable Integer tId, @RequestBody ChecklistRequest checklistRequest) {
        try {
            ChecklistResponse checklistResponse = checklistService.saveChecklist(accessToken, tId, checklistRequest);
            return new BaseResponse<>(checklistResponse);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }
    // 특정 카테고리 정보 조회
    @GetMapping("/{cId}")
    public BaseResponse<ChecklistResponse> getChecklist(@PathVariable("cId") int cId) {
        try {
            ChecklistResponse checklistResponse = checklistService.getChecklist(cId);
            return new BaseResponse<>(checklistResponse);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    // 카테고리명 수정 API
    @PatchMapping("/{cId}/title") // 기존의 {cId}에 "/title"을 추가하여 제목 변경 API로 구성
    public BaseResponse<ChecklistResponse> patchChecklist(
            @RequestHeader("Authorization") String accessToken,
            @PathVariable("cId") int cId,
            @RequestBody TitleChangeRequest request) {
        try {
            ChecklistResponse checklistResponse = checklistService.patchChecklist(accessToken, cId, request.getNewTitle());
            return new BaseResponse<>(checklistResponse);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    // 카테고리 삭제 API
    @DeleteMapping("/{cId}")
    public BaseResponse<String> deleteChecklist(@RequestHeader("Authorization") String accessToken, @PathVariable("cId") int cId) {
        try {
            int result = checklistService.deleteChecklist(accessToken, cId);
            if (result != 1) {
                throw new BaseException(DELETE_CATEGORY_FAIL);
            } else {
                return new BaseResponse<>("카테고리 삭제에 성공했습니다.");
            }
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }
}