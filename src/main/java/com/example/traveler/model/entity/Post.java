package com.example.traveler.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Post {

    public Post(String title, List<String> hashtags, String oneLineReview, String location, int what, int hard, int withwho, double whatrating, double hardrating, double totalrating, String goodPoints, String badPoints, int likes) {
        this.title = title;
        this.hashtags = hashtags;
        this.oneLineReview = oneLineReview;
        this.location = location;
        this.what = what;
        this.hard = hard;
        this.withwho = withwho;
        this.whatrating = whatrating;
        this.hardrating = hardrating;
        this.totalrating = totalrating;
        this.goodPoints = goodPoints;
        this.badPoints = badPoints;
        this.likes = likes;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pId;

    private String title;

    @ElementCollection
    @CollectionTable(joinColumns = @JoinColumn(referencedColumnName = "pId"))
    private List<String> hashtags;

    private String oneLineReview;

    @Column(nullable = false)
    private String location;

    private int what;

    private int hard;

    private int withwho;

    private double whatrating;

    private double hardrating;

    private double totalrating;

    private String goodPoints;

    private String badPoints;

    private int likes = 0;

    private int scraps = 0;

    @CreatedDate
    private LocalDateTime createdAt;

    @JsonIgnore
    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
    private List<Comment> comments = new ArrayList<>();

    @OneToOne(mappedBy = "post")
    private Travel travel;

    @ManyToOne
    @JoinColumn(name="id")
    private User user;

}
