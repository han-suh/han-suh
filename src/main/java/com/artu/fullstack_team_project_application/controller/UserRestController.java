package com.artu.fullstack_team_project_application.controller;

import com.artu.fullstack_team_project_application.dto.users.LoginRequestDto;
import com.artu.fullstack_team_project_application.dto.users.LoginResponseAuthDto;
import com.artu.fullstack_team_project_application.dto.users.OAuthUser;
import com.artu.fullstack_team_project_application.entity.users.user.User;
import com.artu.fullstack_team_project_application.jwt.JwtUtil;
import com.artu.fullstack_team_project_application.service.users.CustomUserDetailsService;
import com.artu.fullstack_team_project_application.service.users.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController // SPA이고, 그래서 jwt를 로컬저장소에 넣으므로, 앞으로 상태만 보낼거니까. RestController로 구현
@RequestMapping("/user")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class UserRestController {

    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final CustomUserDetailsService customUserDetailsService;
    private final Logger logger = LoggerFactory.getLogger(UserController.class);


    /*
     로그인폼 -> jwt/login.do [POST] 요청 {id:경민,pw:1234}
     -> 로그인이 유효하면 loginAction 컨트롤러(이 메서드)에서 jwt 토큰 생성 후 응답.
     -> 그러면 뷰에서 다시 jwt new 토큰을 받아서 로컬스토리지에 저장      */
    @PostMapping("/jwt/login.do")
    public ResponseEntity<LoginResponseAuthDto> loginAction(
            @Valid @RequestBody LoginRequestDto loginRequestDto) {

        Optional<User> userOpt = userService.loginHashCheck(loginRequestDto);

        if (userOpt.isPresent()) {
            String jwt = jwtUtil.generateToken(userOpt.get().getUserId()); // user의 아이디로 jwt 만들기
            LoginResponseAuthDto loginResponseAuthDto
                    = LoginResponseAuthDto.builder().jwt(jwt).user(userOpt.get()).build();
            return ResponseEntity.ok(loginResponseAuthDto);
            // -> 그러면 뷰에서 다시 jwt new 토큰을 받아서 로컬스토리지에 저장
        }
        // 로그인 이 실패시, 403 = 인증이 안됨 (유저가 없거나 비밀번호가 틀림)
        return ResponseEntity.status(403).build();
    }



    @GetMapping("/jwt/check.do")
    public ResponseEntity<LoginResponseAuthDto> checkLogin(

            @AuthenticationPrincipal UserDetails userDetails // userDetails가 null이 아니면 로그인 되어있다는 말.
            // 이미 앞선 필터에서 인증인가 과정을 마치고 이곳에 도착한 것.
            // @AuthenticationPrincipal 는 로그인된 사용자 정보를 컨트롤러에서 바로 꺼내쓸 수 있게 해주는 도구
    ) {
        System.out.println("ㅏㅏㅏㅏㅏㅏㅏ$%^*$*#^$%#*&(&*(*$%^$%^$%$#: 로그인 되었는지 체크 시작" + userDetails);
        String userId = userDetails.getUsername();
        String jwt = jwtUtil.generateToken(userId); // 로그인 사용자의 id로 새로운 토큰 만들기
        Optional<User> userOpt = userService.readOne(userId); //  로그인 사용자의 id로 DB에서 User 찾기

        LoginResponseAuthDto loginResponseAuthDto = new LoginResponseAuthDto();
        if (userOpt.isPresent()) { // 있다는 것은 사용자 정보가 있다는 것.
            User user = userOpt.get();
            loginResponseAuthDto.setUser(user);
            loginResponseAuthDto.setJwt(jwt);
            return ResponseEntity.ok(loginResponseAuthDto);
        }
        return ResponseEntity.badRequest().build();
    }



    @PostMapping("/oauth/login.do")
    public ResponseEntity<LoginResponseAuthDto> oauthLoginAction(@Valid @RequestBody OAuthUser oAuthUser) {
        // 보내진 객체 데이터를 OAuthUser DTO에 맞게 받음

        System.out.println("ㅏㅏㅏㅏㅏㅏㅏ$%^*$*#^$%#*&(&*(*$%^$%^$%$#:  oAuthUser: " + oAuthUser);

        Optional<User> userOpt = userService.readOne(oAuthUser.getEmail());

        if (userOpt.isPresent()) { // 있음
            User user = userOpt.get();
            LoginResponseAuthDto loginResponseAuthDto = new LoginResponseAuthDto(); // 로그인 dto 만들기
            loginResponseAuthDto.setUser(user); // user 넣기
            if (user.getOauth().equals(oAuthUser.getOauth())) { // 구글로 가입했는데, 들어온 요청인 구글인지. 카카오는 아닌지.

                // 맞으면 로그인 성공 => jwt 발급해서 로그인 유지할 수 있게 보내기.
                String jwt = jwtUtil.generateToken(user.getUserId());
                loginResponseAuthDto.setJwt(jwt);

                return ResponseEntity.ok(loginResponseAuthDto);
            }
            return ResponseEntity.status(409).body(loginResponseAuthDto); // 가입된 소셜 아이디말고 다른 소셜로 로그인 시도했을 경우

        }
        return ResponseEntity.notFound().build(); // 404 / 가입된 유저가 없어서 가입페이지로 이동

    }




}

