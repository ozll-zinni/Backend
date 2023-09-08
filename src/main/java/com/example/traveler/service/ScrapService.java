package com.example.traveler.service;

import com.example.traveler.config.BaseException;
import com.example.traveler.model.dto.CommentResponse;
import com.example.traveler.model.dto.HeartResponse;
import com.example.traveler.model.dto.PostResponse;
import com.example.traveler.model.dto.ScrapResponse;
import com.example.traveler.model.entity.Heart;
import com.example.traveler.model.entity.Post;
import com.example.traveler.model.entity.Scrap;
import com.example.traveler.model.entity.User;
import com.example.traveler.repository.HeartRepository;
import com.example.traveler.repository.PostRepository;
import com.example.traveler.repository.ScrapRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.example.traveler.config.BaseResponseStatus.*;

@Service
public class ScrapService {
    @Autowired
    private ScrapRepository scrapRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PostRepository postRepository;

    @Transactional
    public Scrap saveScrap(User user, Post post) throws BaseException {

        Scrap saveScrap = null;

        try {
            Scrap newScrap = new Scrap(user, post);
            saveScrap = scrapRepository.save(newScrap);
        } catch (Exception e) {
            throw new BaseException(POST_SCRAP_FAIL);
        }
        return saveScrap;
    }
    public Scrap getScrap(User user, Post post) throws BaseException {
        try {
            return scrapRepository.findByUserAndPost(user, post);
        } catch (Exception e) {
            throw new BaseException(POST_SCRAP_GET_FAIL);
        }
    }

    @Transactional
    public int deleteScrap(Scrap scrap) throws BaseException {
        try {
            scrapRepository.delete(scrap);
        } catch (Exception e) {
            throw new BaseException(POST_SCRAP_CANCEL_FAIL);
        }
        return 1;
    }

    public int countScrap(Post post) throws BaseException {
        try {
            return (int) scrapRepository.countByPost(post);
        } catch (Exception e) {
            throw new BaseException(POST_SCRAP_COUNT_FAIL);
        }
    }

    public List<ScrapResponse> allMyScrap(String accessToken) throws BaseException {
        User user = userService.getUserByToken(accessToken);
        if (user == null) {
            throw new BaseException(INVALID_JWT);
        }
        try {
            List<Scrap> scraps =  scrapRepository.findAllByUser(user);
            ArrayList<ScrapResponse> responses = new ArrayList<>();
            for (Scrap scrap : scraps) {
                Post foundPost = postRepository.findBypId(scrap.getPost().getPId());
                PostResponse postResponse;
                if (foundPost.getImage_url().isEmpty()) {
                    postResponse = new PostResponse(foundPost.getPId(), foundPost.getUser().getId(), foundPost.getTitle(), foundPost.getOneLineReview(), null);
                } else {
                    postResponse = new PostResponse(foundPost.getPId(), foundPost.getUser().getId(), foundPost.getTitle(), foundPost.getOneLineReview(), foundPost.getImage_url().get(0));
                }
                ScrapResponse response = new ScrapResponse(scrap.getScId(), postResponse, scrap.getUser().getId());
                responses.add(response);
            }
            return responses;
        } catch (Exception e) {
            throw new BaseException(MY_SCRAP_GET_FAIL);
        }
    }

}
