package com.example.traveler.model.entity;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 증가
    private Long id;

    @JoinColumn(nullable = false)
    private String name;

    @JoinColumn(nullable = false)
    private Long order; // Changed the name to 'order' to match ItemRequest

    @JoinColumn(nullable = false)
    private Boolean ischecked;

    @ManyToOne
    private ChecklistEntity checklist;

    public boolean isIschecked() {
        return ischecked;
    }
}
