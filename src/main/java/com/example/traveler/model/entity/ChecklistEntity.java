package com.example.traveler.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ChecklistEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int cId;

    @Column(nullable = false)
    private String title;

    // TravelEntity와의 다대일 관계 설정
    @ManyToOne
    @JoinColumn(name = "tId") // tId는 TravelEntity의 PK (여행 ID)
    private Travel travel;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "checklist")
    private List<ItemEntity> checklistItems = new ArrayList<>();

}
