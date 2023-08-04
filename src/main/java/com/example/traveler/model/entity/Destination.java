package com.example.traveler.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class Destination {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int dId;

    String city;

    String country;
}
