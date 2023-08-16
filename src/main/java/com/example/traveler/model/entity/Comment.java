package com.example.traveler.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long coId;

    @ManyToOne
    @JoinColumn(name="pId")
    private Post post;

    private String content;

    @ManyToOne
    @JoinColumn(name="uId")
    private User user;


    public Comment(Post post, String content, User user) {
        this.post = post;
        this.content = content;
        this.user = user;
    }
}
