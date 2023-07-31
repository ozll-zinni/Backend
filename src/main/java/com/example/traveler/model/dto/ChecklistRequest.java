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
    private Long order; // Changed the type to Long
    private Boolean completed;
    private Long categoryId;
    private List<ChecklistRequest> items;
    private Long id;

    // Define the return type and implement the method body if needed
    public Boolean isCompleted() {
        // Implement the logic if required
        // For example, you can simply return the value of 'completed' field:
        return completed;
    }
}
