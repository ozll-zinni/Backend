package com.example.traveler.controller;


import com.example.traveler.config.BaseException;
import com.example.traveler.config.BaseResponse;
import com.example.traveler.model.dto.*;
import com.example.traveler.model.entity.Comment;
import com.example.traveler.model.dto.CommentRequest;
import com.example.traveler.model.dto.CommentResponse;
import com.example.traveler.model.entity.Post;
import com.example.traveler.service.CommentService;
import com.example.traveler.service.PostService;
import com.example.traveler.service.S3Uploader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/post")
public class PostController {
    @Autowired
    private PostService postService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private S3Uploader s3Uploader;

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
        System.out.println("11111111");
        System.out.println(keyword);
        if (keyword == null) {
            try {
                System.out.println("22222222");
                postResponse = postService.getAllPost();
            } catch (BaseException exception) {
                return new BaseResponse<>(exception.getStatus());
            }
        } else {
            if (keyword.startsWith("#")) {
                try {
                    System.out.println("333333333");
                    postResponse = postService.searchByHashtag(keyword);
                } catch (BaseException exception) {
                    return new BaseResponse<>(exception.getStatus());
                }
            } else {
                try {
                    System.out.println("4444444444");
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

    @PostMapping("/create")
    public BaseResponse<Post> createPost(@RequestHeader("Authorization") String accessToken, @RequestPart("content") PostRequest postRequest, @RequestPart("imageFile") List<MultipartFile> imageFiles) {
        try{
            List<String> urls = s3Uploader.uploadImages(imageFiles);
            Post post = postService.createPost(accessToken, postRequest, urls);
            return new BaseResponse<>(post);
        }catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }

    }

    @GetMapping("{pId}/getOne")
    public BaseResponse<Post> getOnePost(@PathVariable("pId") long pId) {
        try {
            Post readpost = postService.getPost(pId);
            return new BaseResponse<>(readpost);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }

    }

    @PatchMapping("{pId}/update")
    public BaseResponse<Post> updatePost(@RequestHeader("Authorization") String accessToken, @RequestBody PostUpdateRequest postUpdateRequest, @PathVariable("pId") long pId) {
        try {
            Post updatepost = postService.update(accessToken, postUpdateRequest, pId);
            return new BaseResponse<>(updatepost);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @DeleteMapping("{pId}/delete")
    public BaseResponse<Post> deletePost(@RequestHeader("Authorization") String accessToken, @PathVariable("pId") long pId) {
        try {
            Post deletepost = postService.delete(accessToken, pId);
            return new BaseResponse<>(deletepost);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

}
