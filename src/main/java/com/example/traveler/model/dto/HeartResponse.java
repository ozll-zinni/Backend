package com.example.traveler.model.dto;

import com.example.traveler.model.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class HeartResponse {
    long hId;
    PostResponse postResponse;
    long uId;
}
