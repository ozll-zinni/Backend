package com.example.traveler.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
public class RecommendTravel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int rtId;

    //1박 2일이면 2 저장
    int period;

    String title;

    //무엇을
    //1. 경치관람, 2. 먹방, 3. 액티비티, 4. 체험, 5. 카페
    int what;

    //강도
    //1. 빡빡하게, 2. 보통, 3. 느긋하게
    int intensity;

    int people;

    //1. 친구와, 2. 가족과, 3. 연인과, 4. 혼자서
    int with;
}
