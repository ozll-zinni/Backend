package com.example.traveler.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
// ChecklistResponse 클래스에 인자가 있는 생성자 추가
public class ChecklistResponse {
    private String title;
    private int cId;

    public ChecklistResponse() {
        // 기본 생성자
    }

    public ChecklistResponse(String title, int cId) {
        this.title = title;
        this.cId = cId;
    }
}
