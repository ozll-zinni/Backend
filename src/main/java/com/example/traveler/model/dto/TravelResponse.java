package com.example.traveler.model.dto;

import com.example.traveler.model.entity.DayCourse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class TravelResponse {
    int tId;
    String title;
    String destination;
    Date start_date;
    Date end_date;
    Timestamp created_at;
    int time_status;
    int write_status;
    int note_status;
    List<DayCourse> courses;
}
