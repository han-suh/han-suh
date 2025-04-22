package com.artu.fullstack_team_project_application.controller;

import com.artu.fullstack_team_project_application.dto.UserPageDto;
import com.artu.fullstack_team_project_application.entity.postings.Posting;
import com.artu.fullstack_team_project_application.entity.postings.UserFollow;
import com.artu.fullstack_team_project_application.entity.users.user.User;
import com.artu.fullstack_team_project_application.service.postings.PostingService;
import com.artu.fullstack_team_project_application.service.users.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@RestController
//@Controller
@RequestMapping("/posting")
@CrossOrigin(origins = "http://localhost:4775", allowCredentials = "true")
@AllArgsConstructor
public class PostingController {
    private final PostingService postingService;
    private final UserService userService;

    @GetMapping("/findAll.do")
    public String findAll(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user); // 세션관리

        List<User> userList = userService.findAll();
        model.addAttribute("users", userList);
        model.addAttribute("headerNav", "personal");

        return "/posting/findAll";
    }

    @GetMapping("/{userId}/userpage.do")
    public ResponseEntity<Object> userPage(@PathVariable String userId, HttpSession session) {
        UserPageDto userPageDto = userService.readUserPage(userId);
        if (userPageDto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userPageDto);
    }

//    @GetMapping("/{userId}/userpage.do")
//    public String userpage(
//            @PathVariable String userId,
//            Model model) {
//        UserPageDto userPageDto=userService.readUserPage(userId);
//        System.out.println("userPageDto:" + userPageDto);
//        if (userPageDto==null) {
//            return "redirect:/artu.do";
//        }
//        model.addAttribute("userPage", userPageDto);
//
//        // 템플릿에 user, followerCounts, followeeCounts를 전달
//        return "/posting/userpage";
//    }

    // 게시물 비동기식
    @GetMapping("/{userId}/postpage.do")
    @ResponseBody
    public ResponseEntity<Set<Posting>> postpage(
            @PathVariable String userId) {
        Set<Posting> postings = postingService.findByUserId(userId);
        // return ResponseEntity.ok(postings);
        return ResponseEntity.status(201).body(postings);
    }

    @PostMapping
    public ResponseEntity<Posting> createPosting(@ModelAttribute Posting posting, HttpSession session) {
        return ResponseEntity.status(201).body(postingService.save(posting));
    }

//    @GetMapping("/{userId}/postAdd.do")
//    public String postForm(
//            @ModelAttribute Posting posting
//    ){
//        return "/posting/postAdd";
//    }




}
