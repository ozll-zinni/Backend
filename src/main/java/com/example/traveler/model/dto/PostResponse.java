package com.example.traveler.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PostResponse {

    int pId;

    int tId;

    int uId;

    private String title;

    private List<String> hashtags;

    private String oneLineReview;

    private String location;

    private int what;

    private int hard;

    private int withwho;

    private double whatrating;

    private double hardrating;

    private double totalrating;

    private String goodPoints;

    private String badPoints;

}
