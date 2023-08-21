package com.example.traveler.oauth;

import com.example.traveler.kakao.KakaoLoginParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final OAuthLoginService oAuthLoginService;

    @PostMapping("/kakao")
    public ResponseEntity<AuthTokens> loginKakao(@RequestBody KakaoLoginParams params) {
        System.out.println(params);
        log.info("{}",params);
        return ResponseEntity.ok(oAuthLoginService.login(params));
    }

    @GetMapping("/kakaoLogin")
    public ResponseEntity<AuthTokens> loginKakao(@RequestParam String code) {
        KakaoLoginParams kakaoLoginParams = new KakaoLoginParams(code);
        return ResponseEntity.ok(oAuthLoginService.login(kakaoLoginParams));
    }

}
