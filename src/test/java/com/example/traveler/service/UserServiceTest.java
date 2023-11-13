package com.example.traveler.service;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import com.example.traveler.config.BaseException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class UserServiceTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void TestUserController() throws BaseException{

        String authToken = "aa";

        mockMvc
                .perform(
                        get("/users/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header()
                        .andExpect(status().isOk());
        )
    }



"""

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testUpdateNicknameById() throws BaseException {
        // 가짜 유저 데이터 생성
        User user = new User();
        user.setId(1L);
        user.setNickname("N");

        // 업데이트할 닉네임
        UpdateNicknameDTO updateNicknameDTO = new UpdateNicknameDTO();
        updateNicknameDTO.setNickname("NewNickname");

        // 테스트 실행 및 예외 처리
        assertThrows(BaseException.class, () -> userService.updateNicknameById(1L, updateNicknameDTO));
    }

    @Test
    void testDeleteUser() throws BaseException {
        // 가짜 유저 데이터 생성
        User user = new User();
        user.setId(1L);

        // UserRepository의 findById 메서드가 호출될 때 가짜 유저 데이터 반환하도록 설정
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // 테스트 실행 및 예외 처리
        assertThrows(BaseException.class, () -> userService.deleteUser(1L));
    }

    @Test
    void testGetUser() {
        // 가짜 유저 데이터 생성
        User user = new User();
        user.setId(1L);

        // UserRepository의 findById 메서드가 호출될 때 가짜 유저 데이터 반환하도록 설정
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // 반환된 유저가 예상과 일치하는지 확인
        assertEquals(user.getId(), userService.getUser(1L).getId());
    }

    @Test
    void testGetUserByToken() {
        // 가짜 토큰 및 유저 ID 설정
        String accessToken = "fakeAccessToken";
        Long userId = 1L;

        // JwtTokenProvider의 extractId 메서드가 호출될 때 가짜 유저 ID 반환하도록 설정
        when(jwtTokenProvider.extractId(accessToken)).thenReturn(userId);

        // 가짜 유저 데이터 생성
        User user = new User();
        user.setId(userId);

        // UserRepository의 findById 메서드가 호출될 때 가짜 유저 데이터 반환하도록 설정
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // 테스트 실행
        User retrievedUser = userService.getUserByToken(accessToken);

        // 반환된 유저가 예상과 일치하는지 확인
        assertEquals(userId, retrievedUser.getId());
    }
"""
}