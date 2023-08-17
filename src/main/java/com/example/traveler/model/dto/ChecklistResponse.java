package com.example.traveler.model.dto;

import com.example.traveler.model.entity.ChecklistEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
// ChecklistResponse 클래스에 인자가 있는 생성자 추가
public class ChecklistResponse {
    private String title;
    private int cId;
    private int tId;
    private List<ItemResponse> items;

    public ChecklistResponse() {
        // 기본 생성자
    }

    public ChecklistResponse(String title, int cId) {
        this.title = title;
        this.cId = cId;
    }

    public ChecklistResponse(ChecklistEntity checklistEntity, ChecklistEntity checklistEntity1) {
    }
}
