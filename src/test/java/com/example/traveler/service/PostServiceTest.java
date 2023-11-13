package com.example.traveler.service;

import com.example.traveler.config.BaseException;
import com.example.traveler.model.dto.PostRequest;
import com.example.traveler.model.dto.PostUpdateRequest;
import com.example.traveler.model.entity.Post;
import com.example.traveler.model.entity.Travel;
import com.example.traveler.model.entity.User;
import com.example.traveler.repository.PostRepository;
import com.example.traveler.repository.TravelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;
import static com.example.traveler.config.BaseResponseStatus.*;

public class PostServiceTest {

    @InjectMocks
    private PostService postService;

    @Mock
    private UserService userService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private TravelRepository travelRepository;

    @Mock
    private S3Uploader s3Uploader;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testCreatePost() throws BaseException {
        User user = new User();
        user.setId(1L);

        when(userService.getUserByToken(anyString())).thenReturn(user);

        Travel travel = new Travel();
        travel.setTId(1);
        travel.setUser(user);
        travel.setCode(0);

        when(travelRepository.findBytId(anyInt())).thenReturn(travel);

        PostRequest request = new PostRequest();

        List<String> imageFiles = new ArrayList<>();

        Post result = postService.createPost("accessToken", request, imageFiles);

        assertNotNull(result);

    }

    @Test
    void testGetPost() throws BaseException {
        Post post = new Post();
        post.setPId(1L);
        post.setTitle("Test Post");
        when(postRepository.findBypId(1L)).thenReturn(post);

        Post result = postService.getPost(1L);

        assertNotNull(result);
        assertEquals("Test Post", result.getTitle());

    }

    @Test
    void testUpdate() throws BaseException {
        String accessToken = "testAccessToken";
        PostUpdateRequest request = new PostUpdateRequest();
        request.setTitle("Updated Title");
        request.setOneLineReview("Updated Review");
        request.setLocation("Updated Location");
        request.setWhatrating(5);
        request.setHardrating(4);
        request.setTotalrating(4);
        request.setGoodPoints("Updated Good Points");
        request.setBadPoints("Updated Bad Points");

        User user = new User();
        user.setId(1L);
        when(userService.getUserByToken(accessToken)).thenReturn(user);

        Post post = new Post();
        post.setPId(1L);
        post.setTitle("Test Post");
        when(postRepository.findBypId(1L)).thenReturn(post);

        Post result = postService.update(accessToken, request, 1L);

        assertNotNull(result);
        assertEquals("Updated Title", result.getTitle());
    }

    @Test
    void testDeletePost() throws BaseException {
        // 사용자와 포스트의 소유자가 같은 경우
        String accessToken = "testAccessToken";
        Long pId = 1L;

        User user = new User();
        user.setId(1L);
        when(userService.getUserByToken(accessToken)).thenReturn(user);

        Post post = new Post();
        post.setPId(1L);
        post.setUser(user);
        when(postRepository.findBypId(pId)).thenReturn(post);

        Post deletedPost = postService.delete(accessToken, pId);

        assertNotNull(deletedPost);
        assertEquals(pId, deletedPost.getPId());

        // 사용자와 포스트의 소유자가 다른 경우
        when(userService.getUserByToken(accessToken)).thenReturn(new User()); // 다른 사용자로 설정

        assertThrows(BaseException.class, () -> {
            postService.delete(accessToken, pId);
        });
    }



}