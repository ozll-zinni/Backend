package com.example.traveler.controller;

import com.example.traveler.config.BaseException;
import com.example.traveler.config.BaseResponse;
import com.example.traveler.jwt.JwtTokenProvider;
import com.example.traveler.model.dto.*;
import com.example.traveler.model.entity.Heart;
import com.example.traveler.model.entity.Scrap;
import com.example.traveler.model.entity.User;
import com.example.traveler.repository.UserRepository;
import com.example.traveler.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = {"http://localhost:3000", "https://traveler-smoky.vercel.app"})
public class UserController {

    private final UserService userService;

    private final JwtTokenProvider jwtTokenProvider;

    private final UserRepository userRepository;

    private final S3Uploader s3Uploader;

    private final TravelService travelService;

    @Autowired
    private HeartService heartService;

    @Autowired
    private ScrapService scrapService;

    @Autowired
    private CommentService commentService;

    @Autowired
    public UserController(JwtTokenProvider jwtTokenProvider, UserService userService, UserRepository userRepository, S3Uploader s3Uploader, TravelService travelService) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
        this.s3Uploader = s3Uploader;
        this.travelService = travelService;
    }

    @PatchMapping("/nickname")
    public BaseResponse<User> updateNickname(@RequestHeader("Authorization") String accessToken,
                                               @RequestBody UpdateNicknameDTO updateNicknameDTO) {
        try {
            Long Id = jwtTokenProvider.extractId(accessToken); // User 아이디 추출
            User updatedUser = userService.updateNicknameById(Id, updateNicknameDTO);

            return new BaseResponse<>(updatedUser);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));        }
    }

    @PatchMapping("/profile_image")
    public BaseResponse<String> uploadProfileImage(@RequestHeader("Authorization") String accessToken,
                                                   @RequestParam("imageFile") MultipartFile imageFile) {
        try {
            // 유저 정보를 통해 해당 사용자의 ID를 가져옴
            Long Id = jwtTokenProvider.extractId(accessToken);

            // 이미지 업로드 서비스를 통해 이미지를 S3에 업로드하고 경로를 받아옴
            String imageUrl = s3Uploader.uploadImage(imageFile);

            // 사용자 정보 업데이트
            Optional<User> optionalUser = userRepository.findById(Id);

            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                user.setProfile_image_url(imageUrl);
                userRepository.save(user);
            }

            return new BaseResponse<>(imageUrl);

        } catch (BaseException exception) {
            // 예외가 발생한 경우에 대한 처리
            return new BaseResponse<>(exception.getStatus());
        }
    }


    @DeleteMapping("/profile")
    public BaseResponse<String> deleteUserProfile(@RequestHeader("Authorization") String accessToken) {
        try{
        Long Id = jwtTokenProvider.extractId(accessToken);
        userService.deleteUser(Id);
        String result = "삭제되었습니다.";

        return new BaseResponse<>(result);

    } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @GetMapping("/profile")
    public BaseResponse<User> getUserProfile(@RequestHeader("Authorization") String accessToken) {
        Long Id = jwtTokenProvider.extractId(accessToken);
        User user = userService.getUser(Id);
        return new BaseResponse<>(user);
    }


    @GetMapping("/my_travels")
    public BaseResponse<List<TravelResponse>> getAllMyTravel(@RequestHeader("Authorization") String accessToken) {
            // 유저 정보를 통해 해당 사용자의 여행 정보를 가져옴
            Long Id = jwtTokenProvider.extractId(accessToken);
            User user = userService.getUser(Id); // User 객체를 생성하거나 UserRepository를 이용하여 사용자 정보를 가져올 수 있습니다.
            List<TravelResponse> allMyTravelResponse = travelService.getAllMyTravel(user);
            return new BaseResponse<>(allMyTravelResponse);

    }

    @GetMapping("/my_past_travels")
    public BaseResponse<List<TravelResponse>> allMyPastTravel(@RequestHeader("Authorization") String accessToken) {
        // 유저 정보를 통해 해당 사용자의 여행 정보를 가져옴
        Long Id = jwtTokenProvider.extractId(accessToken);
        User user = userService.getUser(Id); // User 객체를 생성하거나 UserRepository를 이용하여 사용자 정보를 가져올 수 있습니다.
        List<TravelResponse> allMyTravelResponse = travelService.allMyPastTravel(user);
        return new BaseResponse<>(allMyTravelResponse);

    }

    @GetMapping("/myLike")
    public BaseResponse<List<HeartResponse>> allMyHeart(@RequestHeader("Authorization") String accessToken) {
        try {
            return new BaseResponse<>(heartService.allMyHeart(accessToken));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @GetMapping("/myScrap")
    public BaseResponse<List<ScrapResponse>> allMyScrap(@RequestHeader("Authorization") String accessToken) {
        try {
            return new BaseResponse<>(scrapService.allMyScrap(accessToken));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @GetMapping("/myComment")
    public BaseResponse<List<CommentResponse>> allMyComment(@RequestHeader("Authorization") String accessToken) {
        try {
            return new BaseResponse<>(commentService.getMyComment(accessToken));
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
}

