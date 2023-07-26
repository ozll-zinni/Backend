package com.example.traveler.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class DayCourse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int dcId;

    @ManyToOne
    @JoinColumn(name="tId")
    Travel travel;

    @OneToOne
    @JoinColumn(name="sId1")
    Spot spot1;

    @OneToOne
    @JoinColumn(name="sId2")
    Spot spot2;

    @OneToOne
    @JoinColumn(name="sId3")
    Spot spot3;

    @OneToOne
    @JoinColumn(name="sId4")
    Spot spot4;

    @OneToOne
    @JoinColumn(name="sId5")
    Spot spot5;

    @OneToOne
    @JoinColumn(name="sId6")
    Spot spot6;

    int numOfDay;

}
