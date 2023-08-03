package com.example.traveler.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemResponse {
    private Long iId;        // Change int to Long
    private String name;     // Change the variable name to match ItemRequest
    private Long order;
    private boolean isChecked;
    private int cId;

    public ItemResponse(Long iId, String name, Boolean isChecked, int cId) {
        this.iId = iId;
        this.name = name;
        this.isChecked = isChecked;
        this.cId = cId;
    }

    public ItemResponse(Long iId, String name, Boolean ischecked) {
        this.name = name;
        this.isChecked = isChecked;
        this.cId = cId;
    }
}
