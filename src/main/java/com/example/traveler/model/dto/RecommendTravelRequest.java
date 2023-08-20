package com.example.traveler.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecommendTravelRequest {

    private String startDate;
    private String finishDate;
    private int cityId;
    private int hard;
    private int what;
    private int withwho;
    private int people;

}