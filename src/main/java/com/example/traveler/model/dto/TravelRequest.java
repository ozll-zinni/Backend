package com.example.traveler.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
public class TravelRequest {
    String title;
    String destination;
    Date start_date;
    Date end_date;
    int write_status;
}
