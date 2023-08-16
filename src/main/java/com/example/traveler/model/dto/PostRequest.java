package com.example.traveler.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PostRequest {

    private String title;

    private List<String> hashtags;

    private String oneLineReview;

    private int what;

    private int hard;

    private int with;

    private double whatrating;

    private double hardrating;

    private double totalrating;

    private String goodPoints;

    private String badPoints;


}
