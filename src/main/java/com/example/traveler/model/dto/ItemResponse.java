package com.example.traveler.model.dto;

import com.example.traveler.model.entity.ItemEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemResponse {
    private Long id;
    private String title;
    private Long order;
    private Boolean completed;
    private Long categoryId;
    private String categoryName;
    private String url;

    public ItemResponse(ItemEntity itemEntity){
        this.id = itemEntity.getId();
        this.title = itemEntity.getTitle();
        this.order = itemEntity.getOrder();
        this.completed = itemEntity.getCompleted();

        this.categoryId = itemEntity.getCategory().getId();
        this.categoryName = itemEntity.getCategory().getName();

        this.url = "http://localhost:9000/"+this.id;
    }
}
