package com.example.traveler.repository;

import com.example.traveler.model.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    public Post findBypId(Long pId);

    public List<Post> findAllByTitleContaining(String keyword);

    public List<Post> findAllByHashtagsContaining(String hashtag);

}
