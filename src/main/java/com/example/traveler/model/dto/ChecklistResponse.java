package com.example.traveler.model.dto;

import com.example.traveler.model.entity.ChecklistEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ChecklistResponse {
    String title;
    int cId;
    int tId;
    List<ItemResponse> items;


    public ChecklistResponse() {
        // 기본 생성자
    }

    // 생성자에 매개변수로 title, cId, tId를 받아와서 초기화하고 items를 빈 리스트로 초기화
    public ChecklistResponse(String title, int cId, int tId) {
        this.title = title;
        this.cId = cId;
        this.tId = tId;
        this.items = new ArrayList<>(); // 빈 리스트로 초기화
    }

    // 생성자에 ChecklistEntity를 받아와서 ChecklistResponse 객체로 변환하는 코드 추가
    public ChecklistResponse(ChecklistEntity checklistEntity) {
        this.title = checklistEntity.getTitle();
        this.cId = checklistEntity.getCId();
        this.tId = checklistEntity.getTravel().getTId();
        // items는 생성자에서 미리 초기화했기 때문에 따로 설정할 필요 없음
    }
}
