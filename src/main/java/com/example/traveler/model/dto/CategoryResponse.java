package com.example.traveler.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryResponse {
    private String name;
    private int categoryId;

    public void categoryId(int i) {
    }
}
