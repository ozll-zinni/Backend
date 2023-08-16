package com.example.traveler.repository;

import com.example.traveler.model.entity.Comment;
import com.example.traveler.model.entity.Heart;
import com.example.traveler.model.entity.Post;
import com.example.traveler.model.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface HeartRepository extends CrudRepository<Heart, Long> {

    public Heart findByUserAndPost(User user, Post post);
    public List<Heart> findAllByPost(Post post);

    public List<Heart> findAllByUser(User user);
    //public List<Travel> findAllByUser()

}
