package com.example.traveler.controller;


import com.example.traveler.config.BaseException;
import com.example.traveler.config.BaseResponse;
import com.example.traveler.model.dto.CommentRequest;
import com.example.traveler.model.dto.CommentResponse;
import com.example.traveler.model.dto.TravelRequest;
import com.example.traveler.model.dto.TravelResponse;
import com.example.traveler.model.entity.Comment;
import com.example.traveler.model.entity.Post;
import com.example.traveler.service.CommentService;
import com.example.traveler.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post")
public class CommentController {
    @Autowired
    private PostService postService;
    @Autowired
    private CommentService commentService;

    @PostMapping("/{pId}/comment")
    public BaseResponse<CommentResponse> saveComment(@RequestHeader("Authorization") String accessToken, @RequestBody CommentRequest commentRequest, @PathVariable("pId") long pId) {
        try {
            CommentResponse commentResponse = commentService.saveComment(accessToken, pId, commentRequest);
            return new BaseResponse<>(commentResponse);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @GetMapping("/{pId}/comment")
    public BaseResponse<List<CommentResponse>> getAllComment(@PathVariable("pId") long pId) {
        try {
            List<CommentResponse> commentResponses = commentService.getAllComment(pId);
            return new BaseResponse<>(commentResponses);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }


    @GetMapping("")
    public BaseResponse<List<Post>> getAllPost(@RequestParam(value = "keyword", required = false) String keyword) {
        List<Post> postResponse;
        if (keyword == null) {
            try {
                postResponse = postService.getAllPost();
            } catch (BaseException exception) {
                return new BaseResponse<>(exception.getStatus());
            }
        } else {
            if (keyword.startsWith("#")) {
                try {
                    postResponse = postService.searchByHashtag(keyword);
                } catch (BaseException exception) {
                    return new BaseResponse<>(exception.getStatus());
                }
            } else {
                try {
                    postResponse = postService.searchByTitle(keyword);
                } catch (BaseException exception) {
                    return new BaseResponse<>(exception.getStatus());
                }
            }
        }
        return new BaseResponse<>(postResponse);

    }

    @PostMapping("/{pId}/like")
    public BaseResponse<String> likePost(@RequestHeader("Authorization") String accessToken, @PathVariable("pId") long pId) {
        try {
            int result = postService.likePost(accessToken, pId);
            if (result == 1) {
                return new BaseResponse<>("좋아요 취소 성공!");
            }
            else {
                return new BaseResponse<>("좋아요 성공!");
            }
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @PostMapping("/{pId}/scrap")
    public BaseResponse<String> likeScrap(@RequestHeader("Authorization") String accessToken, @PathVariable("pId") long pId) {
        try {
            int result = postService.scrapPost(accessToken, pId);
            if (result == 1) {
                return new BaseResponse<>("찜 취소 성공!");
            }
            else {
                return new BaseResponse<>("찜 성공!");
            }
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
