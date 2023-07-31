package com.example.traveler.model.entity;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ChecklistEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Long orderNumber; // Changed the name to orderNumber and type to Long

    @Column(nullable = false)
    private Boolean completed;

    @ManyToOne
    private CategoryEntity category;

    public boolean isCompleted() {
        return completed;
    }

    public Long getOrder() {
        return orderNumber;
    }

    public void setOrder(Long order) {
    }
}
