package com.example.traveler.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

@Data
@Entity
@NoArgsConstructor
public class DayCourse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int dcId;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="tId")
    Travel travel;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="rtId", nullable = true)
    RecommendTravel recommendTravel;

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

    //1과 2 사이 거리
    Double first;

    //2와 3 사이 거리
    Double second;

    //3과 4 사이 거리
    Double third;

    int numOfDay;

    public DayCourse(Travel travel, int numOfDay) {
        this.travel = travel;
        this.numOfDay = numOfDay;
    }
//    public DayCourse(RecommendTravel travel, int numOfDay) {
//        this.recommendTravel = travel;
//        this.numOfDay = numOfDay;
//    }
}
