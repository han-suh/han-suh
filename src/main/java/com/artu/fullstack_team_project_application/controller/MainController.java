package com.artu.fullstack_team_project_application.controller;

import com.artu.fullstack_team_project_application.entity.users.user.User;
import com.artu.fullstack_team_project_application.service.users.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@AllArgsConstructor
public class MainController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String index(HttpSession session) {
//        session.getAttribute("user"); // getAttribute는 Object를 반환함
        // 형변환 안 하면 session.user.id처럼 필드 접근 못함. Object 타입은 필드나 메서드에 직접 접근할 수 없음.
        // Thymeleaf는 내부에서 자동으로 형변환하고 객체를 분석함.
//        return "fragments/widget/widget"; // 홈 화면
        return "forward:index.html";
    }
}

