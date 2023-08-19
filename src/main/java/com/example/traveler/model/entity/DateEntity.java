package com.example.traveler.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
public class DateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int dateId;

    @Temporal(TemporalType.DATE)
    Date date;

    @ManyToOne
    @JoinColumn(name = "tId")
    Travel travel;

    @ManyToOne
    @JoinColumn(name = "dcId")
    DayCourse dayCourse;

    @ManyToOne
    @JoinColumn(name = "accountId")
    private AccountBook accountBook;

    public DateEntity(Date date, Travel travel, DayCourse dayCourse) {
        this.date = date;
        this.travel = travel;
        this.dayCourse = dayCourse;
    }
}
