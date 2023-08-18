package com.example.traveler.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Data
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long coId;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="pId")
    private Post post;

    private String content;

    @ManyToOne
    @JoinColumn(name="id")
    private User user;

    @CreationTimestamp
    Timestamp created_at;


    public Comment(Post post, String content, User user) {
        this.post = post;
        this.content = content;
        this.user = user;
    }
}
