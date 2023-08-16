package com.example.traveler.repository;

import com.example.traveler.model.entity.Heart;
import com.example.traveler.model.entity.Post;
import com.example.traveler.model.entity.Scrap;
import com.example.traveler.model.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ScrapRepository extends CrudRepository<Scrap, Long> {

    public Scrap findByUserAndPost(User user, Post post);
    public List<Scrap> findAllByPost(Post post);

    public List<Scrap> findAllByUser(User user);
    //public List<Travel> findAllByUser()
    public long countByPost(Post post);
}
