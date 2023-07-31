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
    public ResponseEntity<String> uploadProfileImage(@RequestHeader("Authorization") String accessToken,
                                                     @RequestParam("imageFile") MultipartFile imageFile) {
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

                return ResponseEntity.ok("프로필 이미지가 업로드되었습니다.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 사용자를 찾을 수 없습니다.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("이미지 업로드에 실패했습니다.");
        }
    }

    @DeleteMapping("/profile")
    public ResponseEntity<String> deleteUserProfile(@RequestHeader("Authorization") String accessToken) {
        Long userId = jwtTokenProvider.extractId(accessToken);

        userService.deleteUser(userId);
        return ResponseEntity.ok("회원 탈퇴가 성공적으로 처리되었습니다.");
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

