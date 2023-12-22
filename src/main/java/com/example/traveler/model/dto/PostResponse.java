package com.example.traveler.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PostResponse {

    long pId;

    long uId;


    String title;

    String oneLineReview;

    String url;

    int noteStatus;

}
