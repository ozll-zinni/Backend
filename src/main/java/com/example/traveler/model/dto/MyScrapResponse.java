package com.example.traveler.model.dto;

import lombok.*;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class MyScrapResponse {

    private long pId;

    private long tId;

    private String image_url;

    private int like;

    private String p_title;

    private String location;

    private int comment;

    private Date start_date;

    private Date end_date;

    private String t_title;

}
