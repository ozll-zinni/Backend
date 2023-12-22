package com.example.traveler.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PostRequest {

    int tId;

    String title;

    List<String> hashtags;

    String oneLineReview;

    String location;

    int what;

    int hard;

    int withwho;

    double whatrating;

    double hardrating;

    double totalrating;

    String goodPoints;

    String badPoints;

    int noteStatus;

}
