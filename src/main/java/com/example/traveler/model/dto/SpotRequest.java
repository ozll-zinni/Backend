package com.example.traveler.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class SpotRequest {
    String title;
    double latitude;
    double longitude;
}
