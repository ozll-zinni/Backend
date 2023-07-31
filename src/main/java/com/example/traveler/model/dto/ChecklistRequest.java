package com.example.traveler.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChecklistRequest {
    private String title;
    private Long order; // Change the type to Long
    private Boolean completed;
    private Long categoryId;
    private List<ChecklistRequest> items;
    private Long id;
}
