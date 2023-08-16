package com.example.traveler.service;

import com.example.traveler.model.entity.Post;
import com.example.traveler.model.entity.Travel;
import com.example.traveler.repository.PostRepository;
import com.example.traveler.repository.TravelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private TravelRepository travelRepository;

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


}
