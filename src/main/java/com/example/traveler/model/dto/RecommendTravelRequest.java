package com.example.traveler.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class RecommendTravelRequest {

    private String startDate;
    private String finishDate;
    private int countryId;
    private int hard;
    private int what;
    private int with;
    private int people;

}