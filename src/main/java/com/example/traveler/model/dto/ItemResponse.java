package com.example.traveler.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class ItemResponse {
    private Long iId;
    private String name;
    private Long itemOrder;
    private Boolean isChecked; // 변수 이름 변경
    private int cId;

    public ItemResponse(Long iId, String name, Boolean isChecked, int cId) {
        this.iId = iId;
        this.name = name;
        this.isChecked = isChecked;
        this.cId = cId;
    }

    public ItemResponse(Long iId, String name, Boolean isChecked) {
        this.iId = iId;
        this.name = name;
        this.isChecked = isChecked;
    }

    // setter 메서드 추가
    public void setChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }


    // getter 메서드는 롬복의 @Data 어노테이션으로 자동 생성됨
}
