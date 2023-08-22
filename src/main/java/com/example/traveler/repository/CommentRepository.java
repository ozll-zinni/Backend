package com.example.traveler.repository;

import com.example.traveler.model.entity.Comment;
import com.example.traveler.model.entity.Post;
import com.example.traveler.model.entity.Travel;
import com.example.traveler.model.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CommentRepository extends CrudRepository<Comment, Long> {

    public Comment findByCoId(long id);
    public List<Comment> findAllByPostOrderByCoId(Post post);

    public List<Comment> findAllByUserOrderByCoId(User user);
    //public List<Travel> findAllByUser()
}
