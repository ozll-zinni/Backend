package com.example.traveler.oauth;

import com.example.traveler.config.BaseException;
import com.example.traveler.config.BaseResponse;
import com.example.traveler.kakao.KakaoLoginParams;
import lombok.RequiredArgsConstructor;
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
    public BaseResponse<String> loginKakao(@RequestBody KakaoLoginParams params) {
        try {
            AuthTokens authTokens = oAuthLoginService.login(params);
            String result = "로그인 되었습니다";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {

            return new BaseResponse<>((exception.getStatus()));

        }
    }


}
