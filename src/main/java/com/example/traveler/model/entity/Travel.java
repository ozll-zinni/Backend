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
public class Travel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int tId;

    String title;

    String destination;

    Date start_date;

    Date end_date;

    @CreationTimestamp
    Timestamp created_at;

    //시작 날짜 기준으로 지난건지 예정된건지
    int time_status;

    //추천받은건지 직접쓴건지
    int write_status;

    //아무것도 없는지, 가계부만 있는지, 체크리스트만 있는지, 둘 다 있는지
    int note_status;

    int state;

    //사용자
    @ManyToOne
    @JoinColumn(name="uId")
    User user;

    @OneToMany(mappedBy = "travel")
    List<DayCourse> courses = new ArrayList<>();

    public Travel(String title, String destination, Date startDate, Date endDate, int timeStatus, int writeStatus, int noteStatus, int state, User user) {
        this.title = title;
        this.destination = destination;
        this.start_date = startDate;
        this.end_date = endDate;
        this.time_status = timeStatus;
        this.write_status = writeStatus;
        this.note_status = noteStatus;
        this.state = state;
        this.user = user;
    }
}
