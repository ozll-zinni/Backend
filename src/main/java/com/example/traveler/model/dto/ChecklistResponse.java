package com.example.traveler.model.dto;

import com.example.traveler.model.entity.ChecklistEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChecklistResponse {
    private Long id;
    private String title;
    private Long order;
    private Boolean completed;
    private Long categoryId;
    private String categoryName;
    private String url;

    public ChecklistResponse(ChecklistEntity checklistEntity){
        this.id = checklistEntity.getId();
        this.title = checklistEntity.getTitle();
        this.order = checklistEntity.getOrder();
        this.completed = checklistEntity.getCompleted();

        this.categoryId = checklistEntity.getCategory().getId();
        this.categoryName = checklistEntity.getCategory().getName();

        this.url = "http://localhost:9000/"+this.id;
    }
}
