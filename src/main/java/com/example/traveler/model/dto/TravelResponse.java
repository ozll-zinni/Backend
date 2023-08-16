package com.example.traveler.model.dto;

import com.example.traveler.model.entity.DayCourse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TravelResponse {
    int tId;
    String title;
    String destination;
    String start_date;
    String end_date;
    String created_at;
    int time_status;
    int writeStatus;
    int noteStatus;
    List<DayCourse> courses;
}
