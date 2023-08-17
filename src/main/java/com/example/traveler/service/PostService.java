package com.example.traveler.service;

import com.example.traveler.config.BaseException;
import com.example.traveler.model.dto.PostRequest;
import com.example.traveler.model.dto.PostUpdateRequest;
import com.example.traveler.model.entity.*;
import com.example.traveler.repository.HeartRepository;
import com.example.traveler.repository.PostRepository;
import com.example.traveler.repository.TravelRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.traveler.config.BaseResponseStatus.*;

@Service
@RequiredArgsConstructor
@Slf4j
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

    public Post createPost(String accessToken, PostRequest request) throws BaseException {

        User user = userService.getUserByToken(accessToken);
        log.info("iser={}",user.getId());


        try {
            log.info("tId1 = {}", request.getTId());
            Travel travel = travelRepository.findBytIdAndState(request.getTId(), 1);
            log.info("hasg = {}", request.getHashtags());
            log.info("tite = {}", request.getTitle());
            log.info("gggg = {}", request.getGoodPoints());
            log.info("w = {}", request.getWhat());
            log.info("ww = {}", request.getWhatrating());



            int code = travel.getCode();
            log.info("code = {}", code);
            //


            if (code == 0) {
                Post post = Post.builder()
                        .user(user)
                        .title(request.getTitle())
                        .hashtags(request.getHashtags())
                        .location(request.getLocation())
                        .oneLineReview(request.getOneLineReview())
                        .what(request.getWhat())
                        .hard(request.getHard())
                        .withwho(request.getWithwho())
                        .whatrating(request.getWhatrating())
                        .hardrating(request.getHardrating())
                        .totalrating(request.getTotalrating())
                        .goodPoints(request.getGoodPoints())
                        .badPoints(request.getBadPoints())
                        .build();
                log.info("post = {}", post);


                return postRepository.save(post);

            }

            else {

                int withwho = travel.getWithWho();
                log.info("withwho = {}", withwho);

                Post post = Post.builder()
                        .user(user)
                        .title(request.getTitle())
                        .hashtags(request.getHashtags())
                        .oneLineReview(request.getOneLineReview())
                        .what((code/100)%10)
                        .hard((code/10)%10)
                        .withwho(withwho)
                        .whatrating(request.getWhatrating())
                        .hardrating(request.getHardrating())
                        .totalrating(request.getTotalrating())
                        .goodPoints(request.getGoodPoints())
                        .badPoints(request.getBadPoints())
                        .build();
                log.info("info = {}", post);

                return postRepository.save(post);
            }
        } catch (Exception e) {
            throw new BaseException(INVALID_AUTHORIZATION_CODE);
        }


    }

    public Post getPost(Long pId) throws BaseException{
        try {
            Post foundpost = postRepository.findBypId(pId);
            return foundpost;
        } catch(Exception e) {
            throw new BaseException(INVALID_JWT);
        }
    }

    public Post update(String accessToken, PostUpdateRequest request, Long pId) throws BaseException {

        User user = userService.getUserByToken(accessToken);
        Post foundPost = postRepository.findBypId(pId);

        if (user.getId() == foundPost.getUser().getId()){
            try{
                foundPost.setTitle(request.getTitle());
                foundPost.setHashtags(request.getHashtags());
                foundPost.setOneLineReview(request.getOneLineReview());
                foundPost.setLocation(request.getLocation());
                foundPost.setWhatrating(request.getWhatrating());
                foundPost.setHardrating(request.getHardrating());
                foundPost.setTotalrating(request.getTotalrating());
                foundPost.setGoodPoints(request.getGoodPoints());
                foundPost.setBadPoints(request.getBadPoints());
                return postRepository.save(foundPost);
            } catch (Exception e) {
                throw new BaseException(INVALID_JWT);
            }
        }

        else {
            throw new BaseException(INVALID_JWT);
        }
    }

    public Post delete(String accessToken, Long pId) throws BaseException {

        User user = userService.getUserByToken(accessToken);
        Post deletepost = postRepository.findBypId(pId);

        if (user.getId() == deletepost.getUser().getId()){
            try{
                postRepository.delete(deletepost);
                return deletepost;
            } catch (Exception e) {
                throw new BaseException(INVALID_JWT);
            }
        }

        else{
            throw new BaseException(INVALID_JWT);
        }

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
        post.setLikes(heartService.countHeart(post));
        postRepository.save(post);
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
        post.setScraps(scrapService.countScrap(post));
        postRepository.save(post);
        return result;
    }
}
