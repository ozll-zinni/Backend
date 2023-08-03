package com.example.traveler.controller;

import com.example.traveler.config.BaseException;
import com.example.traveler.config.BaseResponse;
import com.example.traveler.model.dto.ChecklistRequest;
import com.example.traveler.model.dto.ChecklistResponse;
import com.example.traveler.service.ChecklistService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;


import java.util.List;

import static com.example.traveler.config.BaseResponseStatus.DELETE_CATEGORY_FAIL;

@RestController
@RequestMapping("/checklsit")
public class ChecklistController {
    @Autowired
    private ChecklistService checklistService;
    @Autowired
    public ChecklistController(ChecklistService checklistService) {
        this.checklistService = checklistService;
    }

    @GetMapping("/travel/{tId}")
    public List<ChecklistResponse> getAllChecklistsByTravel(@PathVariable Integer tId) throws BaseException {
        return checklistService.getAllChecklistsByTravel(tId);
    }
    // 새로운 체크리스트 정보 저장
    @PostMapping("/checklist")
    public BaseResponse<ChecklistResponse> saveChecklist(@RequestBody Integer checklistRequest) {
        try {
            ChecklistResponse checklistResponse = checklistService.saveChecklist(checklistRequest);
            return new BaseResponse<>(checklistResponse);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    // 특정 카테고리 정보 조회
    @GetMapping("/checklist/{cId}")
    public BaseResponse<ChecklistResponse> getChecklist(@PathVariable("cId") int cId) {
        try {
            ChecklistResponse checklistResponse = checklistService.getChecklist(cId);
            return new BaseResponse<>(checklistResponse);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    // 카테고리명 수정 API
    @PatchMapping("/{cId}")
    public BaseResponse<ChecklistResponse> patchChecklist(
            @PathVariable("cId") int cId,
            @RequestBody ChecklistRequest request) {
        try {
            ChecklistResponse checklistResponse = checklistService.patchChecklist(cId, request.getTitle());
            return new BaseResponse<>(checklistResponse);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    // 카테고리 삭제 API
    @DeleteMapping("/{cId}")
    public BaseResponse<String> deleteChecklist(
            @PathVariable("cId") int cId) {
        try {
            int result = checklistService.deleteChecklist(cId);
            if (result != 1) {
                throw new BaseException(DELETE_CATEGORY_FAIL);
            } else {
                return new BaseResponse<>("카테고리 삭제에 성공했습니다.");
            }
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

//    // 모든 카테고리 조회 API
//    @GetMapping("/checklist")
//    public BaseResponse<List<ChecklistResponse>> getAllChecklist() {
//        List<ChecklistResponse> checklistResponses = checklistService.getAllChecklist();
//        return new BaseResponse<>(checklistResponses);
//    }

}
