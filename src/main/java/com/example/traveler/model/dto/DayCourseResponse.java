package com.example.traveler.model.dto;

import com.example.traveler.model.entity.Spot;
import com.example.traveler.model.entity.Travel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Date;

@AllArgsConstructor
@Getter
@Setter
public class DayCourseResponse {
    int dcId;
    //Travel travel;
    int tId;
    Spot spot1;
    Spot spot2;
    Spot spot3;
    Spot spot4;
    Double first;
    Double second;
    Double third;
    int numOfDay;
}
