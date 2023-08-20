package com.example.traveler.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    //시작 날짜 기준으로 지난건지(1) 예정된건지(0)
    int timeStatus;

    //추천받은건지(1) 직접쓴건지(0)
    int writeStatus;

    //아무것도 없는지(0), 가계부만 있는지(1), 체크리스트만 있는지(2), 둘 다 있는지(3)
    int noteStatus;

    //삭제시(0) 삭제되지 않았을시(1)
    int state;

    // RecommendTravel을 통해 작성되면 그 코드 값을, 아니면 0
    int code = 0;

    int withWho;

    //사용자
    @ManyToOne
    @JoinColumn(name="id")
    User user;

    @OneToMany(mappedBy = "travel")
    List<DayCourse> courses = new ArrayList<>();

    @JsonIgnore
    @OneToOne
    @JoinColumn(name="pId")
    Post post;

    public Travel(String title, String destination, Date startDate, Date endDate, int timeStatus, int writeStatus, int noteStatus, int state, User user) {
        this.title = title;
        this.destination = destination;
        this.start_date = startDate;
        this.end_date = endDate;
        this.timeStatus = timeStatus;
        this.writeStatus = writeStatus;
        this.noteStatus = noteStatus;
        this.state = state;
        this.user = user;
    }
}
