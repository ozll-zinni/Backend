package com.example.traveler.model.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "kakao_id")
    private Long kakao;

    @Column(name = "profile_image_url ")
    private String profile_image_url;

    @Builder
    public User(String name, String email, String nickname, Long kakao, String profile_image_url) {
        this.name = name;
        this.email = email;
        this.nickname = nickname;
        this.kakao = kakao;
        this.profile_image_url = profile_image_url;
    }

}
