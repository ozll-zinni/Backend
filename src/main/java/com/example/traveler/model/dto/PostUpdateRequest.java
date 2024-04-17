package com.example.traveler.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class PostUpdateRequest {
    String title;
    List<String> hashtags;
    String oneLineReview;
    String location;
    double whatrating;
    double hardrating;
    double totalrating;
    String goodPoints;
    String badPoints;
    int noteStatus;
}
