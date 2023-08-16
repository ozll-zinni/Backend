package com.example.traveler.controller;


import com.example.traveler.config.BaseException;
import com.example.traveler.config.BaseResponse;
import com.example.traveler.model.dto.CommentRequest;
import com.example.traveler.model.dto.CommentResponse;
import com.example.traveler.model.dto.TravelRequest;
import com.example.traveler.model.dto.TravelResponse;
import com.example.traveler.model.entity.Comment;
import com.example.traveler.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post")
public class CommentController {
//    @Autowired
//    private PostService postService;
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
            List<CommentResponse> commentResponses = commentService.getAllComment();
            return new BaseResponse<>(commentResponses);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
