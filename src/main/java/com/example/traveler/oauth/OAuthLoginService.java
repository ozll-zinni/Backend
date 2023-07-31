package com.example.traveler.oauth;

<<<<<<< HEAD
=======
import com.example.traveler.config.BaseException;
>>>>>>> 0f903cca70b820824c4409422dd89978138f38d2
import com.example.traveler.model.entity.User;
import com.example.traveler.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

<<<<<<< HEAD
=======
import static com.example.traveler.config.BaseResponseStatus.EMPTY_JWT;
import static com.example.traveler.config.BaseResponseStatus.INVALID_AUTHORIZATION_CODE;

>>>>>>> 0f903cca70b820824c4409422dd89978138f38d2
@Service
@RequiredArgsConstructor
public class OAuthLoginService {
    private final UserRepository userRepository;
    private final AuthTokensGenerator authTokensGenerator;
    private final RequestOAuthInfoService requestOAuthInfoService;

<<<<<<< HEAD
    public AuthTokens login(OAuthLoginParams params) {
        OAuthInfoResponse oAuthInfoResponse = requestOAuthInfoService.request(params);
        Long userId = findOrCreateUser(oAuthInfoResponse);
        return authTokensGenerator.generate(userId);
=======
    public AuthTokens login(OAuthLoginParams params) throws BaseException {
        try{
            OAuthInfoResponse oAuthInfoResponse = requestOAuthInfoService.request(params);
            Long userId = findOrCreateUser(oAuthInfoResponse);
            return authTokensGenerator.generate(userId);
        } catch (Exception exception) {
            throw new BaseException(INVALID_AUTHORIZATION_CODE);
        }
>>>>>>> 0f903cca70b820824c4409422dd89978138f38d2
    }

    private Long findOrCreateUser(OAuthInfoResponse oAuthInfoResponse) {

        return userRepository.findByKakao(oAuthInfoResponse.getKakao())
                .map(User::getId)
                .orElseGet(() -> newUser(oAuthInfoResponse));
    }

    private Long newUser(OAuthInfoResponse oAuthInfoResponse) {

        User user = User.builder()
                .name(oAuthInfoResponse.getName())
                .email(oAuthInfoResponse.getEmail())
                .nickname(oAuthInfoResponse.getNickname())
                .kakao(oAuthInfoResponse.getKakao())
                .profile_image_url(oAuthInfoResponse.getProfile_image_url())
                .build();

        return userRepository.save(user).getId();
    }
}
