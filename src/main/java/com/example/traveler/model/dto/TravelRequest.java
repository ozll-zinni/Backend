package com.example.traveler.model.dto;

import com.example.traveler.model.entity.DayCourse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TravelRequest {
    String title;
    String destination;
    Date start_date;
    Date end_date;
    int writeStatus;
}
