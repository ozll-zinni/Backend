package com.example.traveler.model.dto;

import com.example.traveler.model.entity.DayCourse;
import com.example.traveler.model.entity.Travel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class CommentResponse {
    long coId;
    String content;
    long pId;
    long uId;
}
