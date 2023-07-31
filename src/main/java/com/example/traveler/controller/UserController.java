package com.example.traveler.controller;

import com.example.traveler.config.BaseException;
import com.example.traveler.config.BaseResponse;
import com.example.traveler.jwt.JwtTokenProvider;
import com.example.traveler.model.dto.UpdateNicknameDTO;
import com.example.traveler.model.entity.User;
import com.example.traveler.repository.UserRepository;
import com.example.traveler.service.S3Uploader;
import com.example.traveler.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static com.example.traveler.config.BaseResponseStatus.INVALID_JWT;
import static com.example.traveler.config.BaseResponseStatus.PATCH_NULL_FILE;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    private final JwtTokenProvider jwtTokenProvider;

    private final UserRepository userRepository;

    private final S3Uploader s3Uploader;

    @Autowired
    public UserController(JwtTokenProvider jwtTokenProvider, UserService userService, UserRepository userRepository, S3Uploader s3Uploader) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
        this.s3Uploader = s3Uploader;
    }

    @PatchMapping("/nickname")
    public BaseResponse<User> updateNickname(@RequestHeader("Authorization") String accessToken,
                                               @RequestBody UpdateNicknameDTO updateNicknameDTO) {
        try {
            Long id = jwtTokenProvider.extractId(accessToken); // User 아이디 추출
            User updatedUser = userService.updateNicknameById(id, updateNicknameDTO);

            return new BaseResponse<>(updatedUser);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));        }
    }

    @PatchMapping("/profile_image")
    public BaseResponse<String> uploadProfileImage(@RequestHeader("Authorization") String accessToken,
                                                     @RequestParam("imageFile") MultipartFile imageFile) {
        try {
            Long userId = jwtTokenProvider.extractId(accessToken); // User 아이디 추출

            // 이미지 업로드 서비스를 통해 이미지를 S3에 업로드하고 경로를 받아옴
            String imageUrl = s3Uploader.uploadImage(imageFile);

            if (imageUrl != null) {
                // User 엔티티를 찾아서 이미지 경로를 저장
                Optional<User> optionalUser = userRepository.findById(userId);
                if (optionalUser.isPresent()) {
                    User user = optionalUser.get();
                    user.setProfile_image_url(imageUrl);
                    userRepository.save(user);

                    String result = "프로필 이미지가 업로드 되었습니다.";
                    return new BaseResponse<>(result);
                } else {
                    throw new BaseException(INVALID_JWT);                 }
            } else {
                throw new BaseException(PATCH_NULL_FILE);
            }
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @DeleteMapping("/profile")
    public BaseResponse<String> deleteUserProfile(@RequestHeader("Authorization") String accessToken) {
        try{
        Long userId = jwtTokenProvider.extractId(accessToken);
        userService.deleteUser(userId);
        String result = "삭제되었습니다.";

        return new BaseResponse<>(result);

    } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<User> getUserProfile(@RequestHeader("Authorization") String accessToken) {
        Long Id = jwtTokenProvider.extractId(accessToken);

        User user = userService.getUser(Id);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}

