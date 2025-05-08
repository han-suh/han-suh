package com.artu.fullstack_team_project_application.controller;

import com.artu.fullstack_team_project_application.entity.users.user.User;
import com.artu.fullstack_team_project_application.entity.users.user.UserloginLogs;
import com.artu.fullstack_team_project_application.service.users.UserLoginLogService;
import com.artu.fullstack_team_project_application.service.users.UserService;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Controller
@RequestMapping("/user")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")

public class UserController {

    private final UserService userService;
    private final EntityManager entityManager;
    private final UserLoginLogService userLoginLogService;

    @GetMapping("/signIn.do")
    public String signInForm(@SessionAttribute(required = false) User user) {
        if (user != null) { // 세션에 사용자 정보가 있으면, 마이페이지로 리다이렉트
            return "redirect:/posting/" + user.getUserId() + "/userpage.do";
        }
        return "/user/signIn";  // 세션에 사용자 정보가 없어야지 로그인 페이지로 이동
    }


    @PostMapping("/signIn.do")
    public String signIn(
            @RequestParam("userId") String username,
            @RequestParam("typedPw") String typedPw,
            HttpServletRequest request,
            HttpSession session) {

        Optional<User> userOptional = userService.readOne(username);
        String UserPw = userOptional.get().getPassword();

        if (userOptional.isPresent() && BCrypt.checkpw(typedPw, UserPw)) { // 사용자 확인 성공하면,
            // 세션에 사용자 정보 저장
            session.setAttribute("user", userOptional.get());

            // 로그인 로그 기록 저장
            String userAgent = request.getHeader("User-Agent");  // user-agent 가져오기
            String ipAddress = request.getRemoteAddr();  // 클라이언트 IP 주소 가져오기
            UserloginLogs userloginLogs = new UserloginLogs(); // 정보 넣을 객체 생성
            userloginLogs.setUserId(userOptional.get().getUserId());
            userloginLogs.setUserAgent(userAgent);
            userloginLogs.setIpAddress(ipAddress);
            userloginLogs.setLoginAt(LocalDateTime.now());
            userLoginLogService.save(userloginLogs); // 로그인 로그 기록

            return "redirect:/";  // 로그인 후 홈 화면으로 리다이렉트
        } else {
            return "redirect:/user/signIn";
        }
    }

    @GetMapping("/signUp.do")
    public String signUpForm(@SessionAttribute(required = false) User user) {
        if (user != null) { // 세션에 사용자 정보가 있으면 마이페이지로 리다이렉트
            return "redirect:/user/mypage";  // 마이페이지로 리다이렉트
        }
        // 세션에 사용자 정보가 없으면 로그인 페이지로 이동
        return "/user/signUp";  // 로그인 페이지
    }

    @PostMapping("/signUp.do") // Action페이지 = 리다이렉트 할거임.
    public String signUpAction(
            @ModelAttribute User user, // 폼에서 전송된 데이터를 자동으로 객체로 변환해주는 역할
            RedirectAttributes redirectAttributes
    ) throws Exception {
        String pwBcrypt = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(pwBcrypt);
        user.setCreatedAt(LocalDate.now());
        User result = userService.save(user);
        // user가 null이 아니면 성공
        if (result != null) { // 성공하면, 리다이렉트 하고 user 아이디 같이 보내기
            // redirectAttributes.addFlashAttribute("userId", user.getUserId());
            return "redirect:/user/signIn.do";
        } else { // 실패시 등록페이지 머뭄. 적은 데이터 다 초기화 되니까, 리액트로 인풋값 검증해야함.
            return "redirect:/user/singUp.do";
        }
    }

    @GetMapping("/signOut.do")
    public String logout(HttpSession session) {
        session.invalidate(); // 세션 무효화
        return "redirect:/"; // 로그인 페이지로 리다이렉트
    }

    @GetMapping("/api/checkUserId.do")
    @ResponseBody // JSON 형식으로 반환
    public boolean checkUserId(@RequestParam String userId) {
        return userService.checkUserExists(userId);
    }

    @GetMapping("/api/checkUserEmail.do")
    @ResponseBody // JSON 형식으로 반환
    public boolean checkUserEmail(@RequestParam String UserEmail) {
        return userService.checkUserEmailExists(UserEmail);
    }


    @GetMapping("/checkPw.do")
    public String checkPw() {
        return "user/checkPw";
    }

    @PostMapping("/checkPw.do")
    public String checkPwAction(
            @SessionAttribute(required = true) User user,
            @RequestParam("inputPw") String inputPw,  // 사용자가 다시 입력한 비밀번호
            HttpSession session,  // 세션에서 사용자 정보 가져오기
            Model model) // 모델을 사용하여 데이터 전달
    {
        String encryptedPassword = user.getPassword();  // 세션에서 가져온 암호화된 비밀번호
        if (BCrypt.checkpw(inputPw, encryptedPassword)) { // 입력한 비밀번호와 세션에 저장된 암호화된 비밀번호 비교
            // 비밀번호가 일치하면, user/updatePw.do로 리다이렉트
            return "redirect:/user/updatePw.do";
        } else {
            // 비밀번호가 일치하지 않으면, 다시 입력을 요구
            model.addAttribute("error", "비밀번호가 일치하지 않습니다.");
            return "passwordCheck";  // 다시 비밀번호 확인 페이지로 리턴
        }
    }

    @GetMapping("updatePw.do")
    public String updatePw() {
        return "user/updatePw";
    }

    @PostMapping("updatePw.do")
    public String updatePwAction(
            @SessionAttribute(required = true) User user,
            @RequestParam("newPw") String newPw) {

        String userId = user.getUserId(); // 세션에서 user ID 가져오기

        try {
            userService.updatePassword(userId, newPw); // 비번 업데이트
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/";  // 비밀번호 변경 후 리다이렉트 또는 적절한 처리
    }


}














