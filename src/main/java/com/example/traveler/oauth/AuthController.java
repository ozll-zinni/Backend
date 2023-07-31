package com.example.traveler.oauth;

<<<<<<< HEAD
import com.example.traveler.kakao.KakaoLoginParams;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
=======
import com.example.traveler.config.BaseException;
import com.example.traveler.config.BaseResponse;
import com.example.traveler.kakao.KakaoLoginParams;
import lombok.RequiredArgsConstructor;
>>>>>>> 0f903cca70b820824c4409422dd89978138f38d2
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final OAuthLoginService oAuthLoginService;

    @PostMapping("/kakao")
<<<<<<< HEAD
    public ResponseEntity<AuthTokens> loginKakao(@RequestBody KakaoLoginParams params) {
        return ResponseEntity.ok(oAuthLoginService.login(params));
    }

=======
    public BaseResponse<String> loginKakao(@RequestBody KakaoLoginParams params) {
        try {
            AuthTokens authTokens = oAuthLoginService.login(params);
            String result = "로그인 되었습니다";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {

            return new BaseResponse<>((exception.getStatus()));

        }
    }


>>>>>>> 0f903cca70b820824c4409422dd89978138f38d2
}
