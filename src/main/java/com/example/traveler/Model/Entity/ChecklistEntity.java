package com.example.traveler.Model.Entity;

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

    @Column(name = "ChecklistOrder", nullable = false)
    private Long order;

    @Column(nullable = false)
    private Boolean completed;
}
