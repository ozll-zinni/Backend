package com.example.traveler.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class RecommendSpotResponse {
    private String name;
    private List<Double> location;
    private String rating;
}
