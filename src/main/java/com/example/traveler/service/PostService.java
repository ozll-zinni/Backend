package com.example.traveler.service;

import com.example.traveler.config.BaseException;
import com.example.traveler.model.entity.*;
import com.example.traveler.repository.HeartRepository;
import com.example.traveler.repository.PostRepository;
import com.example.traveler.repository.TravelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.traveler.config.BaseResponseStatus.*;

@Service
@RequiredArgsConstructor
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private TravelRepository travelRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private HeartService heartService;

    @Autowired
    private ScrapService scrapService;

    public String createPost(String title, List<String> hashtags, String oneLineReview, String location, int what, int hard, int with, double whatrating, double hardrating, double totalrating, String goodPoints, String badPoints, int likes, int tId, int state) {

        Travel travel = travelRepository.findBytIdAndState(tId, state);

        int code = travel.getCode();

        if (code == 0) {
            Post post = Post.builder()
                    .title(title)
                    .hashtags(hashtags)
                    .oneLineReview(oneLineReview)
                    .location(location)
                    .what(what)
                    .hard(hard)
                    .with(with)
                    .whatrating(whatrating)
                    .hardrating(hardrating)
                    .totalrating(totalrating)
                    .goodPoints(goodPoints)
                    .badPoints(badPoints)
                    .likes(likes)
                    .build();

            postRepository.save(post);

        }

        else {
            Post post = Post.builder()
                    .title(title)
                    .hashtags(hashtags)
                    .oneLineReview(oneLineReview)
                    .location(location)
                    .what((code/100)%10)
                    .hard((code/10)%10)
                    .with(with)
                    .whatrating(whatrating)
                    .hardrating(hardrating)
                    .totalrating(totalrating)
                    .goodPoints(goodPoints)
                    .badPoints(badPoints)
                    .likes(likes)
                    .build();

            postRepository.save(post);
        }

        return title;

    }

    public Post getPost(Long pId) {
        return postRepository.findBypId(pId);
    }

    public String update(Long pId, String title, List<String> hashtags, String oneLineReview, String location, int what, int hard, int with, double whatrating, double hardrating, double totalrating, String goodPoints, String badPoints) {

        Post foundPost = postRepository.findBypId(pId);
        foundPost.setTitle(title);
        foundPost.setHashtags(hashtags);
        foundPost.setOneLineReview(oneLineReview);
        foundPost.setLocation(location);
        foundPost.setWhatrating(whatrating);
        foundPost.setHardrating(hardrating);
        foundPost.setTotalrating(totalrating);
        foundPost.setGoodPoints(goodPoints);
        foundPost.setBadPoints(badPoints);
        postRepository.save(foundPost);
        return foundPost.getTitle();
    }

    public Post delete(Long pId) {

        Post deletepost = postRepository.findBypId(pId);
        postRepository.delete(deletepost);
        return deletepost;
    }

    public List<Post> searchByTitle(String keyword) throws BaseException{
        List<Post> posts;
        try {
            posts = postRepository.findAllByTitleContaining(keyword);
        } catch (Exception exception) {
            throw new BaseException(POST_SEARCH_FAIL);
        }
        return posts;
    }

    public List<Post> searchByHashtag(String keyword) throws BaseException{
        List<Post> posts;
        try {
            posts = postRepository.findAllByHashtagsContaining(keyword);
        } catch (Exception exception) {
            throw new BaseException(POST_SEARCH_FAIL);
        }
        return posts;
    }

    public List<Post> getAllPost() throws BaseException {
        List<Post> posts;
        try {
            posts = postRepository.findAll();
        } catch (Exception exception) {
            throw new BaseException(POST_GET_FAIL);
        }
        return posts;
    }

    public int likePost(String accessToken, long pId) throws BaseException {
        int result = -1;
        Post post;
        User user = userService.getUserByToken(accessToken);
        if (user == null) {
            throw new BaseException(INVALID_JWT);
        }
        try {
            post = getPost(pId);
        } catch (Exception e) {
            throw new BaseException(POST_IS_EMPTY);
        }

        Heart heart = heartService.getHeart(user, post);
        if (heart != null) {
            result = heartService.deleteHeart(heart);
        } else {
            Heart saveHeart = heartService.saveHeart(user, post);
            if (saveHeart != null) {
                result = 1000;
            }
        }
        return result;

    }

    public int scrapPost(String accessToken, long pId) throws BaseException {
        int result = -1;
        Post post;
        User user = userService.getUserByToken(accessToken);
        if (user == null) {
            throw new BaseException(INVALID_JWT);
        }
        try {
            post = getPost(pId);
        } catch (Exception e) {
            throw new BaseException(POST_IS_EMPTY);
        }

        Scrap scrap = scrapService.getScrap(user, post);
        if (scrap != null) {
            result = scrapService.deleteScrap(scrap);
        } else {
            Scrap saveScrap = scrapService.saveScrap(user, post);
            if (saveScrap != null) {
                result = 1000;
            }
        }
        return result;

    }
}
