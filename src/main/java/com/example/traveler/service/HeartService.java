package com.example.traveler.service;

import com.example.traveler.config.BaseException;
import com.example.traveler.model.dto.CommentRequest;
import com.example.traveler.model.dto.CommentResponse;
import com.example.traveler.model.dto.HeartResponse;
import com.example.traveler.model.entity.Comment;
import com.example.traveler.model.entity.Heart;
import com.example.traveler.model.entity.Post;
import com.example.traveler.model.entity.User;
import com.example.traveler.repository.CommentRepository;
import com.example.traveler.repository.HeartRepository;
import com.example.traveler.repository.PostRepository;
import com.fasterxml.jackson.databind.ser.Serializers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static com.example.traveler.config.BaseResponseStatus.*;

@Service
public class HeartService {
    @Autowired
    private HeartRepository heartRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private PostRepository postRepository;

    public Heart saveHeart(User user, Post post) throws BaseException {

        Heart saveHeart = null;

        try {
            Heart newHeart = new Heart(user, post);
            saveHeart = heartRepository.save(newHeart);
        } catch (Exception e) {
            throw new BaseException(POST_LIKE_FAIL);
        }
        return saveHeart;
    }
    public Heart getHeart(User user, Post post) throws BaseException {
        try {
            return heartRepository.findByUserAndPost(user, post);
        } catch (Exception e) {
            throw new BaseException(POST_LIKE_GET_FAIL);
        }
    }

    public int deleteHeart(Heart heart) throws BaseException {
        try {
            heartRepository.delete(heart);
        } catch (Exception e) {
            throw new BaseException(POST_LIKE_CANCEL_FAIL);
        }
        return 1;
    }

    public int countHeart(Post post) throws BaseException {
        try {
            return (int) heartRepository.countByPost(post);
        } catch (Exception e) {
            throw new BaseException(POST_LIKE_COUNT_FAIL);
        }
    }

    public List<HeartResponse> allMyHeart(String accessToken) throws BaseException {
        User user = userService.getUserByToken(accessToken);
        if (user == null) {
            throw new BaseException(INVALID_JWT);
        }
        try {
            List<Heart> hearts = heartRepository.findAllByUser(user);
            ArrayList<HeartResponse> responses = new ArrayList<>();
            for (Heart heart : hearts) {
                Post foundPost = postRepository.findBypId(heart.getPost().getPId());
                HeartResponse response = new HeartResponse(heart.getHId(), foundPost, heart.getUser().getId());
                responses.add(response);
            }
            return responses;
        } catch (Exception e) {
            throw new BaseException(MY_LIKE_GET_FAIL);
        }
    }


}
