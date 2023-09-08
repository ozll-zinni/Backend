package com.example.traveler.service;

import com.example.traveler.config.BaseException;
import com.example.traveler.model.dto.CommentRequest;
import com.example.traveler.model.dto.CommentResponse;
import com.example.traveler.model.dto.TravelRequest;
import com.example.traveler.model.dto.TravelResponse;
import com.example.traveler.model.entity.Comment;
import com.example.traveler.model.entity.Post;
import com.example.traveler.model.entity.Travel;
import com.example.traveler.model.entity.User;
import com.example.traveler.repository.CommentRepository;
import com.example.traveler.repository.TravelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static com.example.traveler.config.BaseResponseStatus.*;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private PostService postService;

    @Transactional
    public CommentResponse saveComment(String accessToken, long pId, CommentRequest comment) throws BaseException {
        User user = userService.getUserByToken(accessToken);
        Post post;
        if (user == null) {
            throw new BaseException(INVALID_JWT);
        }
        Comment saveComment = null;
        try {
            post = postService.getPost(pId);
        } catch (Exception e) {
            throw new BaseException(POST_IS_EMPTY);
        }
        try {
            Comment newComment = new Comment(post, comment.getContent(), user);
            saveComment = commentRepository.save(newComment);
        } catch (Exception e) {
            throw new BaseException(SAVE_TRAVEL_FAIL);
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        CommentResponse commentResponse = new CommentResponse(saveComment.getCoId(), saveComment.getContent(), saveComment.getPost().getPId(), saveComment.getUser().getId(), formatter.format(saveComment.getCreated_at()));
        return commentResponse;
    }



    public List<CommentResponse> getAllComment(long pId) throws BaseException {
        Post post = null;
        try {
            post = postService.getPost(pId);
        } catch (Exception e) {
            throw new BaseException(POST_IS_EMPTY);
        }
        List<Comment> allComment = commentRepository.findAllByPostOrderByCoId(post);
        ArrayList<CommentResponse> allCommentResponse = new ArrayList<>();
        for (Comment comment : allComment) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            CommentResponse commentResponse = new CommentResponse(comment.getCoId(), comment.getContent(), comment.getPost().getPId(), comment.getUser().getId(), formatter.format(comment.getCreated_at()));
            allCommentResponse.add(commentResponse);
        }
        return allCommentResponse;
    }





    public List<CommentResponse> getMyComment(String accessToken) throws BaseException{
        User user = userService.getUserByToken(accessToken);
        if (user == null) {
            throw new BaseException(INVALID_JWT);
        }
        try {
            List<Comment> allMyComment = commentRepository.findAllByUserOrderByCoId(user);
            ArrayList<CommentResponse> allMyCommentResponse = new ArrayList<>();
            for (Comment comment : allMyComment) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                CommentResponse commentResponse = new CommentResponse(comment.getCoId(), comment.getContent(), comment.getPost().getPId(), comment.getUser().getId(), formatter.format(comment.getCreated_at()));
                allMyCommentResponse.add(commentResponse);
            }
            return allMyCommentResponse;
        } catch (Exception e) {
            throw new BaseException(MY_COMMENT_GET_FAIL);
        }
    }


}
